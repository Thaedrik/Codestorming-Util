/***************************************************************************
 * Copyright (c) 2013 Codestorming.org.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Codestorming - initial API and implementation
 ****************************************************************************/
package org.codestorming.util.collection;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Set;

/**
 * This class implements the {@link Set} and the {@link List} interfaces. It is backed by
 * a {@code HashMap} and uses an array to keep the insertion order.
 * <p>
 * The addition of an object already present in the set does nothing, that is, the index
 * at which the object was first inserted does not change.
 * <p>
 * The {@link #subList(int, int)} method and the {@link ListIterator}'s methods
 * <em>set</em> and <em>add</em> are <strong>not supported</strong> <TODO : implements
 * these methods>.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class OrderedHashSet<E> extends HashSet<E> implements List<E>, RandomAccess {

	private static final long serialVersionUID = 1L;

	/**
	 * Default initial capacity.<br>
	 * The capacity is the number of elements this set can have without resizing itself.
	 */
	private static final int DEFAULT_CAPACITY = 10;

	/**
	 * Default load factor.<br>
	 * The load factor is the ratio used to increase the capacity when the set is full.
	 */
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * Internal sequential collection of the elements put in the set
	 */
	private E[] elements;

	/**
	 * The actual load factor.
	 */
	private float loadFactor = DEFAULT_LOAD_FACTOR;

	/**
	 * Creates a new {@code OrderedHashSet}.
	 */
	@SuppressWarnings("unchecked")
	public OrderedHashSet() {
		elements = (E[]) new Object[DEFAULT_CAPACITY];
	}

	/**
	 * Creates a new {@code OrderedHashSet}.
	 * 
	 * @param c
	 */
	@SuppressWarnings("unchecked")
	public OrderedHashSet(Collection<? extends E> c) {
		super(Math.max((int) (c.size() / .75f) + 1, 16));
		elements = (E[]) new Object[Math.max((int) (c.size() / .75f) + 1, 16)];
		addAll(c);
	}

	/**
	 * Creates a new {@code OrderedHashSet}.
	 * 
	 * @param initialCapacity
	 * @param loadFactor
	 */
	@SuppressWarnings("unchecked")
	public OrderedHashSet(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		this.loadFactor = loadFactor;
		elements = (E[]) new Object[initialCapacity];
	}

	/**
	 * Creates a new {@code OrderedHashSet}.
	 * 
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public OrderedHashSet(int initialCapacity) {
		super(initialCapacity);
		elements = (E[]) new Object[initialCapacity];
	}

	@Override
	public boolean add(E e) {
		ensureCapacity(size() + 1);
		return internalAdd(size(), e);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return addAll(size(), c);
	}

	@Override
	public boolean remove(Object o) {
		final int index = indexOf(o);
		if (internalRemove(index, o)) {
			compaction();
			return true;
		}// else
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		if (size() > c.size()) {
			for (Iterator<?> i = c.iterator(); i.hasNext();) {
				final Object o = i.next();
				final int index = indexOf(o);
				modified |= internalRemove(index, o);
			}
		} else {
			for (Iterator<E> i = iterator(); i.hasNext();) {
				if (c.contains(i.next())) {
					i.remove();
					modified = true;
				}
			}
		}
		if (modified) {
			compaction();
		}
		return modified;
	}

	private boolean internalRemove(int index, Object o) {
		if (index >= 0 && super.remove(o)) {
			internalListRemove(index);
			return true;
		}// else
		return false;
	}

	private void internalListRemove(int index) {
		if (index < elements.length - 1) {
			System.arraycopy(elements, index + 1, elements, index, elements.length - (index + 1));
		} else {
			elements[index] = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void clear() {
		super.clear();
		elements = (E[]) new Object[DEFAULT_CAPACITY];
	}

	@Override
	public Iterator<E> iterator() {
		return listIterator(0);
	}

	@Override
	public Object[] toArray() {
		final Object[] array = new Object[size()];
		System.arraycopy(elements, 0, array, 0, size());
		return array;
	}

	// Taken from AbstractCollection
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Object> T[] toArray(T[] a) {
		// Estimate size of array; be prepared to see more or fewer elements
		int size = size();
		T[] r = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		Iterator<E> it = iterator();

		for (int i = 0; i < r.length; i++) {
			if (!it.hasNext()) { // fewer elements than expected
				if (a != r)
					return Arrays.copyOf(r, i);
				r[i] = null; // null-terminate
				return r;
			}
			r[i] = (T) it.next();
		}
		return it.hasNext() ? finishToArray(r, it) : r;
	}

	/**
	 * Reallocates the array being used within toArray when the iterator
	 * returned more elements than expected, and finishes filling it from
	 * the iterator.
	 * <p>
	 * <em>Taken from {@link AbstractCollection}</em>
	 * 
	 * @param r the array, replete with previously stored elements
	 * @param it the in-progress iterator over this collection
	 * @return array containing the elements in the given array, plus any
	 *         further elements returned by the iterator, trimmed to size
	 */
	@SuppressWarnings("unchecked")
	private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
		int i = r.length;
		while (it.hasNext()) {
			int cap = r.length;
			if (i == cap) {
				int newCap = ((cap / 2) + 1) * 3;
				if (newCap <= cap) { // integer overflow
					if (cap == Integer.MAX_VALUE)
						throw new OutOfMemoryError("Required array size too large");
					newCap = Integer.MAX_VALUE;
				}
				r = Arrays.copyOf(r, newCap);
			}
			r[i++] = (T) it.next();
		}
		// trim if overallocated
		return (i == r.length) ? r : Arrays.copyOf(r, i);
	}

	/*
	 * List methods
	 */

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}// else
		ensureCapacity(size() + c.size());
		boolean modified = false;
		final Iterator<? extends E> i = c.iterator();
		int insertionIndex = index;
		while (i.hasNext()) {
			if (internalAdd(insertionIndex, i.next())) {
				modified = true;
				insertionIndex++;
			}
		}
		return modified;
	}

	private boolean internalAdd(int index, E e) {
		if (super.add(e)) {
			if (index < size() - 1) {
				System.arraycopy(elements, index, elements, index + 1, size() - index);
			}
			elements[index] = e;
			return true;
		}// else
		return false;
	}

	@Override
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}// else
		return elements[index];
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If the given {@code element} already exists in this set, it will be
	 * <strong>moved</strong> to the given index and the element at this index will be
	 * <strong>removed</strong>.
	 */
	@Override
	public E set(int index, E element) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}// else
		final int elementIndex = indexOf(element);
		if (elementIndex >= 0) {
			internalListRemove(elementIndex);
		} else {
			super.add(element);
		}
		final E previousElement = elements[index];
		super.remove(previousElement);
		elements[index] = element;
		return previousElement;
	}

	@Override
	public void add(int index, E element) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}// else
		ensureCapacity(size() + 1);
		internalAdd(index, element);
	}

	@Override
	public E remove(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}// else
		final E element = elements[index];
		if (super.remove(element)) {
			internalListRemove(index);
		}
		return element;
	}

	@Override
	public int indexOf(Object o) {
		final E[] elements = this.elements;
		final int size = size();
		if (o == null) {
			for (int i = 0; i < size; i++) {
				if (elements[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (o.equals(elements[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		// This is a Set, there is at most one occurence of an object.
		return indexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(final int index) {
		return new OrderedSetIterator<E>(this, index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	};

	@SuppressWarnings("unchecked")
	private void ensureCapacity(int newCapacity) {
		final int minCapacity = newCapacity;
		final int capacity = elements.length;
		if (newCapacity > capacity) {
			newCapacity = (int) (newCapacity * (loadFactor + 1)) + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			final E[] newArray = (E[]) new Object[newCapacity];
			System.arraycopy(elements, 0, newArray, 0, size());
			elements = newArray;
		}
	}

	@SuppressWarnings("unchecked")
	private void compaction() {
		final int newSize = elements.length / 2;
		if (size() < newSize) {
			final E[] newArray = (E[]) new Object[newSize];
			System.arraycopy(elements, 0, newArray, 0, size());
			elements = newArray;
		}
	}

	static class OrderedSetIterator<T> implements ListIterator<T> {
		private final OrderedHashSet<T> orderedHashSet;
		private int currentIndex;
		private int lastReturned = -1;
		// Flag indicating the last returned element has been removed
		private boolean removed;

		/**
		 * Creates a new {@code OrderedSetIterator}.
		 */
		OrderedSetIterator(OrderedHashSet<T> orderedHashSet, int index) {
			this.orderedHashSet = orderedHashSet;
			currentIndex = index;
		}

		@Override
		public boolean hasNext() {
			return currentIndex < orderedHashSet.size();
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}// else
			removed = false;// Cleaning removed flag
			lastReturned = currentIndex;
			return orderedHashSet.elements[currentIndex++];
		}

		@Override
		public void remove() {
			if (lastReturned < 0) {
				throw new IllegalStateException("You must call next() or previous() before.");
			} else if (removed) {
				throw new IllegalStateException("The current element has already been removed.");
			}// else
			if (orderedHashSet.internalRemove(lastReturned, orderedHashSet.elements[lastReturned])) {
				removed = true;
				if (lastReturned < currentIndex) {
					currentIndex--;
				}
			}
		}

		@Override
		public boolean hasPrevious() {
			return currentIndex > 0;
		}

		@Override
		public T previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}// else
			removed = false;// Cleaning removed flag
			lastReturned = --currentIndex;
			return orderedHashSet.elements[currentIndex];
		}

		@Override
		public int nextIndex() {
			return currentIndex;
		}

		@Override
		public int previousIndex() {
			return currentIndex - 1;
		}

		@Override
		public void set(T e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(T e) {
			throw new UnsupportedOperationException();
		}
	}
}
