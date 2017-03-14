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

import org.codestorming.util.collection.CollectionChange.ChangeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Wrapper implementing {@link ObservableCollection} for collections.
 *
 * @author Thaedrik [thaedrik@codestorming.org]
 * @since 1.2
 */
public class AbstractObservableCollectionWrapper<E> extends AbstractObservableCollection<E>
		implements ObservableCollection<E> {

	protected final Collection<E> wrapped;

	protected static <T> List<CollectionChange<T>> create(ChangeType type, Collection<? extends T> values) {
		List<CollectionChange<T>> changes = new ArrayList<CollectionChange<T>>(values.size());
		for (T value : values) {
			changes.add(new CollectionChange<T>(type, value));
		}
		return changes;
	}

	public AbstractObservableCollectionWrapper(Collection<E> wrapped) {
		if (wrapped == null) {
			throw new NullPointerException("The wrapped collection cannot be null");
		}// else
		this.wrapped = wrapped;
	}

	@Override
	public Iterator<E> iterator() {
		return new AbstractIteratorWrapper<E>(wrapped.iterator()) {

			@Override
			public void remove() {
				iter.remove();
				CollectionChange<E> change = new CollectionChange<E>(ChangeType.REMOVE, last);
				fireChange(Collections.singletonList(change));
			}
		};
	}

	@Override
	public Object[] toArray() {
		return wrapped.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return wrapped.toArray(a);
	}

	@Override
	public boolean add(E e) {
		boolean added = wrapped.add(e);
		if (added) {
			fireChange(Collections.singletonList(new CollectionChange<E>(ChangeType.ADD, e)));
		}
		return added;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		boolean removed = wrapped.remove(o);
		if (removed) {
			fireChange(Collections.singletonList(new CollectionChange<E>(ChangeType.REMOVE, (E) o)));
		}
		return removed;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return wrapped.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean added = wrapped.addAll(c);
		if (added) {
			fireChange(create(ChangeType.ADD, c));
		}
		return added;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> c) {
		boolean removed = false;
		List<E> elements = new ArrayList<E>(c.size());
		for (Object o : c) {
			if (wrapped.remove(o)) {
				removed = true;
				elements.add((E) o);
			}
		}
		if (elements.size() > 0) {
			fireChange(create(ChangeType.REMOVE, elements));
		}
		return removed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean retained = false;
		List<E> elements = new ArrayList<E>(c.size());
		final Iterator<E> iter = wrapped.iterator();
		while (iter.hasNext()) {
			E v = iter.next();
			if (!c.contains(v)) {
				iter.remove();
				elements.add(v);
				retained = true;
			}
		}
		if (elements.size() > 0) {
			fireChange(create(ChangeType.REMOVE, elements));
		}
		return retained;
	}

	@Override
	public void clear() {
		List<CollectionChange<E>> changes = create(ChangeType.REMOVE, this);
		wrapped.clear();
		fireChange(changes);
	}

	@Override
	public boolean equals(Object o) {
		return o == this || o instanceof AbstractObservableCollectionWrapper &&
				wrapped.equals(((AbstractObservableCollectionWrapper) o).wrapped);
	}

	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}

	@Override
	public int size() {
		return wrapped.size();
	}

	@Override
	public boolean isEmpty() {
		return wrapped.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return wrapped.contains(o);
	}
}
