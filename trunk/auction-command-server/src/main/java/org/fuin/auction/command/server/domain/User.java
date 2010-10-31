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
package org.fuin.auction.command.server.domain;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.fuin.auction.command.api.exceptions.PasswordException;
import org.fuin.auction.command.api.exceptions.UserEmailVerificationFailedException;
import org.fuin.auction.command.server.events.UserCreatedEvent;
import org.fuin.auction.command.server.events.UserEmailVerifiedEvent;
import org.fuin.auction.command.server.events.UserPasswordChangedEvent;
import org.fuin.auction.common.UserState;
import org.fuin.auction.common.Utils;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.Password;
import org.fuin.objects4j.PasswordSha512;
import org.fuin.objects4j.UserName;

/**
 * Represents a user in the auction system.
 */
public final class User extends AbstractAnnotatedAggregateRoot {

	private UserState userState = UserState.NEW;

	private PasswordSha512 password;

	private String verificationToken;

	/**
	 * Constructor with id that fires NO EVENT.
	 * 
	 * @param identifier
	 *            Unique aggregate root id.
	 */
	public User(final AggregateIdentifier identifier) {
		super(identifier);
	}

	/**
	 * Constructor that fires a {@link UserCreatedEvent}.
	 * 
	 * @param identifier
	 *            New id previously generated.
	 * @param userName
	 *            Human readable unique name of the user.
	 * @param password
	 *            User password.
	 * @param email
	 *            Password.
	 */
	public User(final AggregateIdentifier identifier, final UserName userName,
	        final Password password, final EmailAddress email) {
		super(identifier);
		apply(new UserCreatedEvent(userName, new PasswordSha512(password), email, Utils
		        .createSecureRandom()));
	}

	/**
	 * Changes the password and fires a {@link UserPasswordChangedEvent}.
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

		final PasswordSha512 oldPassword = new PasswordSha512(oldPw);
		if (!password.equals(oldPassword)) {
			throw new PasswordException("The old password is wrong!");
		}

		apply(new UserPasswordChangedEvent(new PasswordSha512(oldPw), new PasswordSha512(newPw)));

	}

	/**
	 * Verify the user by checking the verification token.
	 * 
	 * @param token
	 *            Token to compare with the internal token.
	 * 
	 * @throws IllegalUserStateException
	 *             The state was not {@link UserState#NEW} or
	 *             {@link UserState#RESET}.
	 * @throws UserEmailVerificationFailedException
	 *             The given token was not equal to the user's verification
	 *             token.
	 */
	public final void verifyEmail(final String token) throws IllegalUserStateException,
	        UserEmailVerificationFailedException {

		if (!(userState.equals(UserState.NEW) || userState.equals(UserState.RESET))) {
			throw new IllegalUserStateException(userState, UserState.NEW, UserState.RESET);
		}
		if (!verificationToken.equals(token)) {
			throw new UserEmailVerificationFailedException();
		}

		apply(new UserEmailVerifiedEvent());

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
		this.verificationToken = event.getSecurityToken();
	}

	/**
	 * Handles the password change event without checking any constraints.
	 * 
	 * @param event
	 *            Event.
	 */
	@EventHandler
	protected final void handlePasswordChangedEvent(final UserPasswordChangedEvent event) {
		this.password = event.getNewPassword();
	}

	/**
	 * Handles user verified event without checking any constraints.
	 * 
	 * @param event
	 *            Event.
	 */
	@EventHandler
	protected final void handleUserVerifiedEvent(final UserEmailVerifiedEvent event) {
		this.userState = UserState.ACTIVE;
	}

}
