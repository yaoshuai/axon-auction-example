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

import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.axonframework.eventhandling.FullConcurrencyPolicy;
import org.axonframework.eventhandling.annotation.AsynchronousEventListener;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.fuin.auction.command.server.events.UserCreatedEvent;
import org.fuin.auction.command.server.events.UserEmailVerifiedEvent;
import org.fuin.auction.command.server.events.UserPasswordChangedEvent;
import org.fuin.auction.message.api.AuctionAggregateId;
import org.fuin.auction.message.api.AuctionMessage;
import org.fuin.auction.message.api.UserCreatedMessage;
import org.fuin.auction.message.api.UserEmailVerfiedMessage;
import org.fuin.auction.message.api.UserPasswordChangedMessage;
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
	 * Publish {@link UserCreatedEvent} as JMS message.
	 * 
	 * @param event
	 *            Event to publish.
	 */
	@EventHandler
	public final void handleUserCreatedEvent(final UserCreatedEvent event) {
		publish(new UserCreatedMessage(new AuctionAggregateId(event.getAggregateIdentifier()
		        .toString()), event.getUserName(), event.getEmail(), event.getPassword()));
	}

	/**
	 * Publish {@link UserEmailVerifiedEvent} as JMS message.
	 * 
	 * @param event
	 *            Event to publish.
	 */
	@EventHandler
	public final void handleUserEmailVerifiedEvent(final UserEmailVerifiedEvent event) {
		publish(new UserEmailVerfiedMessage(new AuctionAggregateId(event.getAggregateIdentifier()
		        .toString())));
	}

	/**
	 * Publish {@link UserPasswordChangedEvent} as JMS message.
	 * 
	 * @param event
	 *            Event to publish.
	 */
	@EventHandler
	public final void handleUserPasswordChangedEvent(final UserPasswordChangedEvent event) {
		publish(new UserPasswordChangedMessage(new AuctionAggregateId(event
		        .getAggregateIdentifier().toString()), event.getNewPassword()));
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
