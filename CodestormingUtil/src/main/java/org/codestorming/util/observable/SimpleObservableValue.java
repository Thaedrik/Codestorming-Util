/*
 * Copyright (c) 2012-2016 Codestorming.org
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
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
}
