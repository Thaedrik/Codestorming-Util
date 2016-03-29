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
package org.codestorming.util.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Basic implementation of {@link ObservableCollection}.
 * <p/>
 * Adding and removing listeners and firing changes are thread-safe operations.
 * <p/>
 * Implementors must use the following lock methods when using the {@code changeListeners} collection:
 * <pre>
 *     readLock    - Lock in read mode
 *     readUnlock  - Unlock read mode
 *     writeLock   - Lock in write mode
 *     writeUnlock - Unlock write mode
 * </pre>
 * <em>Unlocking should happen in finally blocks to prevent deadlocks when an exception occurs.</em>
 *
 * @author Thaedrik <thaedrik@codestorming.org>
 */
public abstract class AbstractObservableCollection<E> implements ObservableCollection<E> {

	protected final Set<CollectionChangeListener<E>> changeListeners;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	public AbstractObservableCollection() {
		changeListeners = new HashSet<CollectionChangeListener<E>>();
	}

	@Override
	public void addChangeListener(CollectionChangeListener<E> listener) {
		writeLock();
		try {
			changeListeners.add(listener);
		} finally {
			writeUnlock();
		}
	}

	@Override
	public void removeChangeListener(CollectionChangeListener<E> listener) {
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

	protected void fireChange(Iterable<CollectionChange<E>> changes) {
		ArrayList<CollectionChangeListener<E>> listeners;
		try {
			readLock();
			listeners = new ArrayList<CollectionChangeListener<E>>(changeListeners);
		} finally {
			readUnlock();
		}
		for (CollectionChangeListener<E> listener : listeners) {
			listener.onChange(this, changes);
		}
	}
}
