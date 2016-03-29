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
import java.util.List;
import java.util.ListIterator;

/**
 * {@link ObservableList} implementation by wrapping an existing {@link List}.
 *
 * @author Thaedrik <thaedrik@codestorming.org>
 */
public class ObservableListWrapper<E> extends AbstractObservableCollectionWrapper<E> implements ObservableList<E> {

	private final List<E> wrappedList;

	public ObservableListWrapper(List<E> wrapped) {
		super(wrapped);
		wrappedList = wrapped;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean added = wrappedList.addAll(index, c);
		if (added) {
			fireChange(create(ChangeType.ADD, c));
		}
		return added;
	}

	@Override
	public E get(int index) {
		return wrappedList.get(index);
	}

	@Override
	public E set(int index, E element) {
		E removed = wrappedList.set(index, element);
		List<CollectionChange<E>> changes = new ArrayList<CollectionChange<E>>(2);
		if (removed != null) {
			changes.add(new CollectionChange<E>(ChangeType.REMOVE, removed));
		}
		changes.add(new CollectionChange<E>(ChangeType.ADD, element));
		fireChange(changes);
		return removed;
	}

	@Override
	public void add(int index, E element) {
		wrappedList.add(index, element);
		fireChange(Collections.singletonList(new CollectionChange<E>(ChangeType.ADD, element)));
	}

	@Override
	public E remove(int index) {
		E removed = wrappedList.remove(index);
		if (removed != null) {
			fireChange(Collections.singletonList(new CollectionChange<E>(ChangeType.REMOVE, removed)));
		}
		return removed;
	}

	@Override
	public int indexOf(Object o) {
		return wrappedList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return wrappedList.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new AbstractListIteratorWrapper<E>(wrappedList.listIterator(index)) {
			@Override
			public void remove() {
				iter.remove();
				fireChange(Collections.singletonList(new CollectionChange<E>(ChangeType.REMOVE, last)));
			}

			@Override
			public void set(E e) {
				iter.set(e);
				List<CollectionChange<E>> changes = new ArrayList<CollectionChange<E>>(2);
				if (last != null) {
					changes.add(new CollectionChange<E>(ChangeType.REMOVE, last));
				}
				changes.add(new CollectionChange<E>(ChangeType.ADD, e));
				fireChange(changes);
			}

			@Override
			public void add(E e) {
				iter.add(e);
				fireChange(Collections.singletonList(new CollectionChange<E>(ChangeType.ADD, e)));
			}
		};
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return wrappedList.subList(fromIndex, toIndex);
	}
}
