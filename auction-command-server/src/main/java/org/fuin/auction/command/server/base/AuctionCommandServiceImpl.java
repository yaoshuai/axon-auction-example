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
package org.fuin.auction.command.server.base;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.fuin.auction.command.api.base.AuctionCommandService;
import org.fuin.auction.command.api.base.ResultCode;
import org.fuin.auction.command.api.base.VoidResult;
import org.fuin.auction.command.api.support.Command;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.objects4j.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the {@link AuctionCommandService}.
 */
public class AuctionCommandServiceImpl implements AuctionCommandService {

	private static final Logger LOG = LoggerFactory.getLogger(AuctionCommandServiceImpl.class);

	@Inject
	private CommandBus commandBus;

	/**
	 * Sets the command bus.
	 * 
	 * @param commandBus
	 *            Command bus.
	 */
	public final void setCommandBus(final CommandBus commandBus) {
		this.commandBus = commandBus;
	}

	@Override
	public final CommandResult send(final Command command) {

		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Received command: " + command.toTraceString());
			}
			validateCommand(command);

			final FutureCallback<CommandResult> callback = new FutureCallback<CommandResult>();
			commandBus.dispatch(command, callback);
			final CommandResult result = callback.get();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Command result: " + result.toTraceString());
			}
			validateResult(result);

			return result;

		} catch (final InvalidCommandException ex) {
			LOG.error("Invalid command: " + command.toTraceString(), ex);
			return new VoidResult(ResultCode.INVALID_COMMAND);
		} catch (final ExecutionException ex) {
			LOG.error("Error executing command: " + command.toTraceString(), ex);
			return new VoidResult(ResultCode.INTERNAL_ERROR);
		} catch (final InvalidResultException ex) {
			LOG.error("Invalid result: " + ex.getResult().toTraceString() + ", command: "
			        + command.toTraceString(), ex);
			return new VoidResult(ResultCode.INTERNAL_ERROR);
		} catch (final InterruptedException ex) {
			LOG.error("Interrupted error: " + command.toTraceString(), ex);
			return new VoidResult(ResultCode.INTERNAL_ERROR);
		} catch (final RuntimeException ex) {
			LOG.error("Internal error: " + command.toTraceString(), ex);
			return new VoidResult(ResultCode.INTERNAL_ERROR);
		}

	}

	/**
	 * Checks if the command is valid.
	 * 
	 * @param command
	 *            Command to log and validate.
	 * 
	 * @throws InvalidCommandException
	 *             The command was invalid.
	 */
	private void validateCommand(final Command command) throws InvalidCommandException {
		try {
			// Don't let invalid commands get through
			Contract.requireValid(command);
		} catch (final IllegalStateException ex) {
			throw new InvalidCommandException(ex);
		}
	}

	/**
	 * Checks if the result is valid.
	 * 
	 * @param result
	 *            Result to log and validate.
	 * 
	 * @throws InvalidResultException
	 *             The result was invalid.
	 */
	private void validateResult(final CommandResult result) throws InvalidResultException {
		try {
			Contract.requireValid(result);
		} catch (final IllegalStateException ex) {
			// Violated post condition / Programming error!
			throw new InvalidResultException(ex, result);
		}
	}

}
