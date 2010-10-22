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

import javax.inject.Named;

import org.axonframework.eventhandling.FullConcurrencyPolicy;
import org.axonframework.eventhandling.annotation.AsynchronousEventListener;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for sending or receiving mails.
 */
@Named
@AsynchronousEventListener(sequencingPolicyClass = FullConcurrencyPolicy.class)
public class MailManager {

	private static final Logger LOG = LoggerFactory.getLogger(MailManager.class);

	/**
	 * Creates a welcome mail with a unique identifier to verify the email
	 * address.
	 * 
	 * @param event
	 *            Event to handle.
	 */
	@EventHandler
	public final void handleUserCreatedEvent(final UserCreatedEvent event) {

		// TODO michael 21.10.2010 Send a real email

		if (LOG.isDebugEnabled()) {
			LOG.debug("SEND welcome mail to " + event.getEmail() + " [" + event.getSecurityToken()
			        + "]");
		}

	}

}
