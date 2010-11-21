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
import java.util.UUID;

import org.fuin.auction.command.api.base.AggregateIdentifierUUIDResult;
import org.fuin.auction.command.api.base.AuctionCommandService;
import org.fuin.auction.command.api.base.ChangeUserPasswordCommand;
import org.fuin.auction.command.api.base.RegisterUserCommand;
import org.fuin.auction.command.api.base.ResultCode;
import org.fuin.auction.command.api.base.VerifyUserEmailCommand;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.objects4j.Contract;
import org.fuin.objects4j.ContractViolationException;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.Password;
import org.fuin.objects4j.UserName;

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
	public final UUID registerUser(final UserName userName, final Password password,
	        final EmailAddress email) throws UserNameEmailCombinationAlreadyExistsException,
	        UserNameAlreadyExistsException, UserEmailAlreadyExistsException {

		final RegisterUserCommand cmd = new RegisterUserCommand(userName.toString(), password
		        .toString(), email.toString());

		final CommandResult result = commandService.send(cmd);
		if (result.isSuccess()) {
			final AggregateIdentifierUUIDResult rucr = (AggregateIdentifierUUIDResult) result;
			return UUID.fromString(rucr.getId());
		}

		// Error handling
		if (!ResultCode.exists(result.getCode())) {
			throw new IllegalStateException("Unknown code: " + result.getCode());
		}
		final ResultCode resultCode = ResultCode.forCode(result.getCode());
		switch (resultCode) {
		case DUPLICATE_USERNAME_EMAIL_COMBINATION:
			throw new UserNameEmailCombinationAlreadyExistsException();
		case DUPLICATE_USERNAME:
			throw new UserNameAlreadyExistsException();
		case DUPLICATE_EMAIL:
			throw new UserEmailAlreadyExistsException();
		case INVALID_COMMAND:
			throw new RuntimeException(result.getText());
		case INTERNAL_ERROR:
			throw new RuntimeException(result.getText());
		default:
			throw new IllegalStateException("Unhandled code: " + result.getCode());
		}

	}

	@Override
	public final void changeUserPassword(final UUID userAggregateId, final Password oldPassword,
	        final Password newPassword) throws IdNotFoundException, PasswordException {

		final ChangeUserPasswordCommand cmd = new ChangeUserPasswordCommand(userAggregateId
		        .toString(), oldPassword.toString(), newPassword.toString());

		final CommandResult result = commandService.send(cmd);
		if (!result.isSuccess()) {

			// Error handling
			if (!ResultCode.exists(result.getCode())) {
				throw new IllegalStateException("Unknown code: " + result.getCode());
			}
			final ResultCode resultCode = ResultCode.forCode(result.getCode());
			switch (resultCode) {
			case ID_NOT_FOUND:
				throw new IdNotFoundException();
			case PASSWORD_WRONG:
				throw new PasswordException();
			case INVALID_COMMAND:
				throw new RuntimeException(result.getText());
			case INTERNAL_ERROR:
				throw new RuntimeException(result.getText());
			default:
				throw new IllegalStateException("Unhandled code: " + result.getCode());
			}

		}

	}

	@Override
	public final void verifyUserEmail(final UUID userAggregateId, final String securityToken)
	        throws IdNotFoundException, UserEmailVerificationFailedException {

		// TODO michael 07.11.2010 Handle the checks more nicely!
		// The above arguments are a 1:1 mapping from the fields of the command
		// and so the same validations should be checked here BEFORE the command
		// is created. The error message should fit the fact that it's not a
		// command field but an argument that is checked.

		Contract.requireArgNotNull("userAggregateId", userAggregateId);
		Contract.requireArgNotNull("securityToken", securityToken);

		final VerifyUserEmailCommand cmd;
		try {
			cmd = new VerifyUserEmailCommand(userAggregateId.toString(), securityToken);
		} catch (final ContractViolationException ex) {
			throw new RuntimeException(ex.getMessage());
		}

		final CommandResult result = commandService.send(cmd);
		if (!result.isSuccess()) {

			// Error handling
			if (!ResultCode.exists(result.getCode())) {
				throw new IllegalStateException("Unknown code: " + result.getCode());
			}
			final ResultCode resultCode = ResultCode.forCode(result.getCode());
			switch (resultCode) {
			case ID_NOT_FOUND:
				throw new IdNotFoundException();
			case USER_EMAIL_VERIFICATION_FAILED:
				throw new UserEmailVerificationFailedException();
			case INVALID_COMMAND:
				throw new RuntimeException(result.getText());
			case INTERNAL_ERROR:
				throw new RuntimeException(result.getText());
			default:
				throw new IllegalStateException("Unhandled code: " + result.getCode());
			}

		}

	}

}
