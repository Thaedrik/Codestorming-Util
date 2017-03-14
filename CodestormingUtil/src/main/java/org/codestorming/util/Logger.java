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
	enum Severity {
		/**
		 * Information log level.
		 */
		INFO(1),

		/**
		 * Warning log level.
		 */
		WARNING(1 << 1),

		/**
		 * Error log level.
		 */
		ERROR(1 << 2),

		/**
		 * Debug log level.
		 *
		 * @since 1.4
		 */
		DEBUG(1 << 3);

		private int code;

		private Severity(int code) {
			this.code = code;
		}

		/**
		 * Returns this {@code Logger.Severity}'s code.
		 * 
		 * @return this {@code Logger.Severity}'s code.
		 */
		public int getCode() {
			return code;
		}
	}

	/**
	 * Set the {@link Severity severities} of the messages that can be logged. <br>
	 * If no {@link Severity} is specified, no message will be logged.
	 * 
	 * @param severities The {@link Severity severities} to allow.
	 */
	void filter(Severity... severities);

	/**
	 * Logs the given {@code message} with the specified {@code severity}.
	 * 
	 * @param severity The {@link Severity} level.
	 * @param message The message to log.
	 */
	void log(Severity severity, CharSequence message);

	/**
	 * Logs the given {@code message} with the {@link Severity#INFO} level.
	 * 
	 * @param message The message to log.
	 */
	void log(CharSequence message);

	/**
	 * Logs the given {@code exception}'s with the {@link Severity#ERROR} level.
	 * <p>
	 * Implementers may log the entire exception's stack trace.
	 * 
	 * @param exception The exception to log.
	 */
	void log(Exception exception);

	/**
	 * Indicates if the specified {@link Severity} is currently logable.
	 *
	 * @param severity The {@link Severity} to check.
	 * @return {@code true} if the specified {@link Severity} is currently logable;{@code false} otherwise.
	 * @since 1.4
	 */
	boolean isLogable(Severity severity);
}
