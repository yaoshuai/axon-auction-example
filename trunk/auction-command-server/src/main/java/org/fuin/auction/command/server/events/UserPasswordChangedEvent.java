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
package org.fuin.auction.command.server.events;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.axonframework.domain.DomainEvent;
import org.fuin.auction.command.server.support.ExtendedDomainEvent;
import org.fuin.objects4j.PasswordSha512;

/**
 * A user was created.
 */
public final class UserPasswordChangedEvent extends DomainEvent implements ExtendedDomainEvent {

	private static final long serialVersionUID = -4632580190706565637L;

	private static final int VERSION = 1;

	/** Version to be serialized. */
	private int instanceVersion;

	private final PasswordSha512 oldPassword;

	private final PasswordSha512 newPassword;

	/**
	 * Constructor with all data.
	 * 
	 * @param oldPassword
	 *            Old SHA-512 hashed password.
	 * @param newPassword
	 *            New SHA-512 hashed password.
	 */
	public UserPasswordChangedEvent(final PasswordSha512 oldPassword,
	        final PasswordSha512 newPassword) {
		super();
		this.instanceVersion = VERSION;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	/**
	 * Returns the old hashed password.
	 * 
	 * @return SHA-512 password hash.
	 */
	public final PasswordSha512 getOldPassword() {
		return oldPassword;
	}

	/**
	 * Returns the new hashed password.
	 * 
	 * @return SHA-512 password hash.
	 */
	public final PasswordSha512 getNewPassword() {
		return newPassword;
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("oldPassword", oldPassword).append("newPassword",
		        newPassword).append("version", getInstanceVersion()).toString();
	}

	@Override
	public final int getInstanceVersion() {
		return instanceVersion;
	}

	@Override
	public final int getClassVersion() {
		return VERSION;
	}

	@Override
	public final boolean isSameVersion() {
		return VERSION == instanceVersion;
	}

}
