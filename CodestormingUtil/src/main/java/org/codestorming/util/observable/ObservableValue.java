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
 * {@code ObservableValue} references an object and notifies the registered {@link ChangeListener}s when the reference
 * to the object changes.
 *
 * @author Thaedrik [thaedrik@codestorming.org]
 * @since 1.2
 */
public interface ObservableValue<T> {

	/**
	 * Returns the observed value.
	 *
	 * @return the observed value.
	 */
	T get();

	/**
	 * Changes the value and notifies the registered listeners of the change.
	 *
	 * @param value the new value.
	 */
	void set(T value);

	/**
	 * Add the given {@link ChangeListener} to this {@code ObservableValue}.
	 *
	 * @param listener The {@link ChangeListener} to add.
	 */
	void addChangeListener(ChangeListener<T> listener);

	/**
	 * Remove the given {@link ChangeListener} from this {@code ObservableValue}.
	 *
	 * @param listener The {@link ChangeListener} to remove.
	 */
	void removeChangeListener(ChangeListener<T> listener);
}
