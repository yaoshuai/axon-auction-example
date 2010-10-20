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
package org.fuin.auction.command.api;

import java.util.List;

/**
 * Result of sending a command without command specific attributes.
 */
public final class BasicCommandResult extends AbstractCommandResult {

	private static final long serialVersionUID = -3123164570987026143L;

	/**
	 * Default constructor.
	 */
	public BasicCommandResult() {
		super();
	}

	/**
	 * Constructor without key/values.
	 * 
	 * @param type
	 *            Type of the result.
	 * @param messageId
	 *            Unique id of the message.
	 * @param internalMessage
	 *            Internal message.
	 */
	public BasicCommandResult(final CommandResultType type, final int messageId,
	        final String internalMessage) {
		super(type, messageId, internalMessage);
	}

	/**
	 * Constructor with key/value array.
	 * 
	 * @param type
	 *            Type of the result.
	 * @param messageId
	 *            Unique id of the message.
	 * @param internalMessage
	 *            Internal message.
	 * @param messageKeyValues
	 *            Array of key/value pairs for localized error message.
	 */
	public BasicCommandResult(final CommandResultType type, final int messageId,
	        final String internalMessage, final MessageKeyValue... messageKeyValues) {
		super(type, messageId, internalMessage, messageKeyValues);
	}

	/**
	 * Constructor with key/value list.
	 * 
	 * @param type
	 *            Type of the result.
	 * @param messageId
	 *            Unique id of the message.
	 * @param internalMessage
	 *            Internal message.
	 * @param messageKeyValues
	 *            List of key/value pairs for localized error message.
	 */
	public BasicCommandResult(final CommandResultType type, final int messageId,
	        final String internalMessage, final List<MessageKeyValue> messageKeyValues) {
		super(type, messageId, internalMessage, messageKeyValues);
	}

}
