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

/**
 * Listener notified when the observed collection has changed.
 *
 * @author Thaedrik <thaedrik@codestorming.org>
 * @since 1.2
 */
public interface CollectionChangeListener<T> {

	/**
	 * Notifies this listener, the {@code source} collection has changed.
	 *
	 * @param source The source of the notification.
	 * @param changes The changes that happened on the source.
	 */
	void onChange(ObservableCollection<T> source, Iterable<CollectionChange<T>> changes);
}
