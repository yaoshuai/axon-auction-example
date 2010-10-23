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
package org.fuin.auction.command.server.events;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.axonframework.domain.DomainEvent;
import org.fuin.auction.command.server.support.ExtendedDomainEvent;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.PasswordSha512;
import org.fuin.objects4j.UserId;

/**
 * A user was created.
 */
public final class UserCreatedEvent extends DomainEvent implements ExtendedDomainEvent {

	private static final long serialVersionUID = 8303845111764138148L;

	private static final int VERSION = 1;

	private final UserId userId;

	private final PasswordSha512 password;

	private final EmailAddress email;

	private final String securityToken;

	/**
	 * Constructor with all data.
	 * 
	 * @param userId
	 *            User id.
	 * @param password
	 *            Hashed password.
	 * @param email
	 *            Email.
	 * @param securityToken
	 *            Base64 encoded security token.
	 */
	public UserCreatedEvent(final UserId userId, final PasswordSha512 password,
	        final EmailAddress email, final String securityToken) {
		super();
		this.userId = userId;
		this.password = password;
		this.email = email;
		this.securityToken = securityToken;
	}

	/**
	 * Returns the user id.
	 * 
	 * @return User id.
	 */
	public final UserId getUserId() {
		return userId;
	}

	/**
	 * Returns the hashed password.
	 * 
	 * @return SHA-512 password hash.
	 */
	public final PasswordSha512 getPassword() {
		return password;
	}

	/**
	 * Returns the email.
	 * 
	 * @return Email address.
	 */
	public final EmailAddress getEmail() {
		return email;
	}

	/**
	 * Returns the security token of the user.
	 * 
	 * @return Base64 encoded security token.
	 */
	public final String getSecurityToken() {
		return securityToken;
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("userId", userId).append("email", email).append(
		        "securityToken", securityToken).append("version", getVersion()).toString();
	}

	@Override
	public final int getVersion() {
		return VERSION;
	}

}
