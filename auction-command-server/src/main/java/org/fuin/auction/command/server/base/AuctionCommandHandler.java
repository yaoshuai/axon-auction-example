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
import org.fuin.auction.command.api.base.AggregateIdentifierUUIDResult;
import org.fuin.auction.command.api.base.ChangeUserPasswordCommand;
import org.fuin.auction.command.api.base.CreateCategoryCommand;
import org.fuin.auction.command.api.base.RegisterUserCommand;
import org.fuin.auction.command.api.base.ResultCode;
import org.fuin.auction.command.api.base.VerifyUserEmailCommand;
import org.fuin.auction.command.api.base.VoidResult;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.auction.command.server.domain.IllegalUserStateException;
import org.fuin.auction.command.server.domain.PasswordMismatchException;
import org.fuin.auction.command.server.domain.SecurityTokenException;
import org.fuin.auction.command.server.domain.User;
import org.fuin.auction.command.server.support.AggregateIdentifierFactory;
import org.fuin.auction.command.server.support.IdUUID;
import org.fuin.auction.common.CategoryName;
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
	 * Create a new category.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final CommandResult handle(final CreateCategoryCommand command) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}
		try {

			final CategoryName categoryName = new CategoryName(command.getName());
			constraintSet.add(categoryName);

			return null;

		} catch (final CategoryNameAlreadyExistException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.CATEGORY_ALREADY_EXISTS);
		} catch (final RuntimeException ex) {
			LOG.error(command.toTraceString(), ex);
			return createAndLogVoidResult(ResultCode.INTERNAL_ERROR);
		}

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

			return createAndLogAggregateIdResult(ResultCode.USER_SUCCESSFULLY_REGISTERED, user
			        .getIdentifier());

		} catch (final UserNameEmailCombinationAlreadyExistsException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.DUPLICATE_USERNAME_EMAIL_COMBINATION);
		} catch (final UserNameAlreadyExistsException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.DUPLICATE_USERNAME);
		} catch (final UserEmailAlreadyExistsException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.DUPLICATE_EMAIL);
		} catch (final RuntimeException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.INTERNAL_ERROR);
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

			return createAndLogVoidResult(ResultCode.PASSWORD_SUCCESSFULLY_CHANGED);

		} catch (final PasswordMismatchException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.PASSWORD_WRONG);
		} catch (final AggregateNotFoundException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.ID_NOT_FOUND);
		} catch (final RuntimeException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.INTERNAL_ERROR);
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

			return createAndLogVoidResult(ResultCode.USER_EMAIL_VERIFIED);

		} catch (final AggregateNotFoundException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.ID_NOT_FOUND);
		} catch (final SecurityTokenException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.USER_EMAIL_VERIFICATION_FAILED);
		} catch (final IllegalUserStateException ex) {
			LOG.error(ex.getMessage() + ": " + command.toTraceString());
			return createAndLogVoidResult(ResultCode.INVALID_COMMAND);
		}

	}

	private CommandResult createAndLogVoidResult(final ResultCode resultCode) {
		final CommandResult result = new VoidResult(resultCode);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Result: " + result.toTraceString());
		}
		return result;
	}

	private CommandResult createAndLogAggregateIdResult(final ResultCode resultCode,
	        final AggregateIdentifier id) {
		final CommandResult result = new AggregateIdentifierUUIDResult(resultCode, id.toString());
		if (LOG.isDebugEnabled()) {
			LOG.debug("Result: " + result.toTraceString());
		}
		return result;
	}

}
