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
package org.fuin.auction.command.server;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.axonframework.domain.AggregateIdentifier;
import org.fuin.auction.command.api.Command;

/**
 * Prepare the user for receiving an email verification command. This is an
 * internal command only used within the command server.
 */
final class PrepareForUserEmailVerificationCommand implements Command {

	private static final long serialVersionUID = -3776153476455175302L;

	private static final int VERSION = 1;

	private final AggregateIdentifier aggregateIdentifier;

	private final String token;

	/**
	 * Constructor with user aggregate identifier and security token.
	 * 
	 * @param aggregateIdentifier
	 *            Unique id.
	 * @param token
	 *            Base64 encoded security token.
	 */
	public PrepareForUserEmailVerificationCommand(final AggregateIdentifier aggregateIdentifier,
	        final String token) {
		this.aggregateIdentifier = aggregateIdentifier;
		this.token = token;
	}

	@Override
	public final int getVersion() {
		return VERSION;
	}

	/**
	 * Returns the aggregate identifier of the user.
	 * 
	 * @return Unique id.
	 */
	public final AggregateIdentifier getAggregateIdentifier() {
		return aggregateIdentifier;
	}

	/**
	 * Returns the security token of the user.
	 * 
	 * @return Base64 encoded security token.
	 */
	public final String getToken() {
		return token;
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("aggregateIdentifier", aggregateIdentifier).append(
		        "token", token).append("version", getVersion()).toString();
	}

}
