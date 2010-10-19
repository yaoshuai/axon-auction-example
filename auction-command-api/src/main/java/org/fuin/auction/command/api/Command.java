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

/**
 * Common behavior shared by all commands.
 * 
 * @param <RESULT>
 *            Type of the result.
 */
public interface Command<RESULT extends CommandResult<RESULT>> extends Serializable {

	/**
	 * Returns the version of the command. When a command is changed, it's
	 * version is incremented by one.
	 * 
	 * @return Version number.
	 */
	public int getVersion();

	/**
	 * Returns a unique name of the command.
	 * 
	 * @return Full qualified class name
	 */
	public String getName();

	/**
	 * Creates the specific command result for an internal error.
	 * 
	 * @param message
	 *            Error message.
	 * 
	 * @return Internal error result.
	 */
	public RESULT internalError(String message);

	/**
	 * Creates the specific command result for an invalid command.
	 * 
	 * @param message
	 *            Error message.
	 * 
	 * @return Invalid command result.
	 */
	public RESULT invalidCommand(String message);

}
