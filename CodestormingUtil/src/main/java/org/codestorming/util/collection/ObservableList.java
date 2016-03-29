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

import java.util.List;

/**
 * {@link ObservableCollection} of type {@link List}.
 *
 * @author Thaedrik <thaedrik@codestorming.org>
 */
public interface ObservableList<E> extends ObservableCollection<E>, List<E> {}
