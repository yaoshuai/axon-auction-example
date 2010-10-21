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

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.objects4j.Requires;

/**
 * Basic result of sending/executing a command.
 */
public abstract class AbstractCommandResult implements CommandResult {

	private static final long serialVersionUID = 2861284299330260251L;

	@NotNull
	private CommandResultType type;

	@Min(0)
	private int messageId;

	private String internalMessage;

	private List<MessageKeyValue> messageKeyValues;

	/**
	 * Default constructor.
	 */
	public AbstractCommandResult() {
		super();
		type = null;
		messageId = -1;
		internalMessage = null;
		messageKeyValues = null;
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
	public AbstractCommandResult(final CommandResultType type, final int messageId,
	        final String internalMessage) {
		this(type, messageId, internalMessage, (List<MessageKeyValue>) null);
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
	public AbstractCommandResult(final CommandResultType type, final int messageId,
	        final String internalMessage, final MessageKeyValue... messageKeyValues) {
		this(type, messageId, internalMessage, Arrays.asList(messageKeyValues));
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
	public AbstractCommandResult(final CommandResultType type, final int messageId,
	        final String internalMessage, final List<MessageKeyValue> messageKeyValues) {
		this.type = type;
		this.messageId = messageId;
		this.internalMessage = internalMessage;
		this.messageKeyValues = messageKeyValues;
	}

	@Override
	public final CommandResultType getType() {
		return type;
	}

	/**
	 * Sets the type of the result.
	 * 
	 * @param type
	 *            Type of result.
	 */
	@Requires("type != null")
	public final void setType(final CommandResultType type) {
		this.type = type;
	}

	@Override
	public final boolean isSuccess() {
		return (CommandResultType.SUCCESS.equals(type));
	}

	@Override
	public final int getMessageId() {
		return messageId;
	}

	/**
	 * Sets the id of the message. This is the key within the
	 * <code>auction-command-messages.properties</code> file.
	 * 
	 * @param messageId
	 *            Unique id of the message.
	 */
	@Requires("messageId > 0")
	public final void setMessageId(final int messageId) {
		this.messageId = messageId;
	}

	@Override
	public final String getInternalMessage() {
		return internalMessage;
	}

	/**
	 * Sets an internal message describing a hint, warning or error.
	 * 
	 * @param internalMessage
	 *            Internal message.
	 */
	public final void setInternalMessage(final String internalMessage) {
		this.internalMessage = internalMessage;
	}

	@Override
	public final List<MessageKeyValue> getMessageKeyValues() {
		return messageKeyValues;
	}

	/**
	 * Sets a list of key/value pairs. This corresponds to the variables used
	 * within the value of key <code>messageId</code> in the
	 * <code>auction-command-messages.properties</code> file.
	 * 
	 * @param messageKeyValues
	 *            Key/values for localized messages.
	 */
	public final void setMessageKeyValues(final List<MessageKeyValue> messageKeyValues) {
		this.messageKeyValues = messageKeyValues;
	}

	/**
	 * Appends all properties to the builder.
	 * 
	 * @param builder
	 *            Builder to append key/values.
	 */
	protected final void appendAbstractCommandResult(final ToStringBuilder builder) {
		builder.append("type", type).append("messageId", messageId).append("internalMessage",
		        internalMessage).append("messageKeyValues", messageKeyValues);
	}

}
