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

import java.util.HashMap;
import java.util.Map;

import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.UserId;
import org.springframework.stereotype.Component;

/**
 * In memory constraint service - Just a dummy implementation.
 */
@Component
public final class ConstraintServiceInMemory implements ConstraintService {

	private Map<UserId, EmailAddress> map = new HashMap<UserId, EmailAddress>();

	@Override
	public final void add(final UserId userId, final EmailAddress email)
	        throws UserIdEmailCombinationAlreadyExistException, UserIdAlreadyExistException,
	        EmailAlreadyExistException {

		final EmailAddress value = map.get(userId);
		if (value == null) {
			if (map.containsValue(email)) {
				throw new EmailAlreadyExistException(email);
			}
			map.put(userId, email);
		} else {
			if (value.equals(email)) {
				throw new UserIdEmailCombinationAlreadyExistException(userId, email);
			}
			throw new UserIdAlreadyExistException(userId);
		}

	}

	@Override
	public final void remove(final UserId userId, final EmailAddress email) {
		map.remove(userId);
	}

}
