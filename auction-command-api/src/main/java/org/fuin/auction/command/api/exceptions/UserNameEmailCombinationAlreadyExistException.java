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


/**
 * A user/email combination is already registered.
 */
public final class UserNameEmailCombinationAlreadyExistException extends CommandException {

	private static final long serialVersionUID = -5054752689224246974L;

	/** Unique message id for the exception. */
	public static final int MESSAGE_ID = 5;

	/**
	 * Constructor with message.
	 * 
	 * @param message
	 *            Error message.
	 */
	public UserNameEmailCombinationAlreadyExistException(final String message) {
		super(MESSAGE_ID, message);
	}

}
