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
package org.codestorming.util.io;

import java.io.Closeable;
import java.io.File;

/**
 * Helper providing convenient methods for dealing with {@link File files}.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class FileHelper {

	/**
	 * Closes the given {@link Closeable} without raising exceptions if any.
	 * 
	 * @param closeable The closeable to close (may be {@code null}).
	 */
	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Exception e) {
			// Ignore
		}
	}
}