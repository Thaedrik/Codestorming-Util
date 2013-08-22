/***************************************************************************
 * Copyright (c) 2013 Codestorming.org.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Codestorming - initial API and implementation
 ****************************************************************************/
package org.codestorming.util.command;

/**
 * Abstract implementation of the {@link CommandWithResult} interface.
 * 
 * @param <T> The type of the result.
 * @author Thaedrik <thaedrik@gmail.com>
 */
public abstract class ACommandWithResult<T> implements CommandWithResult<T> {

	private T result;

	private Runnable callback;

	@Override
	public T getResult() {
		return result;
	}

	@Override
	public final void run() {
		execute();
		if (callback != null) {
			callback.run();
		}
	}

	/**
	 * Called when the {@link #run()} method is invoked.
	 */
	protected abstract void execute();

	@Override
	public void setCallback(Runnable callback) {
		this.callback = callback;
	}

	/**
	 * Set the result of this command.
	 * 
	 * @param result The result to set.
	 */
	protected void setResult(T result) {
		this.result = result;
	}
}
