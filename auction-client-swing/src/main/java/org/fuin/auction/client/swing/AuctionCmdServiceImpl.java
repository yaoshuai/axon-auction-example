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
package org.fuin.auction.client.swing;

import java.net.MalformedURLException;

import org.fuin.auction.command.api.base.AuctionCommandService;
import org.fuin.auction.command.api.base.ChangeUserPasswordCommand;
import org.fuin.auction.command.api.base.RegisterUserCommand;
import org.fuin.auction.command.api.base.VerifyUserEmailCommand;
import org.fuin.auction.command.api.exceptions.AuctionCmdService;
import org.fuin.auction.command.api.exceptions.EmailAlreadyExistException;
import org.fuin.auction.command.api.exceptions.IdNotFoundException;
import org.fuin.auction.command.api.exceptions.InternalErrorException;
import org.fuin.auction.command.api.exceptions.InvalidCommandException;
import org.fuin.auction.command.api.exceptions.PasswordException;
import org.fuin.auction.command.api.exceptions.UserEmailVerificationFailedException;
import org.fuin.auction.command.api.exceptions.UserIdAlreadyExistException;
import org.fuin.auction.command.api.exceptions.UserIdEmailCombinationAlreadyExistException;
import org.fuin.auction.command.api.support.AggregateIdResult;
import org.fuin.auction.command.api.support.CommandResult;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Default implementation.<br>
 * <br>
 * TODO michael 20.10.2010 Generate this class on-the-fly with
 * 
 * {@link http://www.fuin.org/srcgen4javassist/index.html}
 * 
 * or replace it with some other automatic construct.
 */
public final class AuctionCmdServiceImpl implements AuctionCmdService {

	private final AuctionCommandService commandService;

	/**
	 * Constructor with factory and URL.
	 * 
	 * @param factory
	 *            Factory to use.
	 * @param url
	 *            Service URL.
	 * 
	 * @throws MalformedURLException
	 *             The URL was invalid.
	 */
	public AuctionCmdServiceImpl(final HessianProxyFactory factory, final String url)
	        throws MalformedURLException {
		super();
		commandService = (AuctionCommandService) factory.create(AuctionCommandService.class, url);

	}

	@Override
	public final String registerUser(final String userId, final String password, final String email)
	        throws UserIdEmailCombinationAlreadyExistException, UserIdAlreadyExistException,
	        EmailAlreadyExistException, InternalErrorException, InvalidCommandException {

		final RegisterUserCommand cmd = new RegisterUserCommand(userId, password, email);

		final CommandResult result = commandService.send(cmd);
		if (result.isSuccess()) {
			final AggregateIdResult rucr = (AggregateIdResult) result;
			return rucr.getId();
		}

		// Error handling
		switch (result.getMessageId()) {
		case UserIdEmailCombinationAlreadyExistException.MESSAGE_ID:
			throw new UserIdEmailCombinationAlreadyExistException(result.getInternalMessage());
		case UserIdAlreadyExistException.MESSAGE_ID:
			throw new UserIdAlreadyExistException(result.getInternalMessage());
		case EmailAlreadyExistException.MESSAGE_ID:
			throw new EmailAlreadyExistException(result.getInternalMessage());
		case InternalErrorException.MESSAGE_ID:
			throw new InternalErrorException(result.getInternalMessage());
		case InvalidCommandException.MESSAGE_ID:
			throw new InvalidCommandException(result.getInternalMessage());
		default:
			throw new IllegalStateException("Unknown message id: " + result.getMessageId());
		}

	}

	@Override
	public final void changePassword(final String userAggregateId, final String oldPassword,
	        final String newPassword) throws IdNotFoundException, PasswordException,
	        InternalErrorException, InvalidCommandException {

		final ChangeUserPasswordCommand cmd = new ChangeUserPasswordCommand(userAggregateId, oldPassword,
		        newPassword);

		final CommandResult result = commandService.send(cmd);
		if (!result.isSuccess()) {

			// Error handling
			switch (result.getMessageId()) {
			case IdNotFoundException.MESSAGE_ID:
				throw new IdNotFoundException(result.getInternalMessage());
			case PasswordException.MESSAGE_ID:
				throw new PasswordException(result.getInternalMessage());
			case InternalErrorException.MESSAGE_ID:
				throw new InternalErrorException(result.getInternalMessage());
			case InvalidCommandException.MESSAGE_ID:
				throw new InvalidCommandException(result.getInternalMessage());
			default:
				throw new IllegalStateException("Unknown message id: " + result.getMessageId());
			}

		}

	}

	@Override
	public final void verifyUser(final String userAggregateId, final String securityToken)
	        throws IdNotFoundException, UserEmailVerificationFailedException, InternalErrorException,
	        InvalidCommandException {

		final VerifyUserEmailCommand cmd = new VerifyUserEmailCommand(userAggregateId, securityToken);
		final CommandResult result = commandService.send(cmd);
		if (!result.isSuccess()) {

			// Error handling
			switch (result.getMessageId()) {
			case IdNotFoundException.MESSAGE_ID:
				throw new IdNotFoundException(result.getInternalMessage());
			case UserEmailVerificationFailedException.MESSAGE_ID:
				throw new UserEmailVerificationFailedException();
			case InternalErrorException.MESSAGE_ID:
				throw new InternalErrorException(result.getInternalMessage());
			case InvalidCommandException.MESSAGE_ID:
				throw new InvalidCommandException(result.getInternalMessage());
			default:
				throw new IllegalStateException("Unknown message id: " + result.getMessageId());
			}

		}

	}

}
