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

/**
 * A user was activated.
 */
public final class UserEmailVerfiedMessageV1 implements AuctionMessage {

	private static final long serialVersionUID = -4888511874340301410L;

	@NotNull
	private UUID userAggregateId;

	/**
	 * Default constructor for serialization.
	 */
	protected UserEmailVerfiedMessageV1() {
		super();
	}

	/**
	 * Constructor with all attributes.
	 * 
	 * @param userAggregateId
	 *            User's aggregate id
	 */
	public UserEmailVerfiedMessageV1(final UUID userAggregateId) {
		super();
		this.userAggregateId = userAggregateId;
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

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("userAggregateId", userAggregateId).toString();
	}

}
