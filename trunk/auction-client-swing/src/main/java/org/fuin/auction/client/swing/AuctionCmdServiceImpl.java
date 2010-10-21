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

import org.fuin.auction.command.api.AuctionCommandService;
import org.fuin.auction.command.api.ChangePasswordCommand;
import org.fuin.auction.command.api.CommandResult;
import org.fuin.auction.command.api.EmailAlreadyExistException;
import org.fuin.auction.command.api.IdNotFoundException;
import org.fuin.auction.command.api.InternalErrorException;
import org.fuin.auction.command.api.InvalidCommandException;
import org.fuin.auction.command.api.PasswordException;
import org.fuin.auction.command.api.RegisterUserCommand;
import org.fuin.auction.command.api.AggregateIdCommandResult;
import org.fuin.auction.command.api.UserIdAlreadyExistException;
import org.fuin.auction.command.api.UserIdEmailCombinationAlreadyExistException;
import org.fuin.auction.command.api.VerificationFailedException;
import org.fuin.auction.command.api.VerifyUserCommand;

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
			final AggregateIdCommandResult rucr = (AggregateIdCommandResult) result;
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

		final ChangePasswordCommand cmd = new ChangePasswordCommand(userAggregateId, oldPassword,
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
	        throws IdNotFoundException, VerificationFailedException, InternalErrorException,
	        InvalidCommandException {

		final VerifyUserCommand cmd = new VerifyUserCommand(userAggregateId, securityToken);
		final CommandResult result = commandService.send(cmd);
		if (!result.isSuccess()) {

			// Error handling
			switch (result.getMessageId()) {
			case IdNotFoundException.MESSAGE_ID:
				throw new IdNotFoundException(result.getInternalMessage());
			case VerificationFailedException.MESSAGE_ID:
				throw new VerificationFailedException();
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
