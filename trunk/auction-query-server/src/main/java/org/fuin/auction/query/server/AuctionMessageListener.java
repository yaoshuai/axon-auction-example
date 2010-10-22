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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.fuin.auction.message.api.UserCreatedMessage;
import org.fuin.auction.message.api.UserEmailVerfiedMessage;
import org.fuin.auction.message.api.UserPasswordChangedMessage;

/**
 * Handles incoming JMS messages.
 */
public final class AuctionMessageListener implements MessageListener {

	@Override
	public final void onMessage(final Message message) {

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
					System.out.println("RECEIVED NON-AUCTION MESSAGE: " + obj);
				}
			} catch (final JMSException ex) {
				ex.printStackTrace();
			}
		} else {
			System.out.println("RECEIVED NON-OBJECT MESSAGE: " + message);
		}

	}

	private void handleMessage(final UserCreatedMessage message) {
		System.out.println("RECEIVED " + message.toTraceString());
	}

	private void handleMessage(final UserEmailVerfiedMessage message) {
		System.out.println("RECEIVED " + message.toTraceString());
	}

	private void handleMessage(final UserPasswordChangedMessage message) {
		System.out.println("RECEIVED " + message.toTraceString());
	}

}
