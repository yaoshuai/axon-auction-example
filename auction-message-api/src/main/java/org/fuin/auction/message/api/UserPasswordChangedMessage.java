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
import org.fuin.objects4j.validation.UUIDStr;

/**
 * A user changed the password.
 */
public final class UserPasswordChangedMessage implements AuctionMessage {

	private static final long serialVersionUID = -4988175758627577689L;

	private static final int VERSION = 1;

	@NotNull
	@UUIDStr
	private String userAggregateId;

	/**
	 * Default constructor.
	 */
	public UserPasswordChangedMessage() {
		super();
	}

	/**
	 * Constructor with all attributes.
	 * 
	 * @param userAggregateId
	 *            User's aggregate id
	 */
	public UserPasswordChangedMessage(final String userAggregateId) {
		super();
		this.userAggregateId = userAggregateId;
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

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("userAggregateId", userAggregateId).append(
		        "version", getVersion()).toString();
	}

}
