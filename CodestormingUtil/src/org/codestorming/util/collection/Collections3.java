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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Utility class that operates on or returns collections from
 * {@code org.codestorming.util.collection} package.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @see OrderedHashSet
 */
public class Collections3 {

	// Suppressing the default constructor, ensuring non-instantiability.
	private Collections3() {}

	/**
	 * Returns a <em>thread-safe</em> {@link OrderedHashSet} backed with the given one.
	 * 
	 * @param orderedHashSet {@link OrderedHashSet} to synchronize.
	 * @return a <em>thread-safe</em> {@link OrderedHashSet} backed with the given one.
	 */
	public static <T> OrderedHashSet<T> synchronizedOrderedHashSet(OrderedHashSet<T> orderedHashSet) {
		return new SynchronizedOrderedHashSet<T>(orderedHashSet);
	}

	/**
	 * Returns an <em>unmodifiable</em> view of the given {@link OrderedHashSet}.
	 * 
	 * @param orderedHashSet the {@link OrderedHashSet} for which an unmodifiable view is
	 *        to be returned.
	 * @return an <em>unmodifiable</em> view of the given {@link OrderedHashSet}.
	 */
	public static <T> OrderedHashSet<T> unmodifiableOrderedSet(OrderedHashSet<T> orderedHashSet) {
		return new UnmodifiableOrderedHashSet<T>(orderedHashSet);
	}

	private static class SynchronizedOrderedHashSet<E> extends OrderedHashSet<E> {

		private static final long serialVersionUID = 1L;

		private OrderedHashSet<E> delegate;

		public SynchronizedOrderedHashSet(OrderedHashSet<E> orderedHashSet) {
			delegate = orderedHashSet;
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

		public synchronized Iterator<E> iterator() {
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

		public synchronized Object clone() {
			return delegate.clone();
		}

		public synchronized ListIterator<E> listIterator() {
			return delegate.listIterator();
		}

		public synchronized ListIterator<E> listIterator(int index) {
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

	private static class UnmodifiableOrderedHashSet<E> extends OrderedHashSet<E> {

		private static final long serialVersionUID = 1L;

		private OrderedHashSet<E> delegate;

		/**
		 * Creates a new {@code Collections3.UnmodifiableOrderedHashSet}.
		 */
		public UnmodifiableOrderedHashSet(OrderedHashSet<E> orderedHashSet) {
			delegate = orderedHashSet;
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

		public Object clone() {
			return delegate.clone();
		}

		public ListIterator<E> listIterator() {
			return listIterator(0);
		}

		public ListIterator<E> listIterator(int index) {
			return new OrderedSetIterator<E>(this, index) {

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
}
