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
import org.axonframework.repository.AggregateNotFoundException;
import org.fuin.auction.command.api.base.ResultCode;
import org.fuin.auction.command.api.base.UserVerifyEmailCommand;
import org.fuin.auction.command.api.base.VoidResult;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.auction.command.server.domain.IllegalUserStateException;
import org.fuin.auction.command.server.domain.SecurityTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// GENERATED CODE - DO NOT EDIT!

/**
 * Handler for managing {@link UserVerifyEmailCommand} commands.
 */
public abstract class AbstractUserVerifyEmailCommandHandler extends AbstractUserCommandHandler {
	private static final Logger LOG = LoggerFactory
	        .getLogger(AbstractUserVerifyEmailCommandHandler.class);

	/**
	 * Verifies the user's email by checking the verification token.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final CommandResult handle(final UserVerifyEmailCommand command) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {
			return handleIntern(command);
		} catch (final IllegalUserStateException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());

			return new VoidResult(ResultCode.ILLEGAL_USER_STATE);
		} catch (final SecurityTokenException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());

			return new VoidResult(ResultCode.USER_EMAIL_VERIFICATION_FAILED);
		} catch (final AggregateNotFoundException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());

			return new VoidResult(ResultCode.ID_NOT_FOUND);
		}
	}

	/**
	 * Verifies the user's email by checking the verification token.
	 * 
	 * @param command
	 *            Valid command to handle.
	 * 
	 * @return Result of the command.
	 * 
	 * @throws IllegalUserStateException
	 *             The state of the user was not NEW or RESET.
	 * @throws SecurityTokenException
	 *             The given token was not equal to the user's verification
	 *             token.
	 */
	protected abstract CommandResult handleIntern(final UserVerifyEmailCommand command)
	        throws IllegalUserStateException, SecurityTokenException;
}
