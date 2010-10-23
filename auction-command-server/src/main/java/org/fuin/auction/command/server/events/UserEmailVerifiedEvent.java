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

/**
 * A user was activated.
 */
public final class UserEmailVerifiedEvent extends DomainEvent implements ExtendedDomainEvent {

	private static final long serialVersionUID = -3180886093274054695L;

	private static final int VERSION = 1;

	/**
	 * Default constructor.
	 */
	public UserEmailVerifiedEvent() {
		super();
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("version", getVersion()).toString();
	}

	@Override
	public final int getVersion() {
		return VERSION;
	}

}
