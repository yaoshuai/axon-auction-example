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

import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.UserId;

/**
 * A user/email combination is already registered.
 */
public final class UserIdEmailCombinationAlreadyExistException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with user id and email.
	 * 
	 * @param userId
	 *            User id.
	 * @param email
	 *            Email address.
	 */
	public UserIdEmailCombinationAlreadyExistException(final UserId userId, final EmailAddress email) {
		super("The combination of user id'" + userId + "' and email '" + email
		        + "' already exists!");
	}

}