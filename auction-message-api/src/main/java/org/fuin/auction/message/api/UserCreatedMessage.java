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
package org.fuin.auction.message.api;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.objects4j.Contract;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.PasswordSha512;
import org.fuin.objects4j.UserName;

/**
 * A new user was created.
 */
public final class UserCreatedMessage implements AuctionMessage {

	private static final long serialVersionUID = -6123698701339149529L;

	private static final int VERSION = 1;

	/** Version to be serialized. */
	private int version;

	@NotNull
	private final UUID userAggregateId;

	@NotNull
	private final UserName userName;

	@NotNull
	private final EmailAddress email;

	@NotNull
	private final PasswordSha512 password;

	/**
	 * Constructor with all attributes.
	 * 
	 * @param userAggregateId
	 *            User's aggregate id
	 * @param userName
	 *            User's name.
	 * @param email
	 *            Email address.
	 * @param password
	 *            Password.
	 */
	public UserCreatedMessage(final UUID userAggregateId, final UserName userName,
	        final EmailAddress email, final PasswordSha512 password) {
		super();
		this.version = VERSION;
		this.userAggregateId = userAggregateId;
		this.userName = userName;
		this.email = email;
		this.password = password;
		Contract.requireValid(this);
	}

	@Override
	public final int getVersion() {
		return version;
	}

	/**
	 * Returns the user's aggregate id.
	 * 
	 * @return Unique id.
	 */
	public final UUID getUserAggregateId() {
		return userAggregateId;
	}

	/**
	 * Returns the user id.
	 * 
	 * @return User id.
	 */
	public final UserName getUserName() {
		return userName;
	}

	/**
	 * Returns the email address.
	 * 
	 * @return Email.
	 */
	public final EmailAddress getEmail() {
		return email;
	}

	/**
	 * Returns the hashed password.
	 * 
	 * @return SHA-512 password hash.
	 */
	public final PasswordSha512 getPassword() {
		return password;
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("userAggregateId", userAggregateId).append(
		        "userName", userName).append("email", email).append("password", password).append(
		        "version", getVersion()).toString();
	}

}
