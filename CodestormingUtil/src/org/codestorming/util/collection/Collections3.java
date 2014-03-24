/****************************************************************************
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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Utility class that operates on or returns collections from
 * {@code org.codestorming.util.collection} package.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @see OrderedSet
 */
public class Collections3 {

	/**
	 * The empty {@link OrderedSet} (immutable). This ordered set is serializable.
	 */
	@SuppressWarnings("rawtypes")
	public static final OrderedSet EMPTY_ORDERED_SET = new EmptyOrderedSet();

	/**
	 * Returns a <em>thread-safe</em> {@link OrderedSet} backed with the given one.
	 * 
	 * @param orderedSet {@link OrderedSet} to synchronize.
	 * @return a <em>thread-safe</em> {@link OrderedSet} backed with the given one.
	 */
	public static <T> OrderedSet<T> synchronizedOrderedSet(OrderedSet<T> orderedSet) {
		return new SynchronizedOrderedSet<T>(orderedSet);
	}

	/**
	 * Returns an <em>unmodifiable</em> view of the given {@link OrderedSet}.
	 * 
	 * @param orderedSet the {@link OrderedSet} for which an unmodifiable view is
	 *        to be returned.
	 * @return an <em>unmodifiable</em> view of the given {@link OrderedSet}.
	 */
	public static <T> OrderedSet<T> unmodifiableOrderedSet(OrderedSet<T> orderedSet) {
		return new UnmodifiableOrderedSet<T>(orderedSet);
	}

	/**
	 * Returns the empty {@link OrderedSet} (immutable). This ordered set is serializable.
	 * 
	 * @return the empty {@link OrderedSet} (immutable). This ordered set is serializable.
	 * @see Collections3#EMPTY_ORDERED_SET EMPTY_ORDERED_SET
	 */
	@SuppressWarnings("unchecked")
	public static <T> OrderedSet<T> emptyOrderedSet() {
		return (OrderedSet<T>) EMPTY_ORDERED_SET;
	}

	private static class SynchronizedOrderedSet<E> implements OrderedSet<E>, Serializable {

		private static final long serialVersionUID = 1845123657602486228L;

		private OrderedSet<E> delegate;

		public SynchronizedOrderedSet(OrderedSet<E> orderedSet) {
			delegate = orderedSet;
		}

		public synchronized boolean equals(Object o) {
			return delegate.equals(o);
		}

		public synchronized int hashCode() {
			return delegate.hashCode();
		}

		public synchronized boolean add(E e) {
			return delegate.add(e);
		}

		public synchronized boolean addAll(Collection<? extends E> c) {
			return delegate.addAll(c);
		}

		public synchronized boolean remove(Object o) {
			return delegate.remove(o);
		}

		public synchronized boolean removeAll(Collection<?> c) {
			return delegate.removeAll(c);
		}

		public synchronized void clear() {
			delegate.clear();
		}

		public Iterator<E> iterator() {
			return delegate.iterator();
		}

		public synchronized Object[] toArray() {
			return delegate.toArray();
		}

		public synchronized <T> T[] toArray(T[] a) {
			return delegate.toArray(a);
		}

		public synchronized boolean addAll(int index, Collection<? extends E> c) {
			return delegate.addAll(index, c);
		}

		public synchronized int size() {
			return delegate.size();
		}

		public synchronized boolean isEmpty() {
			return delegate.isEmpty();
		}

		public synchronized boolean contains(Object o) {
			return delegate.contains(o);
		}

		public synchronized E get(int index) {
			return delegate.get(index);
		}

		public synchronized E set(int index, E element) {
			return delegate.set(index, element);
		}

		public synchronized void add(int index, E element) {
			delegate.add(index, element);
		}

		public synchronized E remove(int index) {
			return delegate.remove(index);
		}

		public synchronized int indexOf(Object o) {
			return delegate.indexOf(o);
		}

		public synchronized boolean containsAll(Collection<?> c) {
			return delegate.containsAll(c);
		}

		public synchronized int lastIndexOf(Object o) {
			return delegate.lastIndexOf(o);
		}

		public ListIterator<E> listIterator() {
			return delegate.listIterator();
		}

		public ListIterator<E> listIterator(int index) {
			return delegate.listIterator(index);
		}

		public synchronized List<E> subList(int fromIndex, int toIndex) {
			return delegate.subList(fromIndex, toIndex);
		}

		public synchronized boolean retainAll(Collection<?> c) {
			return delegate.retainAll(c);
		}

		public synchronized String toString() {
			return delegate.toString();
		}
	}

	private static class UnmodifiableOrderedSet<E> implements OrderedSet<E>, Serializable {

		private static final long serialVersionUID = -3167309036519320200L;

		private OrderedSet<E> delegate;

		/**
		 * Creates a new {@code UnmodifiableOrderedSet}.
		 * 
		 * @param orderedSet
		 */
		public UnmodifiableOrderedSet(OrderedSet<E> orderedSet) {
			delegate = orderedSet;
		}

