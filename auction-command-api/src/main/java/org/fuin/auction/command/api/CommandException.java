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
 * Base class for all command related exceptions.
 */
public abstract class CommandException extends Exception {

	private static final long serialVersionUID = 5330257203043717352L;

	/**
	 * Constructor with message.
	 * 
	 * @param message
	 *            Error message.
	 */
	public CommandException(final String message) {
		super(message);
	}

	/**
	 * Returns a system wide unique message id of this exception.
	 * 
	 * @return Unique exception message id.
	 */
	public abstract int getMessageId();

	/**
	 * Returns a list of key/value pairs. This corresponds to the variables used
	 * within the value of key <code>messageId</code> in the
	 * <code>auction-command-messages.properties</code> file.
	 * 
	 * @return Key/values for localized messages or <code>null</code> if no
	 *         entries are available.
	 */
	public abstract List<MessageKeyValue> getMessageKeyValues();

	/**
	 * Converts the exception into a command result.
	 * 
	 * @return Command result.
	 */
	public abstract CommandResult toCommandResult();

}
