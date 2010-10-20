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

import javax.inject.Inject;
import javax.inject.Named;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventhandling.FullConcurrencyPolicy;
import org.axonframework.eventhandling.annotation.AsynchronousEventListener;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.fuin.auction.common.Utils;

/**
 * Responsible for sending or receiving mails.
 */
@Named
@AsynchronousEventListener(sequencingPolicyClass = FullConcurrencyPolicy.class)
public class MailManager {

	@Inject
	private CommandBus commandBus;

	/**
	 * Sets the command bus to a new value.
	 * 
	 * @param commandBus
	 *            Value to set.
	 */
	protected final void setCommandBus(final CommandBus commandBus) {
		this.commandBus = commandBus;
	}

	/**
	 * Creates a welcome mail with a unique identifier.
	 * 
	 * @param event
	 *            A new user was created.
	 */
	@EventHandler
	public final void handleUserCreatedEvent(final UserCreatedEvent event) {

		final String token = Utils.createSecureRandom();

		System.out.println("SEND mail to " + event.getEmail() + " [" + token + "]");

		final PrepareUserVerificationCommand command = new PrepareUserVerificationCommand(event
		        .getAggregateIdentifier(), token);
		commandBus.dispatch(command);

	}
}
