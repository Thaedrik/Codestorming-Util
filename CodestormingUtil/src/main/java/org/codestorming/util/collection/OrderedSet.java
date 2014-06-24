/****************************************************************************
 * Copyright (c) 2012-2014 Codestorming.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Codestorming - initial implementation and API
 ****************************************************************************/
package org.codestorming.util.collection;

import java.util.List;
import java.util.Set;

/**
 * An {@code OrderedSet} is the union between a {@link List} and a {@link Set}. All
 * elements contained in the {@code OrderedSet} are unique and the insertion order is
 * preserved.
 * <p>
 * The addition of an object already present in the set does nothing, that is, the index
 * at which the object was first inserted does not change.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @see Collections3
 */
public interface OrderedSet<E> extends List<E>, Set<E> {
}
