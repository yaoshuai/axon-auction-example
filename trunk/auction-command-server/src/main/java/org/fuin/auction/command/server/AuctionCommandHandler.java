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

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.fuin.auction.command.api.GetServerInfoCommand;
import org.fuin.auction.command.api.GetServerInfoCommandResult;
import org.fuin.auction.command.api.RegisterUserCommand;
import org.fuin.auction.command.api.RegisterUserCommandResult;
import org.fuin.auction.common.FailedToLoadProjectInfoException;
import org.fuin.auction.common.ProjectInfo;
import org.fuin.auction.common.Utils;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.Password;
import org.fuin.objects4j.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handler for managing the auction commands.
 */
@Component
public class AuctionCommandHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AuctionCommandHandler.class);

	@Autowired
	private ConstraintService constraintService;

	/**
	 * Sets the internal query service.
	 * 
	 * @param queryService
	 *            Service to set.
	 */
	protected final void setQueryInternalService(final ConstraintService queryService) {
		this.constraintService = queryService;
	}

	/**
	 * Returns the server information.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final GetServerInfoCommandResult handle(final GetServerInfoCommand command) {
		try {
			final ProjectInfo projectInfo = Utils.getProjectInfo(this.getClass(),
			        "/auction-command-server.properties");
			return new GetServerInfoCommandResult(projectInfo.getName(), projectInfo.getVersion(),
			        projectInfo.getBuildTimestampAsDate());
		} catch (final FailedToLoadProjectInfoException ex) {
			LOG.error("Unable to retrieve server info!", ex);
			return new GetServerInfoCommandResult().internalError(Utils.createMessage(ex));
		}
	}

	/**
	 * Registers a new user.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final RegisterUserCommandResult handle(final RegisterUserCommand command) {

		try {

			final UserId userId = new UserId(command.getUserId());
			final EmailAddress emailAddress = new EmailAddress(command.getEmail());
			final Password password = new Password(command.getPassword());

			constraintService.add(userId, emailAddress);

			final User user = new User(userId, password, emailAddress);

			return new RegisterUserCommandResult(user.getIdentifier().toString());

		} catch (final UserIdEmailCombinationAlreadyExistException ex) {
			return RegisterUserCommandResult.createUserIdEmailCombinationAlreadyExistError(ex
			        .getMessage());
		} catch (final UserIdAlreadyExistException ex) {
			return RegisterUserCommandResult.createUserIdAlreadyExistError(ex.getMessage());
		} catch (final EmailAlreadyExistException ex) {
			return RegisterUserCommandResult.createEmailAlreadyExistError(ex.getMessage());
		}

	}

}
