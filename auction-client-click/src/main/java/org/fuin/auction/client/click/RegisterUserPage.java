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
package org.fuin.auction.client.click;

import org.apache.click.control.Form;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.util.Bindable;
import org.fuin.auction.command.api.extended.InvalidCommandException;
import org.fuin.auction.command.api.extended.UserEmailAlreadyExistException;
import org.fuin.auction.command.api.extended.UserNameAlreadyExistException;
import org.fuin.auction.command.api.extended.UserNameEmailCombinationAlreadyExistException;
import org.fuin.objects4j.ContractViolationException;
import org.fuin.objects4j.EmailAddress;
import org.fuin.objects4j.Password;
import org.fuin.objects4j.UserName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Register new user page.
 */
public class RegisterUserPage extends AuctionPage {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(RegisterUserPage.class);

	@Bindable
	protected Form form = new Form();

	@Bindable
	protected String msg;

	/**
	 * Default constructor.
	 */
	public RegisterUserPage() {
		super("New User Registration");

		final TextField tfUserName = new TextField("userName", "User Name", true);
		tfUserName.setMinLength(3);
		tfUserName.setMaxLength(20);
		form.add(tfUserName);

		final TextField tfPassword = new PasswordField("password", "Password", true);
		tfPassword.setMinLength(8);
		tfPassword.setMaxLength(20);
		form.add(tfPassword);

		final TextField tfEmail = new TextField("email", "Email Address", true);
		tfPassword.setMinLength(3);
		tfPassword.setMaxLength(300);
		tfEmail.setSize(50);
		form.add(tfEmail);

		form.add(new Submit("ok", "  OK  ", this, "onOkClick"));
	}

	/**
	 * Submits the data.
	 * 
	 * @return If the action was successful <code>true</code> else
	 *         <code>false</code>.
	 */
	public final boolean onOkClick() {

		if (!form.isValid()) {
			return true;
		}

		try {
			getCmdService().registerUser(getUserName(), getPassword(), getEmail());
			form.clearErrors();
			form.clearValues();
			msg = getMessage("Success");
		} catch (final ContractViolationException ex) {
			// TODO michael 07.11.2010 Handle form validation with JSR-303
			form.setError(ex.getMessage());
		} catch (final InvalidCommandException ex) {
			form.setError(ex.getMessage());
		} catch (final UserNameEmailCombinationAlreadyExistException ex) {
			form.setError(getMessage("UserNameEmailCombinationAlreadyExist"));
		} catch (final UserNameAlreadyExistException ex) {
			form.setError(getMessage("UserNameAlreadyExist"));
		} catch (final UserEmailAlreadyExistException ex) {
			form.setError(getMessage("UserEmailAlreadyExist"));
		} catch (final RuntimeException ex) {
			final String msg = getMessage("InternalError");
			LOG.error(msg, ex);
			form.setError(msg);
		}

		return false;

	}

	// TODO michael 07.11.2010 Handle form validation with JSR-303

	private UserName getUserName() {
		return new UserName(form.getField("userName").getValue());
	}

	private Password getPassword() {
		return new Password(form.getField("password").getValue());
	}

	private EmailAddress getEmail() {
		return new EmailAddress(form.getField("email").getValue());
	}

}
