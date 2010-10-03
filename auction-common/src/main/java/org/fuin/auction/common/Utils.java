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
import java.util.Properties;

/**
 * Layer independent utilities for the auction application.
 */
public final class Utils {

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
	 * 
	 * @pre clasz != null
	 * @pre propertiesFilename != null
	 */
	public static final ProjectInfo getProjectInfo(final Class<?> clasz,
	        final String propertiesFilename) throws FailedToLoadProjectInfoException {

		Contract.requireParamNotNull("clasz", clasz);
		Contract.requireParamNotNull("propertiesFilename", propertiesFilename);

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

}
