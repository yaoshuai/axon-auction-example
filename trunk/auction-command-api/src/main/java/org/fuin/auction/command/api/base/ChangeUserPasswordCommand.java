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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.auction.command.api.support.Command;
import org.fuin.objects4j.validation.PasswordStr;
import org.fuin.objects4j.validation.UUIDStr;

/**
 * Change the user's password.
 */
public final class ChangeUserPasswordCommand implements Command {

	private static final long serialVersionUID = -7557765676459176985L;

	private static final int VERSION = 1;

	@NotNull
	@UUIDStr
	private String userAggregateId;

	@NotNull
	@PasswordStr
	private String oldPassword;

	@NotNull
	@PasswordStr
	private String newPassword;

	@Override
	public final int getVersion() {
		return VERSION;
	}

	/**
	 * Constructor with all attributes.
	 * 
	 * @param userAggregateId
	 *            Aggregate id of the user.
	 * @param oldPassword
	 *            Old clear text password.
	 * @param newPassword
	 *            New clear text password.
	 */
	public ChangeUserPasswordCommand(final String userAggregateId, final String oldPassword,
	        final String newPassword) {
		super();
		this.userAggregateId = userAggregateId;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	/**
	 * Returns the aggregate id of the user.
	 * 
	 * @return Unique id.
	 */
	public final String getUserAggregateId() {
		return userAggregateId;
	}

	/**
	 * Sets the aggregate id of the user.
	 * 
	 * @param userAggregateId
	 *            Unique id to set.
	 */
	public final void setUserAggregateId(final String userAggregateId) {
		this.userAggregateId = userAggregateId;
	}

	/**
	 * Returns the old password.
	 * 
	 * @return Old clear text password.
	 */
	public final String getOldPassword() {
		return oldPassword;
	}

	/**
	 * Sets the old password.
	 * 
	 * @param oldPassword
	 *            Clear text password to set.
	 */
	public final void setOldPassword(final String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * Returns the new password.
	 * 
	 * @return New clear text password.
	 */
	public final String getNewPassword() {
		return newPassword;
	}

	/**
	 * Sets the new password.
	 * 
	 * @param newPassword
	 *            Clear text password to set.
	 */
	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public final String toTraceString() {
		// We don't want to include the clear text passwords for security
		// reasons here
		return new ToStringBuilder(this).append("userAggregateId", userAggregateId).append(
		        "version", getVersion()).toString();
	}

}
