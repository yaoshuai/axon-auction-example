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

import javax.inject.Inject;
import javax.inject.Named;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.repository.Repository;
import org.fuin.auction.command.api.base.ChangeUserPasswordCommand;
import org.fuin.auction.command.api.base.RegisterUserCommand;
import org.fuin.auction.command.api.base.VerifyUserEmailCommand;
import org.fuin.auction.command.api.extended.IdNotFoundException;
import org.fuin.auction.command.api.extended.InvalidCommandException;
import org.fuin.auction.command.api.extended.PasswordException;
import org.fuin.auction.command.api.extended.UserEmailAlreadyExistException;
import org.fuin.auction.command.api.extended.UserEmailVerificationFailedException;
import org.fuin.auction.command.api.extended.UserNameAlreadyExistException;
import org.fuin.auction.command.api.extended.UserNameEmailCombinationAlreadyExistException;
import org.fuin.auction.command.api.support.AggregateIdentifierUUIDResult;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.auction.command.api.support.VoidSuccessResult;
import org.fuin.auction.command.server.domain.IllegalUserStateException;
import org.fuin.auction.command.server.domain.PasswordMismatchException;
import org.fuin.auction.command.server.domain.SecurityTokenException;
import org.fuin.auction.command.server.domain.User;
import org.fuin.auction.command.server.support.AggregateIdentifierFactory;
import org.fuin.auction.command.server.support.IdUUID;
import org.fuin.auction.common.Utils;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.Password;
import org.fuin.objects4j.UserName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for managing the auction commands.
 */
@Named
public class AuctionCommandHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AuctionCommandHandler.class);

	@Inject
	private ConstraintSet constraintSet;

	@Inject
	@Named("userRepository")
	private Repository<User> userRepository;

	@Inject
	@IdUUID
	private AggregateIdentifierFactory userAggregateIdFactory;

	/**
	 * Sets the constraint set.
	 * 
	 * @param constraintSet
	 *            Constraint set to set.
	 */
	protected final void setConstraintSet(final ConstraintSet constraintSet) {
		this.constraintSet = constraintSet;
	}

	/**
	 * Sets the user repository.
	 * 
	 * @param userRepository
	 *            User repository to set.
	 */
	protected final void setUserRepository(final Repository<User> userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Sets the user aggregate identifier factory.
	 * 
	 * @param userAggregateIdFactory
	 *            User aggregate identifier factory to set.
	 */
	public final void setUserAggregateIdFactory(
	        final AggregateIdentifierFactory userAggregateIdFactory) {
		this.userAggregateIdFactory = userAggregateIdFactory;
	}

	/**
	 * Register a new user.
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

			final UserName userName = new UserName(command.getUserName());
			final EmailAddress emailAddress = new EmailAddress(command.getEmail());
			final Password password = new Password(command.getPassword());

			constraintSet.add(userName, emailAddress);

			final User user = new User(userAggregateIdFactory.create(), userName, password,
			        emailAddress);
			userRepository.add(user);

			return createAndLogAggregateIdResult(user.getIdentifier());

		} catch (final UserNameEmailCombinationAlreadyExistException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return ex.toResult();
		} catch (final UserNameAlreadyExistException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return ex.toResult();
		} catch (final UserEmailAlreadyExistException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return ex.toResult();
		}

	}

	/**
	 * Change the user's password.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final CommandResult handle(final ChangeUserPasswordCommand command) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {

			final AggregateIdentifier id = userAggregateIdFactory.fromString(command
			        .getUserAggregateId());
			final Password oldPw = new Password(command.getOldPassword());
			final Password newPw = new Password(command.getNewPassword());

			final User user = userRepository.load(id);

			user.changePassword(oldPw, newPw);

			return createAndLogVoidSuccessResult();

		} catch (final PasswordMismatchException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return new PasswordException(ex.getMessage()).toResult();
		} catch (final AggregateNotFoundException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return new IdNotFoundException(Utils.createMessage(ex)).toResult();
		}

	}

	/**
	 * Verify the user.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final CommandResult handle(final VerifyUserEmailCommand command) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {

			final AggregateIdentifier id = userAggregateIdFactory.fromString(command
			        .getUserAggregateId());
			final String securityToken = command.getSecurityToken();

			final User user = userRepository.load(id);

			user.verifyEmail(securityToken);

			return createAndLogVoidSuccessResult();

		} catch (final AggregateNotFoundException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return new IdNotFoundException(Utils.createMessage(ex)).toResult();
		} catch (final SecurityTokenException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return new UserEmailVerificationFailedException().toResult();
		} catch (final IllegalUserStateException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return new InvalidCommandException(ex.getMessage()).toResult();
		}

	}

	private CommandResult createAndLogVoidSuccessResult() {
		final CommandResult result = new VoidSuccessResult();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Result: " + result.toTraceString());
		}
		return result;
	}

	private CommandResult createAndLogAggregateIdResult(final AggregateIdentifier id) {
		final CommandResult result = new AggregateIdentifierUUIDResult(id.toString());
		if (LOG.isDebugEnabled()) {
			LOG.debug("Result: " + result.toTraceString());
		}
		return result;
	}

}
