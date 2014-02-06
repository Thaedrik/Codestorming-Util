/****************************************************************************
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
package org.codestorming.util;

/**
 * A {@code Logger} is used to log messages associated with one of three severity levels..
 * <p>
 * Implementers are free to just display these messages in the standard output or to log
 * them in specified files or whatever fits their needs.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public interface Logger {

	/**
	 * Severity level for {@code Logger}'s messages.
	 */
	public static enum Severity {
		INFO,
		WARNING,
		ERROR
	}

	/**
	 * Logs the given {@code message} with the specified {@code severity}.
	 * 
	 * @param severity The {@link Severity} level.
	 * @param message The message to log.
	 */
	public void log(Severity severity, CharSequence message);

	/**
	 * Logs the given {@code message} with the {@link Severity#INFO} level.
	 * 
	 * @param message The message to log.
	 */
	public void log(CharSequence message);

	/**
	 * Logs the given {@code exception}'s with the {@link Severity#ERROR} level.
	 * <p>
	 * Implementers may log the entire exception's stack trace.
	 * 
	 * @param exception The exception to log.
	 */
	public void log(Exception exception);
}
