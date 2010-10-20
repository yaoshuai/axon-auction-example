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
package org.fuin.auction.command.server;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.FutureCallback;
import org.fuin.auction.command.api.AbstractCommandResult;
import org.fuin.auction.command.api.AuctionCommandService;
import org.fuin.auction.command.api.Command;
import org.fuin.auction.common.Utils;
import org.fuin.objects4j.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implements the {@link AuctionCommandService}.
 */
public class AuctionCommandServiceImpl implements AuctionCommandService {

	private static final Logger LOG = LoggerFactory.getLogger(AuctionCommandServiceImpl.class);

	@Autowired
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
	public final <RESULT extends AbstractCommandResult<RESULT>> RESULT send(
	        final Command<RESULT> command) {

		try {
			// Don't let invalid commands get through
			Contract.requireValid(command);
		} catch (final IllegalStateException ex) {
			LOG.error("Invalid command: " + command, ex);
			return command.invalidCommand(Utils.createMessage(ex));
		}

		try {

			// Dispatch command and wait for result
			final FutureCallback<Command<RESULT>, RESULT> callback;
			callback = new FutureCallback<Command<RESULT>, RESULT>();
			commandBus.dispatch(command, callback);
			return callback.get();

		} catch (final Exception ex) {
			LOG.error("Internal error: " + command, ex);
			return command.internalError(Utils.createMessage(ex));
		}

	}

}