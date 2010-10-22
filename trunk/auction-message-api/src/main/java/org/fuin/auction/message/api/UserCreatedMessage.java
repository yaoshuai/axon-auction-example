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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.objects4j.validation.EmailAddressStr;
import org.fuin.objects4j.validation.UUIDStr;
import org.fuin.objects4j.validation.UserIdStr;

/**
 * A new user was created.
 */
public final class UserCreatedMessage implements AuctionMessage {

	private static final long serialVersionUID = -6123698701339149529L;

	private static final int VERSION = 1;

	@NotNull
	@UUIDStr
	private String userAggregateId;

	@NotNull
	@UserIdStr
	private String userId;

	@NotNull
	@EmailAddressStr
	private String email;

	/**
	 * Default constructor.
	 */
	public UserCreatedMessage() {
		super();
	}

	/**
	 * Constructor with all attributes.
	 * 
	 * @param userAggregateId
	 *            User's aggregate id
	 * @param userId
	 *            User id.
	 * @param email
	 *            Email address.
	 */
	public UserCreatedMessage(final String userAggregateId, final String userId, final String email) {
		super();
		this.userAggregateId = userAggregateId;
		this.userId = userId;
		this.email = email;
	}

	@Override
	public final int getVersion() {
		return VERSION;
	}

	/**
	 * Returns the user's aggregate id.
	 * 
	 * @return Unique id.
	 */
	public final String getUserAggregateId() {
		return userAggregateId;
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
	 * Returns the email address.
	 * 
	 * @return Email.
	 */
	public final String getEmail() {
		return email;
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("userAggregateId", userAggregateId).append(
		        "userId", userId).append("email", email).append("version", getVersion()).toString();
	}

}