		public boolean equals(Object o) {
			return delegate.equals(o);
		}

		public int hashCode() {
			return delegate.hashCode();
		}

		public boolean add(E e) {
			throw new UnsupportedOperationException();
		}

		public boolean addAll(Collection<? extends E> c) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}

		public Iterator<E> iterator() {
			return listIterator();
		}

		public Object[] toArray() {
			return delegate.toArray();
		}

		public <T> T[] toArray(T[] a) {
			return delegate.toArray(a);
		}

		public boolean addAll(int index, Collection<? extends E> c) {
			throw new UnsupportedOperationException();
		}

		public int size() {
			return delegate.size();
		}

		public boolean isEmpty() {
			return delegate.isEmpty();
		}

		public boolean contains(Object o) {
			return delegate.contains(o);
		}

		public E get(int index) {
			return delegate.get(index);
		}

		public E set(int index, E element) {
			throw new UnsupportedOperationException();
		}

		public void add(int index, E element) {
			delegate.add(index, element);
		}

		public E remove(int index) {
			throw new UnsupportedOperationException();
		}

		public int indexOf(Object o) {
			return delegate.indexOf(o);
		}

		public boolean containsAll(Collection<?> c) {
			return delegate.containsAll(c);
		}

		public int lastIndexOf(Object o) {
			return delegate.lastIndexOf(o);
		}

		public ListIterator<E> listIterator() {
			return listIterator(0);
		}

		public ListIterator<E> listIterator(final int index) {
			return new ListIterator<E>() {
				private ListIterator<E> internal = delegate.listIterator(index);

				@Override
				public boolean hasNext() {
					return internal.hasNext();
				}

				@Override
				public E next() {
					return internal.next();
				}

				@Override
				public boolean hasPrevious() {
					return internal.hasPrevious();
				}

				@Override
				public E previous() {
					return internal.previous();
				}

				@Override
				public int nextIndex() {
					return internal.nextIndex();
				}

				@Override
				public int previousIndex() {
					return internal.previousIndex();
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

				@Override
				public void set(E e) {
					throw new UnsupportedOperationException();
				}

				@Override
				public void add(E e) {
					throw new UnsupportedOperationException();
				}
			};
		}

		public List<E> subList(int fromIndex, int toIndex) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		public String toString() {
			return delegate.toString();
		}
	}

	private static class EmptyOrderedSet implements OrderedSet<Object>, Serializable {

		private static final long serialVersionUID = 564365190269344842L;

		@Override
		public int size() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return true;
		}

		@Override
		public boolean contains(Object o) {
			return false;
		}

		@Override
		public Iterator<Object> iterator() {
			return listIterator();
		}

		@Override
		public Object[] toArray() {
			return new Object[0];
		}

		@Override
		public <T> T[] toArray(T[] a) {
			int size = size();
			@SuppressWarnings("unchecked")
			T[] r = a.length >= size ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), size);
			return r;
		}

		@Override
		public boolean add(Object e) {
			throw new UnsupportedOperationException("Cannot modify the Empty OrderedSet");
		}

		@Override
		public boolean remove(Object o) {
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends Object> c) {
			throw new UnsupportedOperationException("Cannot modify the Empty OrderedSet");
		}

		@Override
		public boolean addAll(int index, Collection<? extends Object> c) {
			throw new UnsupportedOperationException("Cannot modify the Empty OrderedSet");
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException("Cannot modify the Empty OrderedSet");
		}

		@Override
		public void clear() {}

		@Override
		public Object get(int index) {
			throw new IndexOutOfBoundsException("Index = " + index);
		}

		@Override
		public Object set(int index, Object element) {
			throw new UnsupportedOperationException("Cannot modify the Empty OrderedSet");
		}

		@Override
		public void add(int index, Object element) {
			throw new UnsupportedOperationException("Cannot modify the Empty OrderedSet");
		}

		@Override
		public Object remove(int index) {
			throw new UnsupportedOperationException("Cannot modify the Empty OrderedSet");
		}

		@Override
		public int indexOf(Object o) {
			return -1;
		}

		@Override
		public int lastIndexOf(Object o) {
			return -1;
		}

		@Override
		public ListIterator<Object> listIterator() {
			return listIterator(0);
		}

		@Override
		public ListIterator<Object> listIterator(int index) {
			if (index < 0 || index > size()) {
				throw new IndexOutOfBoundsException("Index = " + index);
			}// else
			return Collections.emptyList().listIterator();
		}

		@Override
		public List<Object> subList(int fromIndex, int toIndex) {
			if (fromIndex < 0 || toIndex > size() || fromIndex > toIndex) {
				throw new IndexOutOfBoundsException();
			}// else
			return Collections.emptyList();
		}

		private Object readResolve() throws ObjectStreamException {
			return EMPTY_ORDERED_SET;
		}
	}

	// Suppressing the default constructor, ensuring non-instantiability.
	private Collections3() {}
}
