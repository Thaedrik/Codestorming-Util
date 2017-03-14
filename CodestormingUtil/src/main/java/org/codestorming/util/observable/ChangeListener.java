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
package org.codestorming.util.observable;

/**
 * Listener notified when the listened {@link ObservableValue} changes of value.
 *
 * @author Thaedrik [thaedrik@codestorming.org]
 * @since 1.2
 */
public interface ChangeListener<T> {

	/**
	 * Notified by the given {@code source} that the value has changed.
	 *
	 * @param source The source of the notification.
	 * @param oldValue The old value.
	 * @param newValue The new value.
	 */
	void onChange(ObservableValue<T> source, T oldValue, T newValue);
}
