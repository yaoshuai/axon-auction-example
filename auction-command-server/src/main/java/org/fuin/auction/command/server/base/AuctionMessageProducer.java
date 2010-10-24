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

import java.util.UUID;

import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.axonframework.eventhandling.FullConcurrencyPolicy;
import org.axonframework.eventhandling.annotation.AsynchronousEventListener;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.fuin.auction.command.server.events.UserCreatedEventV1;
import org.fuin.auction.command.server.events.UserEmailVerifiedEventV1;
import org.fuin.auction.command.server.events.UserPasswordChangedEventV1;
import org.fuin.auction.message.api.AuctionMessage;
import org.fuin.auction.message.api.UserCreatedMessageV1;
import org.fuin.auction.message.api.UserEmailVerfiedMessageV1;
import org.fuin.auction.message.api.UserPasswordChangedMessageV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * Sends events as JMS messages.
 */
@Named
@AsynchronousEventListener(sequencingPolicyClass = FullConcurrencyPolicy.class)
public class AuctionMessageProducer {

	private static final Logger LOG = LoggerFactory.getLogger(AuctionMessageProducer.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * Sets the JMS template to a new value.
	 * 
	 * @param jmsTemplate
	 *            Template to set.
	 */
	protected final void setJmsTemplate(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * Publish {@link UserCreatedEventV1} as JMS message.
	 * 
	 * @param event
	 *            Event to publish.
	 */
	@EventHandler
	public final void handleUserCreatedEvent(final UserCreatedEventV1 event) {
		publish(new UserCreatedMessageV1(UUID.fromString(event.getAggregateIdentifier().toString()),
		        event.getUserName(), event.getEmail(), event.getPassword()));
	}

	/**
	 * Publish {@link UserEmailVerifiedEventV1} as JMS message.
	 * 
	 * @param event
	 *            Event to publish.
	 */
	@EventHandler
	public final void handleUserEmailVerifiedEvent(final UserEmailVerifiedEventV1 event) {
		publish(new UserEmailVerfiedMessageV1(UUID.fromString(event.getAggregateIdentifier()
		        .toString())));
	}

	/**
	 * Publish {@link UserPasswordChangedEventV1} as JMS message.
	 * 
	 * @param event
	 *            Event to publish.
	 */
	@EventHandler
	public final void handleUserPasswordChangedEvent(final UserPasswordChangedEventV1 event) {
		publish(new UserPasswordChangedMessageV1(UUID.fromString(event.getAggregateIdentifier()
		        .toString()), event.getNewPassword()));
	}

	/**
	 * Publish a message to JMS.
	 * 
	 * @param message
	 *            Message to publish.
	 */
	private void publish(final AuctionMessage message) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Publish to JMS: " + message.toTraceString());
		}
		jmsTemplate.send(new MessageCreator() {
			@Override
			public final Message createMessage(final Session session) throws JMSException {
				return session.createObjectMessage(message);
			}
		});
	}

}
