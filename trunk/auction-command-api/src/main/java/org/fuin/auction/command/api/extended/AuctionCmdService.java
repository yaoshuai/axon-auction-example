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
package org.fuin.auction.command.api.extended;

import java.util.UUID;

import org.fuin.auction.command.api.base.AuctionCommandService;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.Password;
import org.fuin.objects4j.UserName;

/**
 * Maps the result codes from the original {@link AuctionCommandService} back to
 * exceptions and defines explicit methods instead of using command objects.<br>
 * <br>
 * Every method maps 1:1 to a command with the same name and the suffix
 * "Command". The method's arguments map 1:1 to the command's attributes.<br>
 * <br>
 * <b>This interface is NOT implemented by the command server!</b> To use it
 * there has to be an implementation that does the mapping.<br>
 * <br>
 * Every method may throw {@link InvalidCommandException} or
 * {@link InternalErrorException} as two special runtime exceptions.
 */
public interface AuctionCmdService {

	/**
	 * Register a user.
	 * 
	 * @param userName
	 *            User name.
	 * @param password
	 *            Password.
	 * @param email
	 *            Email address.
	 * 
	 * @return New unique user id.
	 * 
	 * @throws UserNameEmailCombinationAlreadyExistException
	 *             The user/email combination is already registered.
	 * @throws UserNameAlreadyExistException
	 *             The user id is already registered for another user.
	 * @throws UserEmailAlreadyExistException
	 *             The email is already assigned to another user.
	 */
	public UUID registerUser(UserName userName, Password password, EmailAddress email)
	        throws UserNameEmailCombinationAlreadyExistException, UserNameAlreadyExistException,
	        UserEmailAlreadyExistException;

	/**
	 * Change the user's password.
	 * 
	 * @param userAggregateId
	 *            User reference.
	 * @param oldPassword
	 *            Old clear text password.
	 * @param newPassword
	 *            New clear text password.
	 * 
	 * @throws IdNotFoundException
	 *             The user with the aggregate id is unknown.
	 * @throws PasswordException
	 *             The old password was wrong.
	 */
	public void changeUserPassword(UUID userAggregateId, Password oldPassword, Password newPassword)
	        throws IdNotFoundException, PasswordException;

	/**
	 * Verify the user.
	 * 
	 * @param userAggregateId
	 *            User reference.
	 * @param securityToken
	 *            Security token.
	 * 
	 * @throws IdNotFoundException
	 *             The user with the aggregate id is unknown.
	 * @throws UserEmailVerificationFailedException
	 *             The given security token was wrong.
	 */
	public void verifyUserEmail(UUID userAggregateId, String securityToken)
	        throws IdNotFoundException, UserEmailVerificationFailedException;

}
