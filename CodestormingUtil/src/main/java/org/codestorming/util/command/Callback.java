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
package org.codestorming.util.command;

/**
 * Defines a callback with a parameter.
 *
 * @author Thaedrik [thaedrik@codestorming.org]
 * @param <T> The type of the parameter that will be passed to the call method.
 */
public interface Callback<T> {

	public void call(T parameter);
}
