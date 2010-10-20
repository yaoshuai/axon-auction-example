/*
 * Copyright (c) 2010. Axon Auction Example
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuin.auction.common;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

/**
 * Layer independent utilities for the auction application.
 */
public final class Utils {

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
	        'b', 'c', 'd', 'e', 'f' };

	/**
	 * Private constructor to avoid instantiation.
	 */
	private Utils() {
		throw new UnsupportedOperationException("You cannot create an instance of a utility class!");
	}

	/**
	 * Returns the project information relative to the given class.
	 * 
	 * @param clasz
	 *            Class to use for getting the project information resource
	 *            from.
	 * @param propertiesFilename
	 *            Name and path of the properties file.
	 * 
	 * @return Project information.
	 * 
	 * @throws FailedToLoadProjectInfoException
	 *             The project properties resource relative to the given class
	 *             was not found or could not be loaded for other reasons.
	 */
	public static final ProjectInfo getProjectInfo(final Class<?> clasz,
	        final String propertiesFilename) throws FailedToLoadProjectInfoException {

		try {

			final Properties props = new Properties();
			final InputStream inStream = clasz.getResourceAsStream(propertiesFilename);
			if (inStream == null) {
				throw new FailedToLoadProjectInfoException(clasz, propertiesFilename,
				        new IOException("Resource '" + propertiesFilename + "' not found!"));
			}
			try {
				try {
					props.load(inStream);
				} finally {
					inStream.close();
				}
			} catch (final IOException ex) {
				throw new FailedToLoadProjectInfoException(clasz, propertiesFilename, ex);
			}
			return new ProjectInfo(props);

		} catch (final RuntimeException ex) {
			throw new FailedToLoadProjectInfoException(clasz, propertiesFilename, ex);
		}

	}

	/**
	 * Creates an error message from an exception. Some exceptions (for example
	 * {@link NullPointerException}) don't carry a message. In this case the
	 * simple name of the exception is used for creating the error message.
	 * 
	 * @param ex
	 *            Exception to create an error message for.
	 * 
	 * @return Error message - Always non-<code>null</code> and not empty.
	 */
	public static String createMessage(final Exception ex) {
		final String msg;
		if ((ex.getMessage() == null) || (ex.getMessage().trim().length() == 0)) {
			msg = ex.getClass().getSimpleName();
		} else {
			msg = ex.getMessage();
		}
		return msg;
	}

	/**
	 * Encodes a byte array base64.
	 * 
	 * @param buffer
	 *            Bytes to encode.
	 * 
	 * @return Base64 encoded string.
	 */
	public static String encodeBase64(final byte[] buffer) {
		final int l = buffer.length;
		final char[] out = new char[l << 1];
		int j = 0;
		for (int i = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & buffer[i]) >>> 4];
			out[j++] = DIGITS[0x0F & buffer[i]];
		}
		return String.copyValueOf(out);
	}

	/**
	 * Creates a encoded secure random string.
	 * 
	 * @return Base64 encoded string.
	 */
	public static String createSecureRandom() {
		try {
			final SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(System.currentTimeMillis());
			final String no = "" + secureRandom.nextInt();
			final MessageDigest md = MessageDigest.getInstance("SHA-1");
			final byte[] digest = md.digest(no.getBytes());
			return encodeBase64(digest);
		} catch (final NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}

}
