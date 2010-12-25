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
package org.fuin.auction.command.server.handler;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.fuin.auction.command.api.base.RegisterUserCommand;
import org.fuin.auction.command.api.base.ResultCode;
import org.fuin.auction.command.api.base.VoidResult;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.auction.command.server.base.UserEmailAlreadyExistsException;
import org.fuin.auction.command.server.base.UserNameAlreadyExistsException;
import org.fuin.auction.command.server.base.UserNameEmailCombinationAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// GENERATED CODE - DO NOT EDIT!

/**
 * Handler for managing {@link RegisterUserCommand} commands.
 */
public abstract class AbstractRegisterUserCommandHandler extends AbstractUserCommandHandler {
	private static final Logger LOG = LoggerFactory
	        .getLogger(AbstractRegisterUserCommandHandler.class);

	/**
	 * Registers a new user.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final CommandResult handle(final RegisterUserCommand command) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {
			return handleIntern(command);
		} catch (final UserEmailAlreadyExistsException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());

			return new VoidResult(ResultCode.DUPLICATE_EMAIL);
		} catch (final UserNameEmailCombinationAlreadyExistsException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());

			return new VoidResult(ResultCode.DUPLICATE_USERNAME_EMAIL_COMBINATION);
		} catch (final UserNameAlreadyExistsException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());

			return new VoidResult(ResultCode.DUPLICATE_USERNAME);
		}
	}

	/**
	 * Registers a new user.
	 * 
	 * @param command
	 *            Valid command to handle.
	 * 
	 * @return Result of the command.
	 * 
	 * @throws UserEmailAlreadyExistsException
	 *             The email address is already registered with another user
	 * @throws UserNameEmailCombinationAlreadyExistsException
	 *             The combination of user name and email is already registered
	 * @throws UserNameAlreadyExistsException
	 *             The name is already used by another user
	 */
	protected abstract CommandResult handleIntern(final RegisterUserCommand command)
	        throws UserEmailAlreadyExistsException, UserNameEmailCombinationAlreadyExistsException,
	        UserNameAlreadyExistsException;
}
