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
package org.fuin.auction.command.server.base;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.fuin.auction.command.api.exceptions.EmailAlreadyExistException;
import org.fuin.auction.command.api.exceptions.UserIdAlreadyExistException;
import org.fuin.auction.command.api.exceptions.UserIdEmailCombinationAlreadyExistException;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.UserId;

/**
 * In memory constraint service - Just a dummy implementation.
 */
@Named
public final class ConstraintSetInMemory implements ConstraintSet {

	private Map<UserId, EmailAddress> map = new HashMap<UserId, EmailAddress>();

	@Override
	public final void add(final UserId userId, final EmailAddress email)
	        throws UserIdEmailCombinationAlreadyExistException, UserIdAlreadyExistException,
	        EmailAlreadyExistException {

		final EmailAddress value = map.get(userId);
		if (value == null) {
			if (map.containsValue(email)) {
				throw new EmailAlreadyExistException("The email '" + email
				        + "' is already in use by another user account!");
			}
			map.put(userId, email);
		} else {
			if (value.equals(email)) {
				throw new UserIdEmailCombinationAlreadyExistException(
				        "The combination of user id '" + userId + "' and email '" + email
				                + "' already exists!");
			}
			throw new UserIdAlreadyExistException("The user id '" + userId + "' already exists!");
		}

	}

	@Override
	public final void remove(final UserId userId, final EmailAddress email) {
		map.remove(userId);
	}

}
