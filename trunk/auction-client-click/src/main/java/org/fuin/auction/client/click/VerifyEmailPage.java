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

import java.util.UUID;

import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.util.Bindable;
import org.fuin.auction.command.api.extended.IdNotFoundException;
import org.fuin.auction.command.api.extended.InvalidCommandException;
import org.fuin.auction.command.api.extended.UserEmailVerificationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page for verifying the email address.
 */
public final class VerifyEmailPage extends AuctionPage {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(VerifyEmailPage.class);

	@Bindable
	protected String userId;

	@Bindable
	protected String token;

	@Bindable
	protected String msg;

	@Bindable
	protected Form form = new Form();

	/**
	 * Default constructor.
	 */
	public VerifyEmailPage() {
		super("Verify Email");

		final TextField tfUserId = new TextField("userId", "User ID", true);
		tfUserId.setSize(50);
		form.add(tfUserId);

		final TextField tfToken = new TextField("securityToken", "Security Token", true);
		tfToken.setSize(50);
		form.add(tfToken);

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
			getCmdService().verifyUserEmail(getUserAggregateId(), getSecurityToken());
			form.clearErrors();
			form.clearValues();
			msg = getMessage("Success");
			return true;
		} catch (final InvalidCommandException ex) {
			form.setError(ex.getMessage());
		} catch (final IdNotFoundException ex) {
			form.setError(getMessage("IdNotFound"));
		} catch (final UserEmailVerificationFailedException ex) {
			form.setError(getMessage("UserEmailVerificationFailed"));
		} catch (final RuntimeException ex) {
			final String msg = getMessage("InternalError");
			LOG.error(msg, ex);
			form.setError(msg);
		}
		return false;

	}

	@Override
	public void onRender() {
		super.onRender();
		if ((userId != null) && ((getUserId() == null) || (getUserId().length() == 0))) {
			setUserId(userId);
		}
		if ((token != null) && ((getSecurityToken() == null) || (getSecurityToken().length() == 0))) {
			setSecurityToken(token);
		}
	}

	// TODO michael 07.11.2010 Handle form validation with JSR-303
	// We need to get rid of this dumb mapping between command and form fields!

	private String getSecurityToken() {
		return form.getField("securityToken").getValue();
	}

	private void setSecurityToken(final String securityToken) {
		form.getField("securityToken").setValue(securityToken);
	}

	private String getUserId() {
		return form.getField("userId").getValue();
	}

	private void setUserId(final String userId) {
		form.getField("userId").setValue(userId);
	}

	private UUID getUserAggregateId() throws IdNotFoundException {
		final String str = getUserId();
		if (str == null) {
			return null;
		}
		try {
			return UUID.fromString(str);
		} catch (final IllegalArgumentException ex) {
			throw new IdNotFoundException("No valid UUID: " + str);
		}
	}

}
