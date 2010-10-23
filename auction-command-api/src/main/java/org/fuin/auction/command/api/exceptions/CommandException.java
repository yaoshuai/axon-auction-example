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
package org.fuin.auction.command.api.exceptions;

import java.util.Arrays;
import java.util.List;

import org.fuin.auction.command.api.support.MessageKeyValue;
import org.fuin.auction.command.api.support.VoidExceptionResult;

/**
 * Base class for all command related exceptions.
 */
public abstract class CommandException extends Exception {

	private static final long serialVersionUID = 5330257203043717352L;

	private final int messageId;

	private final List<MessageKeyValue> messageKeyValues;

	/**
	 * Constructor with id and message.
	 * 
	 * @param messageId
	 *            Unique id of the message.
	 * @param message
	 *            Error message.
	 */
	public CommandException(final int messageId, final String message) {
		this(messageId, message, (List<MessageKeyValue>) null);
	}

	/**
	 * Constructor with id, message and key/value array.
	 * 
	 * @param messageId
	 *            Unique id of the message.
	 * @param message
	 *            Error message.
	 * @param messageKeyValues
	 *            Array of key/value pairs for localized error message.
	 */
	public CommandException(final int messageId, final String message,
	        final MessageKeyValue... messageKeyValues) {
		this(messageId, message, Arrays.asList(messageKeyValues));
	}

	/**
	 * Constructor with id, message and key/value list.
	 * 
	 * @param messageId
	 *            Unique id of the message.
	 * @param message
	 *            Error message.
	 * @param messageKeyValues
	 *            List of key/value pairs for localized error message.
	 */
	public CommandException(final int messageId, final String message,
	        final List<MessageKeyValue> messageKeyValues) {
		super(message);
		this.messageId = messageId;
		this.messageKeyValues = messageKeyValues;
	}

	/**
	 * Returns a system wide unique message id of this exception.
	 * 
	 * @return Unique exception message id.
	 */
	public final int getMessageId() {
		return messageId;
	}

	/**
	 * Returns a list of key/value pairs. This corresponds to the variables used
	 * within the value of key <code>messageId</code> in the
	 * <code>auction-command-messages.properties</code> file.
	 * 
	 * @return Key/values for localized messages or <code>null</code> if no
	 *         entries are available.
	 */
	public final List<MessageKeyValue> getMessageKeyValues() {
		return messageKeyValues;
	}

	/**
	 * Converts the exception into a command result.
	 * 
	 * @return Command result.
	 */
	public final VoidExceptionResult toResult() {
		return new VoidExceptionResult(messageId, getMessage());
	}

}
