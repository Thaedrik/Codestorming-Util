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
 * Describes a change that happend on an {@link ObservableCollection}.
 *
 * @author Thaedrik <thaedrik@codestorming.org>
 * @since 1.2
 */
public class CollectionChange<T> {

	/**
	 * Type of change that can occur on an observable collection.
	 */
	public enum ChangeType {
		ADD,
		REMOVE,
	}

	protected final ChangeType type;

	protected final T value;

	public CollectionChange(ChangeType type, T value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns the type of change that occured on the collection.
	 *
	 * @return the type of change that occured on the collection.
	 */
	public ChangeType getType() {
		return type;
	}

	/**
	 * Returns the value concerning the change.
	 * <p/>
	 * Returns the removed element if the change type is {@link ChangeType#REMOVE}
	 * <p/>
	 * Returns the added element if the change type is {@link ChangeType#ADD}
	 *
	 * @return the value concerning the change.
	 */
	public T getValue() {
		return value;
	}
}
