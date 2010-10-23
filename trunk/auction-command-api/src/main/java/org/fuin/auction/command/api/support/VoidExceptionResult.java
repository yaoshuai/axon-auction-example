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
package org.fuin.auction.command.api.support;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Result of sending a command without command specific attributes.
 */
public final class VoidExceptionResult extends AbstractCommandResult {

	private static final long serialVersionUID = -3123164570987026143L;

	/**
	 * Constructor without key/values.
	 * 
	 * @param messageId
	 *            Unique id of the message.
	 * @param internalMessage
	 *            Internal message.
	 */
	public VoidExceptionResult(final int messageId, final String internalMessage) {
		super(CommandResultType.ERROR, messageId, internalMessage);
	}

	/**
	 * Constructor with key/value array.
	 * 
	 * @param messageId
	 *            Unique id of the message.
	 * @param internalMessage
	 *            Internal message.
	 * @param messageKeyValues
	 *            Array of key/value pairs for localized error message.
	 */
	public VoidExceptionResult(final int messageId, final String internalMessage,
	        final MessageKeyValue... messageKeyValues) {
		super(CommandResultType.ERROR, messageId, internalMessage, messageKeyValues);
	}

	/**
	 * Constructor with key/value list.
	 * 
	 * @param messageId
	 *            Unique id of the message.
	 * @param internalMessage
	 *            Internal message.
	 * @param messageKeyValues
	 *            List of key/value pairs for localized error message.
	 */
	public VoidExceptionResult(final int messageId, final String internalMessage,
	        final List<MessageKeyValue> messageKeyValues) {
		super(CommandResultType.ERROR, messageId, internalMessage, messageKeyValues);
	}

	@Override
	public final String toTraceString() {
		final ToStringBuilder builder = new ToStringBuilder(this);
		appendAbstractCommandResult(builder);
		return builder.toString();
	}

}
