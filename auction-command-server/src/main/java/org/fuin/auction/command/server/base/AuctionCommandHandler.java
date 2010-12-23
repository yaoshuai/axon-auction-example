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
import org.fuin.auction.command.api.base.AggregateIdentifierResult;
import org.fuin.auction.command.api.base.CreateCategoryCommand;
import org.fuin.auction.command.api.base.DeleteCategoryCommand;
import org.fuin.auction.command.api.base.MarkCategoryForDeletionCommand;
import org.fuin.auction.command.api.base.RegisterUserCommand;
import org.fuin.auction.command.api.base.ResultCode;
import org.fuin.auction.command.api.base.UserChangePasswordCommand;
import org.fuin.auction.command.api.base.UserVerifyEmailCommand;
import org.fuin.auction.command.api.base.VoidResult;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.auction.command.server.domain.Category;
import org.fuin.auction.command.server.domain.IllegalCategoryStateException;
import org.fuin.auction.command.server.domain.IllegalUserStateException;
import org.fuin.auction.command.server.domain.PasswordMismatchException;
import org.fuin.auction.command.server.domain.SecurityTokenException;
import org.fuin.auction.command.server.domain.User;
import org.fuin.auction.common.CategoryName;
import org.fuin.axon.support.base.AggregateIdentifierFactory;
import org.fuin.axon.support.base.IllegalAggregateIdentifierException;
import org.fuin.axon.support.base.LongIdFactory;
import org.fuin.axon.support.base.UUIDFactory;
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
	@Named("categoryRepository")
	private Repository<Category> categoryRepository;

	@Inject
	@UUIDFactory
	private AggregateIdentifierFactory userAggregateIdFactory;

	@Inject
	@LongIdFactory(Category.class)
	private AggregateIdentifierFactory categoryAggregateIdFactory;

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

			final Category category = new Category(categoryAggregateIdFactory.create(),
			        categoryName);
			categoryRepository.add(category);

			return new AggregateIdentifierResult(ResultCode.CATEGORY_SUCCESSFULLY_CREATED, category
			        .getIdentifier().toString());

		} catch (final CategoryNameAlreadyExistException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.CATEGORY_ALREADY_EXISTS);
		}

	}

	/**
	 * Marks a category for deletion.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final CommandResult handle(final MarkCategoryForDeletionCommand command) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {
			final AggregateIdentifier id = categoryAggregateIdFactory.fromString(command
			        .getAggregateId());

			final Category category = categoryRepository.load(id);
			category.markForDeletion();

			return new AggregateIdentifierResult(
			        ResultCode.CATEGORY_SUCCESSFULLY_MARKED_FOR_DELETION, category.getIdentifier()
			                .toString());

		} catch (final IllegalCategoryStateException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.CATEGORY_TO_MARK_NOT_ACTIVE);
		} catch (final AggregateNotFoundException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.ID_NOT_FOUND);
		} catch (final IllegalAggregateIdentifierException ex) {
			LOG.info("Invalid command: " + ex.getMessage());
			return new VoidResult(ResultCode.INVALID_COMMAND);
		}

	}

	/**
	 * Marks a category for deletion.
	 * 
	 * @param command
	 *            Command to handle.
	 * 
	 * @return Result of the command.
	 */
	@CommandHandler
	public final CommandResult handle(final DeleteCategoryCommand command) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {
			final AggregateIdentifier id = categoryAggregateIdFactory.fromString(command
			        .getAggregateId());

			final Category category = categoryRepository.load(id);
			category.delete();

			// FIXME michael Remove category name from constraints -
			// Resolve the name by executing a query against the query server.
			// constraintSet.remove(categoryName);

			return new AggregateIdentifierResult(ResultCode.CATEGORY_SUCCESSFULLY_DELETED, category
			        .getIdentifier().toString());

		} catch (final IllegalCategoryStateException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.CATEGORY_TO_DELETE_NOT_MARKED);
		} catch (final AggregateNotFoundException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.ID_NOT_FOUND);
		} catch (final IllegalAggregateIdentifierException ex) {
			LOG.info("Invalid command: " + ex.getMessage());
			return new VoidResult(ResultCode.INVALID_COMMAND);
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

			return new AggregateIdentifierResult(ResultCode.USER_SUCCESSFULLY_REGISTERED, user
			        .getIdentifier().toString());

		} catch (final UserNameEmailCombinationAlreadyExistsException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.DUPLICATE_USERNAME_EMAIL_COMBINATION);
		} catch (final UserNameAlreadyExistsException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.DUPLICATE_USERNAME);
		} catch (final UserEmailAlreadyExistsException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.DUPLICATE_EMAIL);
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
	public final CommandResult handle(final UserChangePasswordCommand command) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {

			final AggregateIdentifier id = userAggregateIdFactory.fromString(command
			        .getAggregateId());
			final Password oldPw = new Password(command.getOldPassword());
			final Password newPw = new Password(command.getNewPassword());

			final User user = userRepository.load(id);

			user.changePassword(oldPw, newPw);

			return new VoidResult(ResultCode.PASSWORD_SUCCESSFULLY_CHANGED);

		} catch (final PasswordMismatchException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.PASSWORD_WRONG);
		} catch (final AggregateNotFoundException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.ID_NOT_FOUND);
		} catch (final IllegalAggregateIdentifierException ex) {
			LOG.info("Invalid command: " + ex.getMessage());
			return new VoidResult(ResultCode.INVALID_COMMAND);
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
	public final CommandResult handle(final UserVerifyEmailCommand command) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Handle command: " + command.toTraceString());
		}

		try {

			final AggregateIdentifier id = userAggregateIdFactory.fromString(command
			        .getAggregateId());
			final String securityToken = command.getSecurityToken();

			final User user = userRepository.load(id);

			user.verifyEmail(securityToken);

			return new VoidResult(ResultCode.USER_EMAIL_VERIFIED);

		} catch (final AggregateNotFoundException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.ID_NOT_FOUND);
		} catch (final SecurityTokenException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.USER_EMAIL_VERIFICATION_FAILED);
		} catch (final IllegalAggregateIdentifierException ex) {
			LOG.info("Invalid command: " + ex.getMessage());
			return new VoidResult(ResultCode.INVALID_COMMAND);
		} catch (final IllegalUserStateException ex) {
			LOG.info(ex.getMessage() + ": " + command.toTraceString());
			return new VoidResult(ResultCode.INVALID_COMMAND);
		}

	}

}
