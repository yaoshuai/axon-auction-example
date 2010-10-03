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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Information about a project artifact (JAR, WAR).
 */
public final class ProjectInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Key for the name of the project. */
	public static final String PROPERTY_NAME = "name";

	/** Key for the version of the project. */
	public static final String PROPERTY_VERSION = "version";

	/** Key for the build timestamp. */
	public static final String PROPERTY_BUILD_TIMESTAMP = "buildTimestamp";

	/** Format of the build timestamp. */
	public static final String BUILD_TIMESTAMP_FORMAT = "yyyy-MM-dd_HH-mm";

	private final String name;

	private final String version;

	private final Date buildTimestamp;

	/**
	 * Constructor with data to populate the instance with.
	 * 
	 * @param props
	 *            Properties to get the values from.
	 * 
	 * @pre props != null
	 * @pre The properties file must contain all required properties (see public
	 *      'PROPERTY_*' constants).
	 * @pre The build timestamp is required to have the correct format (see
	 *      BUILD_TIMESTAMP_FORMAT constant).
	 */
	public ProjectInfo(final Properties props) {
		super();

		Contract.requireParamNotNull("props", props);

		name = props.getProperty(PROPERTY_NAME);
		Contract.requirePropertyNotNull(PROPERTY_NAME, name);

		version = props.getProperty(PROPERTY_VERSION);
		Contract.requirePropertyNotNull(PROPERTY_VERSION, version);

		final String buildTimestampStr = props.getProperty(PROPERTY_BUILD_TIMESTAMP);
		Contract.requirePropertyNotNull(PROPERTY_BUILD_TIMESTAMP, buildTimestampStr);

		final SimpleDateFormat sdf = new SimpleDateFormat(BUILD_TIMESTAMP_FORMAT);
		try {
			buildTimestamp = sdf.parse(buildTimestampStr);
		} catch (final ParseException ex) {
			throw new IllegalStateException("The value '" + buildTimestampStr
			        + "' of the property '" + name + "' was not in the format '"
			        + BUILD_TIMESTAMP_FORMAT + "'!");
		}

	}

	/**
	 * Returns the project name.
	 * 
	 * @return Name of the project.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the version of the project.
	 * 
	 * @return Version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Returns the build timestamp.
	 * 
	 * @return Date and time of the build.
	 */
	public Date getBuildTimestamp() {
		return buildTimestamp;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).append("version", version).append(
		        "buildTimestamp", buildTimestamp).toString();
	}

}
