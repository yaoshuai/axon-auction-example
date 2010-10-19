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
package org.fuin.auction.command.api;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.objects4j.Contract;
import org.fuin.objects4j.validation.NotEmpty;

/**
 * Result of requesting the server information.
 */
public final class GetServerInfoCommandResult extends
        AbstractCommandResult<GetServerInfoCommandResult> {

	private static final long serialVersionUID = 2137727596212900077L;

	@NotEmpty
	private String name;

	@NotEmpty
	private String version;

	@NotNull
	private Date buildTimestamp;

	/**
	 * Default constructor.
	 */
	public GetServerInfoCommandResult() {
		super();
	}

	/**
	 * Constructor with version.
	 * 
	 * @param name
	 *            Name of the project.
	 * @param version
	 *            Version of the project.
	 * @param buildTimestamp
	 *            Date and time of the build.
	 */
	public GetServerInfoCommandResult(final String name, final String version,
	        final Date buildTimestamp) {
		super(CommandResultType.SUCCESS, 0, "");
		this.name = name;
		this.version = version;
		this.buildTimestamp = buildTimestamp;
		Contract.requireValid(this);
	}

	/**
	 * Returns the project name.
	 * 
	 * @return Name of the project.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sets the project name.
	 * 
	 * @param name
	 *            Name of the project.
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns the version of the project.
	 * 
	 * @return Version.
	 */
	public final String getVersion() {
		return version;
	}

	/**
	 * Sets the version of the project.
	 * 
	 * @param version
	 *            Version.
	 */
	public final void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * Returns the build timestamp.
	 * 
	 * @return Date and time of the build.
	 */
	public final Date getBuildTimestamp() {
		return buildTimestamp;
	}

	/**
	 * Sets the build timestamp.
	 * 
	 * @param buildTimestamp
	 *            Date and time of the build.
	 */
	public final void setBuildTimestamp(final Date buildTimestamp) {
		this.buildTimestamp = buildTimestamp;
	}

	/**
	 * Appends all properties to the builder.
	 * 
	 * @param builder
	 *            Builder to append key/values.
	 */
	protected void append(final ToStringBuilder builder) {
		builder.append("name", name).append("version", version).append("buildTimestamp",
		        buildTimestamp);
	}

	@Override
	public final String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this);
		super.append(builder);
		append(builder);
		return builder.toString();
	}

}
