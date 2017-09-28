/*
 * Copyright (c) 2012-2017 Codestorming.org
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

	/**
	 * Binds this {@code ObservableValue} to the given one with a bidirectional link.
	 * <p>
	 * <em>Convenient method for {@code bind(observable, false)}.</em>
	 *
	 * @param observable The other {@code ObservableValue} to be bound to.
	 * @see #bind(ObservableValue, boolean)
	 * @since 1.6
	 */
	void bind(ObservableValue<T> observable);

	/**
	 * Binds this {@code ObservableValue} to the given one with a bidirectional link.
	 * <p>
	 * If one of the values changes it will be reflected on the other.
	 * <p>
	 * <strong>NOTE:</strong> When the binding is made, the value of the given {@code ObservableValue} is set to this
	 * one without notifying the change listeners if {@code notifyChange} is {@code false}.
	 *
	 * @param observable The other {@code ObservableValue} to be bound to.
	 * @param notifyChange Indicates if the listeners on this value must be notified of a value change when the binding
	 * is made.
	 * @since 1.6
	 */
	void bind(ObservableValue<T> observable, boolean notifyChange);

	/**
	 * Removed any bind that may exist between this and the other {@code ObservableValue}.
	 *
	 * @param observable The other {@code ObservableValue} to be unbound to.
	 * @since 1.6
	 */
	void unbind(ObservableValue<T> observable);
}
