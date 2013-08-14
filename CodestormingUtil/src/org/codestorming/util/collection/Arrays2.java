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
package org.codestorming.util.collection;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Utility class for arrays.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 * @see Arrays
 */
public class Arrays2 {

	// Suppressing default constructor, ensuring non-instantiability
	private Arrays2() {}

	/**
	 * Returns a new array built with the given one for which the elements between
	 * {@code begin} (included) and {@code end} (excluded) have been removed.
	 * 
	 * @param array The source array.
	 * @param begin The index of the first element to remove.
	 * @param end The index of the last element to remove plus {@code 1}.
	 * @return a new array built with the given one for which the elements between
	 *         {@code begin} (included) and {@code end} (excluded) have been removed.
	 * @throws ArrayIndexOutOfBoundsException if {@code begin < 0} or
	 *         {@code begin > array.length - 1} or {@code end < 0} or
	 *         {@code end > array.length}.
	 * @throws IllegalArgumentException if {@code end < begin}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] remove(T[] array, int begin, int end) {
		if (begin < 0 || end < 0 || begin > array.length - 1 || end > array.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException("The end index cannot be lower than the begin index.");
		}// else
		T[] newArray;
		if (begin != end) {
			final int newLength = array.length - (end - begin);
			newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), newLength);
			System.arraycopy(array, 0, newArray, 0, begin);
			System.arraycopy(array, end, newArray, begin, array.length - end);
		} else {
			newArray = array;
		}
		return newArray;
	}

	/**
	 * Returns a new array built with the given one for which the elements between
	 * {@code begin} (included) and {@code end} (excluded) have been removed.
	 * 
	 * @param array The source array.
	 * @param removeIndex The index of the element to remove.
	 * @return a new array built with the given one for which the elements between
	 *         {@code begin} (included) and {@code end} (excluded) have been removed.
	 * @throws ArrayIndexOutOfBoundsException if {@code removeIndex < 0} or
	 *         {@code removeIndex > array.length - 1}.
	 */
	public static <T> T[] remove(T[] array, int removeIndex) {
		return remove(array, removeIndex, removeIndex + 1);
	}

	/**
	 * Insert values into the given {@code array} at the specified {@code insertIndex}.
	 * 
	 * @param array The source array.
	 * @param insertIndex Index of the given {@code array} into which the values will be
	 *        inserted (between 0 and {@code array.length} included).
	 * @param insertedValues Values to insert.
	 * @param begin Index of the first element of {@code insertedValues} to insert.
	 * @param end Index of the last element of {@code insertedValues} to insert +
	 *        {@code 1}.
	 * @return a new array with the content of the given {@code array} and the inserted
	 *         values.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(T[] array, int insertIndex, T[] insertedValues, int begin, int end) {
		if (insertIndex < 0 || begin < 0 || end < 0 || insertIndex > array.length || begin > insertedValues.length - 1
				|| end > insertedValues.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException("The end index cannot be lower than the begin index.");
		}// else
		T[] newArray;
		if (begin != end) {
			final int newLength = array.length + (end - begin);
			newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), newLength);
			System.arraycopy(array, 0, newArray, 0, insertIndex);
			System.arraycopy(insertedValues, begin, newArray, insertIndex, end - begin);
			System.arraycopy(array, insertIndex, newArray, insertIndex + end - begin, array.length - insertIndex);
		} else {
			newArray = array;
		}
		return newArray;
	}

	/**
	 * Insert values into the given {@code array} at the specified {@code insertIndex}.
	 * 
	 * @param array The source array.
	 * @param insertIndex Index of the given {@code array} into which the values will be
	 *        inserted.
	 * @param value Value to insert.
	 * @return a new array with the content of the given {@code array} and the inserted
	 *         values.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(T[] array, int insertIndex, T value) {
		T[] valueArray = (T[]) Array.newInstance(array.getClass().getComponentType(), 1);
		valueArray[0] = value;
		return insert(array, insertIndex, valueArray, 0, 1);
	}
}
