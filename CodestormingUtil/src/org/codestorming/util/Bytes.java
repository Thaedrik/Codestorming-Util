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
 * Utility class for bytes.
 * 
 * @author Thaedrik <thaedrik@gmail.com>
 */
public class Bytes {

	// Suppressing default constructor, ensuring non-instantiability
	private Bytes() {}

	/**
	 * Create an array of four bytes corresponding to the given integer in big-endian.
	 * 
	 * @param integer The integer for which to create a byte array.
	 * @return an array of four bytes corresponding to the given integer in big-endian.
	 */
	public static byte[] intToByteArray(int integer) {
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte) ((integer >>> 24) & 0xFF);
		byteArray[1] = (byte) ((integer >>> 16) & 0xFF);
		byteArray[2] = (byte) ((integer >>> 8) & 0xFF);
		byteArray[3] = (byte) (integer & 0xFF);
		return byteArray;
	}

	/**
	 * Returns the integer corresponding to the given byte array.
	 * <p>
	 * The given array must be an array of four bytes and in big-endian.
	 * 
	 * @param byteArray The byte array for which to have the corresponding integer.
	 * @return the integer corresponding to the given byte array.
	 */
	public static int byteArrayToInt(byte[] byteArray) {
		if (byteArray.length != 4) {
			throw new IllegalArgumentException("The array's length is incorrect : " + byteArray.length);
		}// else
		int integer = 0;
		integer |= (0xFF & byteArray[0]) << 24;
		integer |= (0xFF & byteArray[1]) << 16;
		integer |= (0xFF & byteArray[2]) << 8;
		integer |= (0xFF & byteArray[3]);
		return integer;
	}

	/**
	 * Create an array of eight bytes corresponding to the given long integer in
	 * big-endian.
	 * 
	 * @param longInt The long integer for which to create a byte array.
	 * @return an array of eight bytes corresponding to the given long integer in
	 *         big-endian.
	 */
	public static byte[] longToByteArray(long longInt) {
		byte[] byteArray = new byte[8];
		byteArray[0] = (byte) ((longInt >>> 56) & 0xFF);
		byteArray[1] = (byte) ((longInt >>> 48) & 0xFF);
		byteArray[2] = (byte) ((longInt >>> 40) & 0xFF);
		byteArray[3] = (byte) ((longInt >>> 32) & 0xFF);
		byteArray[4] = (byte) ((longInt >>> 24) & 0xFF);
		byteArray[5] = (byte) ((longInt >>> 16) & 0xFF);
		byteArray[6] = (byte) ((longInt >>> 8) & 0xFF);
		byteArray[7] = (byte) (longInt & 0xFF);
		return byteArray;
	}

	/**
	 * Returns the long integer corresponding to the given byte array.
	 * <p>
	 * The given array must be an array of eight bytes and in big-endian.
	 * 
	 * @param byteArray The byte array for which to have the corresponding long integer.
	 * @return the long integer corresponding to the given byte array.
	 */
	public static long byteArrayToLong(byte[] byteArray) {
		if (byteArray.length != 8) {
			throw new IllegalArgumentException("The array's length is incorrect : " + byteArray.length);
		}// else
		long longInt = 0;
		longInt |= (long) (0xFF & byteArray[0]) << 56;
		longInt |= (long) (0xFF & byteArray[1]) << 48;
		longInt |= (long) (0xFF & byteArray[2]) << 40;
		longInt |= (long) (0xFF & byteArray[3]) << 32;
		longInt |= (long) (0xFF & byteArray[4]) << 24;
		longInt |= (long) (0xFF & byteArray[5]) << 16;
		longInt |= (long) (0xFF & byteArray[6]) << 8;
		longInt |= (long) (0xFF & byteArray[7]);
		return longInt;
	}
}
