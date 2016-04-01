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

import java.util.Collection;

/**
 * {@code ObservableCollection} is a {@link Collection} that notifies its referenced listeners of changes that happened
 * on the collection.
 *
 * @author Thaedrik <thaedrik@codestorming.org>
 * @since 1.2
 */
public interface ObservableCollection<E> extends Collection<E> {

	void addChangeListener(CollectionChangeListener<E> listener);

	void removeChangeListener(CollectionChangeListener<E> listener);
}
