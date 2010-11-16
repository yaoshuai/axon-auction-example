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
package org.fuin.auction.command.api.base;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.auction.command.api.extended.InternalErrorException;
import org.fuin.auction.command.api.extended.InvalidCommandException;
import org.fuin.auction.command.api.extended.UserEmailAlreadyExistException;
import org.fuin.auction.command.api.extended.UserNameAlreadyExistException;
import org.fuin.auction.command.api.support.Command;
import org.fuin.auction.command.api.support.CommandException;
import org.fuin.objects4j.Contract;
import org.fuin.objects4j.Label;
import org.fuin.objects4j.TextField;
import org.fuin.objects4j.validation.EmailAddressStr;
import org.fuin.objects4j.validation.PasswordStr;
import org.fuin.objects4j.validation.UserNameStr;

/**
 * Register a new user.
 */
@Label("New User Registration")
public final class RegisterUserCommand implements Command {

	private static final long serialVersionUID = 5381295115581408651L;

	@NotNull
	@UserNameStr
	@Label("User name")
	@TextField(width = 50)
	private String userName;

	@NotNull
	@PasswordStr
	@Label("Password")
	@TextField(width = 50)
	private String password;

	@NotNull
	@Size(min = 1, max = 320)
	@EmailAddressStr
	@Label("Email address")
	@TextField(width = 50)
	private String email;

	/**
	 * Default constructor for serialization.
	 */
	protected RegisterUserCommand() {
		super();
	}

	/**
	 * Constructor with all attributes.
	 * 
	 * @param userName
	 *            User ID.
	 * @param password
	 *            Password.
	 * @param email
	 *            Email address.
	 */
	public RegisterUserCommand(final String userName, final String password, final String email) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		Contract.requireValid(this);
	}

	/**
	 * Returns the user name.
	 * 
	 * @return User name.
	 */
	public final String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name to a new value.
	 * 
	 * @param userName
	 *            User name to set.
	 */
	public final void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * Returns the password.
	 * 
	 * @return Password.
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * Sets the password to a new value.
	 * 
	 * @param password
	 *            Password to set.
	 */
	public final void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Returns the email address.
	 * 
	 * @return Email address.
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * Sets the email address to a new value.
	 * 
	 * @param email
	 *            Email address to set.
	 */
	public final void setEmail(final String email) {
		this.email = email;
	}

	@Override
	public final String toTraceString() {
		// We don't want to include the clear text password for security
		// reasons here
		return new ToStringBuilder(this).append("userName", userName).append("email", email)
		        .toString();
	}

	@Override
	public final List<Class<? extends CommandException>> getExceptions() {
		final List<Class<? extends CommandException>> list;
		list = new ArrayList<Class<? extends CommandException>>();
		list.add(UserNameAlreadyExistException.class);
		list.add(UserEmailAlreadyExistException.class);
		list.add(InvalidCommandException.class);
		list.add(InternalErrorException.class);
		return list;
	}

}
