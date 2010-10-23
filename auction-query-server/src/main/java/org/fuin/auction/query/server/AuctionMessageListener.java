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
package org.fuin.auction.query.server;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.fuin.auction.common.UserState;
import org.fuin.auction.message.api.UserCreatedMessage;
import org.fuin.auction.message.api.UserEmailVerfiedMessage;
import org.fuin.auction.message.api.UserPasswordChangedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles incoming JMS messages.
 */
@Named
public class AuctionMessageListener implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(AuctionMessageListener.class);

	@Inject
	private AuctionUserDao userDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public final void onMessage(final Message message) {

		if (LOG.isDebugEnabled()) {
			LOG.debug(message.toString());
		}

		try {

			if (message instanceof ObjectMessage) {
				final ObjectMessage objectMessage = (ObjectMessage) message;
				try {
					final Serializable obj = objectMessage.getObject();
					if (obj instanceof UserCreatedMessage) {
						handleMessage((UserCreatedMessage) obj);
					} else if (obj instanceof UserEmailVerfiedMessage) {
						handleMessage((UserEmailVerfiedMessage) obj);
					} else if (obj instanceof UserPasswordChangedMessage) {
						handleMessage((UserPasswordChangedMessage) obj);
					} else {
						LOG.warn("Received non-Auction message: " + obj);
					}
				} catch (final JMSException ex) {
					LOG.error("Error reading object from message", ex);
				}
			} else {
				LOG.warn("Received non-Object message: " + message);
			}

		} catch (final RuntimeException ex) {
			LOG.error("Error handling message", ex);
		}

	}

	private void handleMessage(final UserCreatedMessage message) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle: " + message.toTraceString());
		}

		userDao.persist(new AuctionUser(message.getUserAggregateId(), message.getUserName(), message
		        .getEmail(), UserState.NEW, message.getPassword()));

	}

	private void handleMessage(final UserEmailVerfiedMessage message) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle: " + message.toTraceString());
		}

		final AuctionUser user = userDao.findByAggregateId(message.getUserAggregateId().toString());
		user.setState(UserState.ACTIVE);

	}

	private void handleMessage(final UserPasswordChangedMessage message) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle: " + message.toTraceString());
		}
		// Nothing to do for now...

	}

}
