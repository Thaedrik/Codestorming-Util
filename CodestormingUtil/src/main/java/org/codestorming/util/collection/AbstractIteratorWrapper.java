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

import java.util.Iterator;

/**
 * Implements {@link Iterator} methods that do not modify the collection state.
 *
 * @author Thaedrik <thaedrik@codestorming.org>
 * @since 1.2
 */
public abstract class AbstractIteratorWrapper<T> implements Iterator<T> {

	protected final Iterator<T> iter;

	protected T last;

	public AbstractIteratorWrapper(Iterator<T> iterator) {
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
}
