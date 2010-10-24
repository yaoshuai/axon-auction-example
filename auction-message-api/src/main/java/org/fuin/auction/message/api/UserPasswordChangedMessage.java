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
import org.fuin.objects4j.PasswordSha512;

/**
 * A user changed the password.
 */
public final class UserPasswordChangedMessage implements AuctionMessage {

	private static final long serialVersionUID = -4988175758627577689L;

	private static final int VERSION = 1;

	/** Version to be serialized. */
	private int instanceVersion;

	@NotNull
	private final UUID userAggregateId;

	@NotNull
	private final PasswordSha512 password;

	/**
	 * Constructor with all attributes.
	 * 
	 * @param userAggregateId
	 *            User's aggregate id
	 * @param password
	 *            The new password.
	 */
	public UserPasswordChangedMessage(final UUID userAggregateId, final PasswordSha512 password) {
		super();
		this.instanceVersion = VERSION;
		this.userAggregateId = userAggregateId;
		this.password = password;
		Contract.requireValid(this);
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
		        "password", password).append("version", getInstanceVersion()).toString();
	}

	@Override
	public final int getInstanceVersion() {
		return instanceVersion;
	}

	@Override
	public final int getClassVersion() {
		return VERSION;
	}

	@Override
	public final boolean isSameVersion() {
		return VERSION == instanceVersion;
	}

}
