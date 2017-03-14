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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
 * This class implements the {@link OrderedSet} interface. It is backed by
 * a {@code HashSet} and uses an array to keep the insertion order.
 * <p>
 * The addition of an object already present in the set does nothing, that is, the index
 * at which the object was first inserted does not change.
 * <p>
 * The {@link #subList(int, int)} method and the {@link ListIterator}'s methods
 * <em>set</em> and <em>add</em> are <strong>not supported</strong> <TODO : implements
 * these methods>.
 * 
 * @author Thaedrik [thaedrik@codestorming.org]
 */
public class OrderedHashSet<E> implements OrderedSet<E>, RandomAccess, Cloneable, Serializable {

	private static final long serialVersionUID = 935390544812443951L;

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
	 * Internal {@code HashSet}.
	 */
	private transient Set<E> internalSet;

	/**
	 * Internal sequential collection of the elements put in the set
	 */
	private transient E[] elements;

	/**
	 * The actual load factor.
	 */
	private float loadFactor = DEFAULT_LOAD_FACTOR;

	/**
	 * Creates a new {@code OrderedHashSet}.
	 */
	@SuppressWarnings("unchecked")
	public OrderedHashSet() {
		internalSet = new HashSet<E>(DEFAULT_CAPACITY);
		elements = (E[]) new Object[DEFAULT_CAPACITY];
	}

	/**
	 * Creates a new {@code OrderedHashSet}.
	 * 
	 * @param c
	 */
	@SuppressWarnings("unchecked")
	public OrderedHashSet(Collection<? extends E> c) {
		int initialCapacity = Math.max((int) (c.size() / loadFactor) + 1, DEFAULT_CAPACITY);
		internalSet = new HashSet<E>(initialCapacity);
		elements = (E[]) new Object[initialCapacity];
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
		internalSet = new HashSet<E>(initialCapacity, loadFactor);
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
		internalSet = new HashSet<E>(initialCapacity);
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
		if (index >= 0 && internalSet.remove(o)) {
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
	public int size() {
		return internalSet.size();
	}

	@Override
	public boolean isEmpty() {
		return internalSet.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return internalSet.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return internalSet.containsAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		Iterator<E> e = iterator();
		while (e.hasNext()) {
			if (!c.contains(e.next())) {
				e.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void clear() {
		internalSet.clear();
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
			if (!it.hasNext()) {
				// fewer elements than expected
				if (a != r) {
					return Arrays.copyOf(r, i);
				}
				// null-terminate
				r[i] = null;
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
		T[] array = r;
		int i = r.length;
		while (it.hasNext()) {
			int cap = array.length;
			if (i == cap) {
				int newCap = ((cap / 2) + 1) * 3;
				if (newCap <= cap) { // integer overflow
					if (cap == Integer.MAX_VALUE) {
						throw new OutOfMemoryError("Required array size too large");
					}
					newCap = Integer.MAX_VALUE;
				}
				array = Arrays.copyOf(array, newCap);
			}
			array[i++] = (T) it.next();
		}
		// trim if overallocated
		return (i == array.length) ? array : Arrays.copyOf(array, i);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object clone() {
		try {
			OrderedHashSet<E> clone = (OrderedHashSet<E>) super.clone();
			clone.elements = Arrays.copyOf(elements, size());
			clone.internalSet = (HashSet<E>) ((HashSet<E>) internalSet).clone();
			return clone;
		} catch (CloneNotSupportedException ignore) {
			// Should not happen, we are cloneable
			throw new InternalError();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}// else
		if (!(obj instanceof OrderedSet)) {
			return false;
		}// else
		OrderedSet<?> other = (OrderedSet<?>) obj;
		if (size() != other.size()) {
			return false;
		}// else
		ListIterator<E> iter = listIterator();
		ListIterator<?> iter2 = other.listIterator();
		while (iter.hasNext()) {
			E o = iter.next();
			Object o2 = iter2.next();
			if (o == null || o2 == null) {
				if (o != o2) {
					return false;
				}
			} else if (!o.equals(o2)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hashcode = 0;
		for (E elt : this) {
			hashcode = 31 * hashcode + elt.hashCode();
		}
		return hashcode;
	}

	/*
	 * LIST METHODS
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
		if (internalSet.add(e)) {
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
			internalSet.add(element);
		}
		final E previousElement = elements[index];
		internalSet.remove(previousElement);
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
		if (internalSet.remove(element)) {
			internalListRemove(index);
		}
		return element;
	}

	@Override
	public int indexOf(Object o) {
		final E[] elts = elements;
		final int size = size();
		if (o == null) {
			for (int i = 0; i < size; i++) {
				if (elts[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (o.equals(elts[i])) {
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
	}

	@SuppressWarnings("unchecked")
	private void ensureCapacity(int minCapacity) {
		final int capacity = elements.length;
		int newCapacity = minCapacity;
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

	private void writeObject(ObjectOutputStream s) throws IOException {
		final int size = size();
		s.defaultWriteObject();
		s.writeInt(elements.length);
		s.writeInt(size);
		for (int i = 0; i < size; i++) {
			s.writeObject(get(i));
		}
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		int length = s.readInt();
		int size = s.readInt();
		elements = (E[]) new Object[length];
		internalSet = new HashSet<E>(size, loadFactor);
		for (int i = 0; i < size; i++) {
			add((E) s.readObject());
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
