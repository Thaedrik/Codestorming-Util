/*
 * Copyright (c) 2012-2017 Codestorming.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Codestorming - initial implementation and API
 */
package org.codestorming.util.observable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Default implementation of an {@link ObservableValue}.
 *
 * @author Thaedrik [thaedrik@codestorming.org]
 * @since 1.2
 */
public class SimpleObservableValue<T> implements ObservableValue<T> {

	protected T value;

	protected final Set<ChangeListener<T>> changeListeners = new HashSet<ChangeListener<T>>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * Creates a {@code SimpleObservableValue} initialized with {@code null}.
	 */
	public SimpleObservableValue() {}

	/**
	 * Creates a {@code SimpleObservableValue} initialized with the given value.
	 *
	 * @param value The value.
	 */
	public SimpleObservableValue(T value) {
		this.value = value;
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public void set(T value) {
		if (value != this.value) {
			T oldValue = this.value;
			this.value = value;
			fireChange(oldValue, value);
		}
	}

	@Override
	public void addChangeListener(ChangeListener<T> listener) {
		writeLock();
		try {
			changeListeners.add(listener);
		} finally {
			writeUnlock();
		}
	}

	@Override
	public void removeChangeListener(ChangeListener<T> listener) {
		writeLock();
		try {
			changeListeners.remove(listener);
		} finally {
			writeUnlock();
		}
	}

	protected void readLock() {
		lock.readLock().lock();
	}

	protected void readUnlock() {
		lock.readLock().unlock();
	}

	protected void writeLock() {
		lock.writeLock().lock();
	}

	protected void writeUnlock() {
		lock.writeLock().unlock();
	}

	protected void fireChange(T oldValue, T newValue) {
		readLock();
		List<ChangeListener<T>> changeListeners;
		try {
			changeListeners = new ArrayList<ChangeListener<T>>(this.changeListeners);
		} finally {
			readUnlock();
		}
		for (ChangeListener<T> listener : changeListeners) {
			listener.onChange(this, oldValue, newValue);
		}
	}

	@Override
	public void bind(ObservableValue<T> observable) {
		bind(observable, false);
	}

	@Override
	public void bind(final ObservableValue<T> observable, boolean notifyChange) {
		Binder<T> binder = new Binder<T>(this, observable);
		binder.bind(notifyChange);
	}

	@Override
	public void unbind(ObservableValue<T> observable) {
		Binder<T> binder = new Binder<T>(this, observable);
		binder.unbind();
	}

	/**
	 * The Binder class references all binds made between {@code SimpleObservableValue} objects.
	 *
	 * @param <T> The type of value.
	 * @since 1.6
	 */
	protected static class Binder<T> {

		protected static final Map<Binder<?>, Binder<?>> binders = new HashMap<Binder<?>, Binder<?>>(100);

		protected static final Lock binderLock = new ReentrantLock();

		private final SimpleObservableValue<T> obs0;

		private final ObservableValue<T> obs1;

		private boolean changing0;

		private boolean changing1;

		private ChangeListener<T> listener0;

		private ChangeListener<T> listener1;

		public Binder(SimpleObservableValue<T> obs0, ObservableValue<T> obs1) {
			if (obs0 == null || obs1 == null) {
				throw new NullPointerException("Observables must not be null");
			} // else

			this.obs0 = obs0;
			this.obs1 = obs1;
		}

		public void bind(boolean notifyChange) {
			// Registers this Binder or throws an exception if
			// it is already registered.
			checkBinder();

			listener0 = new ChangeListener<T>() {
				@Override
				public void onChange(ObservableValue<T> source, T oldValue, T newValue) {
					if (!changing1) {
						changing0 = true;
						obs1.set(newValue);
					} else {
						changing1 = false;
					}
				}
			};
			listener1 = new ChangeListener<T>() {
				@Override
				public void onChange(ObservableValue<T> source, T oldValue, T newValue) {
					if (!changing0) {
						changing1 = true;
						obs0.set(newValue);
					} else {
						changing0 = false;
					}
				}
			};

			if (notifyChange) {
				obs0.set(obs1.get());
			} else {
				obs0.value = obs1.get();
			}

			obs0.addChangeListener(listener0);
			obs1.addChangeListener(listener1);
		}

		protected void checkBinder() throws IllegalStateException {
			binderLock.lock();
			try {
				if (binders.containsKey(this)) {
					throw new IllegalStateException("These two values are already bound together");
				} // else
				binders.put(this, this);
			} finally {
				binderLock.unlock();
			}
		}

		@SuppressWarnings("unchecked")
		public void unbind() {
			Binder<Object> unbound;
			binderLock.lock();
			try {
				unbound = (Binder<Object>) binders.remove(this);
			} finally {
				binderLock.unlock();
			}
			if (unbound != null) {
				unbound.obs0.removeChangeListener(unbound.listener0);
				unbound.obs1.removeChangeListener(unbound.listener1);
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			} // else
			if (obj == null) {
				return false;
			} // else
			if (obj.getClass() != Binder.class) {
				return false;
			}

			Binder<?> other = (Binder<?>) obj;
			return obs0 == other.obs0 && obs1 == other.obs1 || obs0 == other.obs1 && obs1 == other.obs0;
		}

		@Override
		public int hashCode() {
			return obs0.hashCode() + obs1.hashCode();
		}
	}
}
