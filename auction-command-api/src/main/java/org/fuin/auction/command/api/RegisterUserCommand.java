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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.objects4j.validation.EmailAddressStr;
import org.fuin.objects4j.validation.PasswordStr;
import org.fuin.objects4j.validation.UserIdStr;

/**
 * Register a new user.
 */
public final class RegisterUserCommand implements Command {

	private static final long serialVersionUID = 5381295115581408651L;

	private static final int VERSION = 1;

	@NotNull
	@UserIdStr
	private String userId;

	@NotNull
	@PasswordStr
	private String password;

	@NotNull
	@EmailAddressStr
	private String email;

	/**
	 * Default constructor.
	 */
	public RegisterUserCommand() {
		super();
	}

	/**
	 * Constructor with all attributes.
	 * 
	 * @param userId
	 *            User ID.
	 * @param password
	 *            Password.
	 * @param email
	 *            Email address.
	 */
	public RegisterUserCommand(final String userId, final String password, final String email) {
		super();
		this.userId = userId;
		this.password = password;
		this.email = email;
	}

	@Override
	public final int getVersion() {
		return VERSION;
	}

	/**
	 * Returns the user id.
	 * 
	 * @return User id.
	 */
	public final String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id to a new value.
	 * 
	 * @param userId
	 *            User id to set.
	 */
	public final void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * Returns the password.
	 * 
	 * @return Password.
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * Sets the password to a new value.
	 * 
	 * @param password
	 *            Password to set.
	 */
	public final void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Returns the email address.
	 * 
	 * @return Email address.
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * Sets the email address to a new value.
	 * 
	 * @param email
	 *            Email address to set.
	 */
	public final void setEmail(final String email) {
		this.email = email;
	}

	@Override
	public final String toTraceString() {
		// We don't want to include the clear text password for security
		// reasons here
		return new ToStringBuilder(this).append("userId", userId).append("email", email).append(
		        "version", getVersion()).toString();
	}

}
