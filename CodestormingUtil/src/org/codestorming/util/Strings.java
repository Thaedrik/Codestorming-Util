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
 * Utility class for String operations.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class Strings {

	// Suppressing default constructor, ensuring non-instantiability
	private Strings() {};

	/**
	 * Remove all <strong>trim</strong> strings at the beginning and at the end of the
	 * given String <em>str</em>.
	 * <p>
	 * If {@code trim} is empty or if there is no {@code trim} string in {@code str},
	 * nothing happen.
	 * 
	 * @param str The string to trim.
	 * @param trim The string to remove from {@code str}.
	 * @return the trimmed string.
	 * @throws NullPointerException if {@code str} or {@code trim} are {@code null}.
	 */
	public static String trim(String str, String trim) {
		if (str == null || trim == null) {
			throw new NullPointerException();
		}// else
		final int trimLength = trim.length();
		if (trimLength > 0) {
			int start = 0;

			// Removing the trim string at the beginning
			while (str.startsWith(trim, start)) {
				start += trimLength;
			}
			str = str.substring(start);

			int count = str.length();
			// Removing the trim string at the end
			while (endsWith(str, trim, count)) {
				count -= trimLength;
			}
			str = str.substring(0, count);
		}
		return str;
	}

	/**
	 * Tests if the {@code str} string ends with the specified suffix at the given
	 * {@code length}.
	 * <p>
	 * Equivalent to do <code>str.substring(0, length).endsWith(suffix);</code>
	 * 
	 * @param str The string to test.
	 * @param suffix the suffix.
	 * @param length The length indicating the end of {@code str}.
	 * @return {@code true} if the {@code str} string ends with the specified suffix at
	 *         the given {@code length};<br>
	 *         {@code false} otherwise.
	 * @throws NullPointerException if {@code str} or {@code suffix} are {@code null}.
	 * @throws IndexOutOfBoundsException if {@code length} is larger than the length of
	 *         the {@code str} string.
	 */
	public static boolean endsWith(String str, String suffix, int length) {
		if (str == null || suffix == null) {
			throw new NullPointerException();
		}// else
		return str.substring(0, length).endsWith(suffix);
	}
}
