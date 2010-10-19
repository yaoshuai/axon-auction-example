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

import java.util.UUID;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.Password;
import org.fuin.objects4j.PasswordSha512;
import org.fuin.objects4j.UserId;

/**
 * Represents a user in the auction system.
 */
public final class User extends AbstractAnnotatedAggregateRoot {

	private PasswordSha512 password;

	/**
	 * Constructor with id that fires NO EVENT.
	 * 
	 * @param identifier
	 *            Unique aggregate root id.
	 */
	public User(final UUID identifier) {
		super(identifier);
	}

	/**
	 * Constructor that fires a {@link UserCreatedEvent}.
	 * 
	 * @param userId
	 *            Human readable unique name of the user.
	 * @param password
	 *            User password.
	 * @param email
	 *            Password.
	 */
	public User(final UserId userId, final Password password, final EmailAddress email) {
		super(UUID.randomUUID());
		apply(new UserCreatedEvent(userId, new PasswordSha512(password), email));
	}

	/**
	 * Changes the password and fires a {@link PasswordChangedEvent}.
	 * 
	 * @param oldPw
	 *            Old clear text password.
	 * @param newPw
	 *            New clear text password.
	 * 
	 * @throws PasswordException
	 *             The old password is not equal to the stored password.
	 */
	public final void changePassword(final Password oldPw, final Password newPw)
	        throws PasswordException {

		if (!password.equals(oldPw)) {
			throw new PasswordException("The old password is wrong!");
		}

		apply(new PasswordChangedEvent(new PasswordSha512(oldPw), new PasswordSha512(newPw)));

	}

	/**
	 * Handles the creation event without checking any constraints.
	 * 
	 * @param event
	 *            Event.
	 */
	@EventHandler
	protected final void handleUserCreatedEvent(final UserCreatedEvent event) {
		this.password = event.getPassword();
	}

	/**
	 * Handles the password change event without checking any constraints.
	 * 
	 * @param event
	 *            Event.
	 */
	@EventHandler
	protected final void handlePasswordChangedEvent(final PasswordChangedEvent event) {
		this.password = event.getNewPassword();
	}

}
