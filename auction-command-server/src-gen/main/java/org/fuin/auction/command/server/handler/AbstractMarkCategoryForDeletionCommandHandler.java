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
import org.fuin.auction.command.api.base.AggregateIdNotFoundResult;
import org.fuin.auction.command.api.base.MarkCategoryForDeletionCommand;
import org.fuin.auction.command.api.base.MarkCategoryForDeletionFailedIllegalStateResult;
import org.fuin.auction.command.server.domain.IllegalCategoryStateException;
import org.fuin.auction.common.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// GENERATED CODE - DO NOT EDIT!

/**
 * Handler for managing {@link MarkCategoryForDeletionCommand} commands.
 */
public abstract class AbstractMarkCategoryForDeletionCommandHandler extends
        AbstractCategoryCommandHandler {
	private static final Logger LOG = LoggerFactory
	        .getLogger(AbstractMarkCategoryForDeletionCommandHandler.class);

	/**
	 * Marks an existing category for deletion.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final OperationResult handle(final MarkCategoryForDeletionCommand command) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {
			return handleIntern(command);
		} catch (final IllegalCategoryStateException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());

			return new MarkCategoryForDeletionFailedIllegalStateResult(ex.getCurrent(), ex
			        .getExpected());
		} catch (final AggregateNotFoundException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());

			return new AggregateIdNotFoundResult();
		}
	}

	/**
	 * Marks an existing category for deletion.
	 * 
	 * @param command
	 *            Valid command to handle.
	 * 
	 * @return Result of the command.
	 * 
	 * @throws IllegalCategoryStateException
	 *             The category was not in an active state.
	 */
	protected abstract OperationResult handleIntern(final MarkCategoryForDeletionCommand command)
	        throws IllegalCategoryStateException;
}
