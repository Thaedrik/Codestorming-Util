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

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Utility class for arrays.
 * 
 * @author Thaedrik [thaedrik@codestorming.org]
 * @see Arrays
 */
public class Arrays2 {

	private static final String END_INDEX_INVALID = "The end index cannot be lower than the begin index.";

	private static void checkIndexes(int... indexes) {
		for (int index : indexes) {
			if (index < 0) {
				throw new ArrayIndexOutOfBoundsException(index);
			}
		}
	}

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
		checkIndexes(begin, end);
		if (begin > array.length - 1 || end > array.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException(END_INDEX_INVALID);
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
		checkIndexes(insertIndex, begin, end);
		if (insertIndex > array.length || begin > insertedValues.length - 1 || end > insertedValues.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException(END_INDEX_INVALID);
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
	public static byte[] insert(byte[] array, int insertIndex, byte[] insertedValues, int begin, int end) {
		checkIndexes(insertIndex, begin, end);
		if (insertIndex > array.length || begin > insertedValues.length - 1 || end > insertedValues.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException(END_INDEX_INVALID);
		}// else
		byte[] newArray;
		if (begin != end) {
			final int newLength = array.length + (end - begin);
			newArray = new byte[newLength];
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
	 *        inserted (between 0 and {@code array.length} included).
	 * @param insertedValues Values to insert.
	 * @param begin Index of the first element of {@code insertedValues} to insert.
	 * @param end Index of the last element of {@code insertedValues} to insert +
	 *        {@code 1}.
	 * @return a new array with the content of the given {@code array} and the inserted
	 *         values.
	 */
	public static short[] insert(short[] array, int insertIndex, byte[] insertedValues, int begin, int end) {
		checkIndexes(insertIndex, begin, end);
		if (insertIndex > array.length || begin > insertedValues.length - 1 || end > insertedValues.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException(END_INDEX_INVALID);
		}// else
		short[] newArray;
		if (begin != end) {
			final int newLength = array.length + (end - begin);
			newArray = new short[newLength];
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
	 *        inserted (between 0 and {@code array.length} included).
	 * @param insertedValues Values to insert.
	 * @param begin Index of the first element of {@code insertedValues} to insert.
	 * @param end Index of the last element of {@code insertedValues} to insert +
	 *        {@code 1}.
	 * @return a new array with the content of the given {@code array} and the inserted
	 *         values.
	 */
	public static int[] insert(int[] array, int insertIndex, int[] insertedValues, int begin, int end) {
		checkIndexes(insertIndex, begin, end);
		if (insertIndex > array.length || begin > insertedValues.length - 1 || end > insertedValues.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException(END_INDEX_INVALID);
		}// else
		int[] newArray;
		if (begin != end) {
			final int newLength = array.length + (end - begin);
			newArray = new int[newLength];
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
	 *        inserted (between 0 and {@code array.length} included).
	 * @param insertedValues Values to insert.
	 * @param begin Index of the first element of {@code insertedValues} to insert.
	 * @param end Index of the last element of {@code insertedValues} to insert +
	 *        {@code 1}.
	 * @return a new array with the content of the given {@code array} and the inserted
	 *         values.
	 */
	public static long[] insert(long[] array, int insertIndex, long[] insertedValues, int begin, int end) {
		checkIndexes(insertIndex, begin, end);
		if (insertIndex > array.length || begin > insertedValues.length - 1 || end > insertedValues.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException(END_INDEX_INVALID);
		}// else
		long[] newArray;
		if (begin != end) {
			final int newLength = array.length + (end - begin);
			newArray = new long[newLength];
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
	 *        inserted (between 0 and {@code array.length} included).
	 * @param insertedValues Values to insert.
	 * @param begin Index of the first element of {@code insertedValues} to insert.
	 * @param end Index of the last element of {@code insertedValues} to insert +
	 *        {@code 1}.
	 * @return a new array with the content of the given {@code array} and the inserted
	 *         values.
	 */
	public static double[] insert(double[] array, int insertIndex, double[] insertedValues, int begin, int end) {
		checkIndexes(insertIndex, begin, end);
		if (insertIndex > array.length || begin > insertedValues.length - 1 || end > insertedValues.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException(END_INDEX_INVALID);
		}// else
		double[] newArray;
		if (begin != end) {
			final int newLength = array.length + (end - begin);
			newArray = new double[newLength];
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
	 *        inserted (between 0 and {@code array.length} included).
	 * @param insertedValues Values to insert.
	 * @param begin Index of the first element of {@code insertedValues} to insert.
	 * @param end Index of the last element of {@code insertedValues} to insert +
	 *        {@code 1}.
	 * @return a new array with the content of the given {@code array} and the inserted
	 *         values.
	 */
	public static float[] insert(float[] array, int insertIndex, float[] insertedValues, int begin, int end) {
		checkIndexes(insertIndex, begin, end);
		if (insertIndex > array.length || begin > insertedValues.length - 1 || end > insertedValues.length) {
			throw new ArrayIndexOutOfBoundsException();
		} else if (end < begin) {
			throw new IllegalArgumentException(END_INDEX_INVALID);
		}// else
		float[] newArray;
		if (begin != end) {
			final int newLength = array.length + (end - begin);
			newArray = new float[newLength];
			System.arraycopy(array, 0, newArray, 0, insertIndex);
			System.arraycopy(insertedValues, begin, newArray, insertIndex, end - begin);
			System.arraycopy(array, insertIndex, newArray, insertIndex + end - begin, array.length - insertIndex);
		} else {
			newArray = array;
		}
		return newArray;
	}

	// Suppressing default constructor, ensuring non-instantiability
	private Arrays2() {}
}
