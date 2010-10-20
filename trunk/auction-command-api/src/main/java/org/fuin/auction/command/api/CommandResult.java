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

import java.io.Serializable;
import java.util.List;

/**
 * Result of executing a command.
 */
public interface CommandResult extends Serializable {

	/**
	 * Returns the type of the result.
	 * 
	 * @return Type of result.
	 */
	public CommandResultType getType();

	/**
	 * Returns the information if the command was successful. Convenience method
	 * that checks the result type internally.
	 * 
	 * @return If the command was executed without error or warning
	 *         <code>true</code> else <code>false</code>.
	 */
	public boolean isSuccess();

	/**
	 * Returns the id of the message. This is the key within the
	 * <code>auction-command-messages.properties</code> file.
	 * 
	 * @return Unique id of the message.
	 */
	public int getMessageId();

	/**
	 * Returns an internal message describing a hint, warning or error.
	 * 
	 * @return Internal message.
	 */
	public String getInternalMessage();

	/**
	 * Returns a list of key/value pairs. This corresponds to the variables used
	 * within the value of key <code>messageId</code> in the
	 * <code>auction-command-messages.properties</code> file.
	 * 
	 * @return Key/values for localized messages.
	 */
	public List<MessageKeyValue> getMessageKeyValues();

}
