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

import org.fuin.auction.command.api.EmailAlreadyExistException;
import org.fuin.auction.command.api.IdNotFoundException;
import org.fuin.auction.command.api.InternalErrorException;
import org.fuin.auction.command.api.InvalidCommandException;
import org.fuin.auction.command.api.PasswordException;
import org.fuin.auction.command.api.UserIdAlreadyExistException;
import org.fuin.auction.command.api.UserIdEmailCombinationAlreadyExistException;
import org.fuin.auction.command.api.UserEmailVerificationFailedException;

/**
 * This is a convenience layer that maps the result codes from the original
 * command service back to exceptions and defines explicit methods instead of
 * using command objects.
 */
public interface AuctionCmdService {

	/**
	 * Register a user.
	 * 
	 * @param userId
	 *            User ID.
	 * @param password
	 *            Password.
	 * @param email
	 *            Email address.
	 * 
	 * @return New unique user id.
	 * 
	 * @throws UserIdEmailCombinationAlreadyExistException
	 *             The user/email combination is already registered.
	 * @throws UserIdAlreadyExistException
	 *             The user id is already registered for another user.
	 * @throws EmailAlreadyExistException
	 *             The email is already assigned to another user.
	 * @throws InvalidCommandException
	 *             The data sent is not valid.
	 * @throws InternalErrorException
	 *             Internal error.
	 */
	public String registerUser(String userId, String password, String email)
	        throws UserIdEmailCombinationAlreadyExistException, UserIdAlreadyExistException,
	        EmailAlreadyExistException, InternalErrorException, InvalidCommandException;

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
	 * @throws InvalidCommandException
	 *             The data sent is not valid.
	 * @throws InternalErrorException
	 *             Internal error.
	 */
	public void changePassword(String userAggregateId, String oldPassword, String newPassword)
	        throws IdNotFoundException, PasswordException, InternalErrorException,
	        InvalidCommandException;

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
	 * @throws InvalidCommandException
	 *             The data sent is not valid.
	 * @throws InternalErrorException
	 *             Internal error.
	 */
	public void verifyUser(String userAggregateId, String securityToken)
	        throws IdNotFoundException, UserEmailVerificationFailedException, InternalErrorException,
	        InvalidCommandException;

}
