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
import org.axonframework.domain.DomainEvent;

/**
 * A user was created.
 */
public final class UserEmailVerificationPreparedEvent extends DomainEvent implements
        ExtendedDomainEvent {

	private static final long serialVersionUID = 2953196036121000060L;

	private static final int VERSION = 1;

	private final String token;

	/**
	 * Constructor with security token.
	 * 
	 * @param token
	 *            Base64 encoded security token.
	 */
	public UserEmailVerificationPreparedEvent(final String token) {
		super();
		this.token = token;
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
		return new ToStringBuilder(this).append("token", token).append("version", getVersion())
		        .toString();
	}

	@Override
	public final int getVersion() {
		return VERSION;
	}

}
