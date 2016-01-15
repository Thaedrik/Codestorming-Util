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

import java.io.PrintStream;

import org.codestorming.util.Logger.Severity;

/**
 * A basic {@link Logger} that writes on the standard print streams.
 * <p>
 * Information and warning messages are written on the <em>standard output</em>. <br>
 * Error messages are written on the <em>standard error output</em>.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class BasicLogger implements Logger {

	protected volatile int filter = Severity.ERROR.getCode() | Severity.WARNING.getCode();

	/**
	 * {@inheritDoc}
	 * <p>
	 * By default, only errors and warnings are logged.
	 */
	@Override
	public void filter(Severity... severities) {
		int severityFilter = 0;
		for (Severity severity : severities) {
			severityFilter |= severity.getCode();
		}
		filter = severityFilter;
	}

	@Override
	public void log(Severity severity, CharSequence message) {
		if ((severity.getCode() & filter) != 0) {
			PrintStream output;
			String prefix = "";
			if (severity == Severity.INFO) {
				output = System.out;
			} else if (severity == Severity.WARNING) {
				output = System.out;
				prefix = "[WARNING] ";
			} else {
				output = System.err;
				prefix = "[ERROR] ";
			}
			output.print(prefix);
			output.println(message);
		}
	}

	@Override
	public void log(CharSequence message) {
		log(Severity.INFO, message);
	}

	/**
	 * Logs the given {@code exception}'s <strong>stack trace</strong> with the
	 * {@link Severity#ERROR} level.
	 * 
	 * @param exception The exception to log.
	 */
	@Override
	public void log(Exception exception) {
		log(Severity.ERROR, createExceptionMessage(exception));
	}

	/**
	 * Creates a message from the given {@code exception}'s stack trace.
	 * 
	 * @param exception The exception to create a message for.
	 * @return the created message.
	 */
	protected StringBuilder createExceptionMessage(Exception exception) {
		StringBuilder builder = new StringBuilder();
		return throwableMessage(builder, exception);
	}

	/**
	 * Appends to the specified {@code builder}, the message from the given
	 * {@code throwable}'s stack trace.
	 * 
	 * @param throwable The throwable to create the message for.
	 * @return the given builder.
	 */
	protected StringBuilder throwableMessage(StringBuilder builder, Throwable throwable) {
		builder.append(throwable.toString());
		for (StackTraceElement element : throwable.getStackTrace()) {
			builder.append("\n\tat ").append(element.toString());
		}
		if (throwable.getCause() != null) {
			builder.append("\nCaused by : ");
			throwableMessage(builder, throwable.getCause());
		}
		return builder;
	}

}
