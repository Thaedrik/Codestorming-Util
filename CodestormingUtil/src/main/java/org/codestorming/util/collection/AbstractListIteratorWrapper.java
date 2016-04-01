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

import java.util.ListIterator;

/**
 * Implements {@link ListIterator} methods that do not modify the list state.
 *
 * @author Thaedrik <thaedrik@codestorming.org>
 * @since 1.2
 */
public abstract class AbstractListIteratorWrapper<T> implements ListIterator<T> {

	protected final ListIterator<T> iter;

	protected T last;

	public AbstractListIteratorWrapper(ListIterator<T> iterator) {
		iter = iterator;
	}

	@Override
	public boolean hasNext() {
		return iter.hasNext();
	}

	@Override
	public T next() {
		last = iter.next();
		return last;
	}

	@Override
	public boolean hasPrevious() {
		return iter.hasPrevious();
	}

	@Override
	public T previous() {
		last = iter.previous();
		return last;
	}

	@Override
	public int nextIndex() {
		return iter.nextIndex();
	}

	@Override
	public int previousIndex() {
		return iter.previousIndex();
	}
}
