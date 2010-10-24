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
package org.fuin.auction.command.test;

import java.io.Serializable;

import org.fuin.auction.common.Versioned;

//TESTCODE:BEGIN
public final class ExampleCommand implements Serializable, Versioned {

	private static final long serialVersionUID = 8394404393474935818L;

	private static final int VERSION = 3;

	private int version;

	private int id;

	private String firstName;

	private String lastName;

	public ExampleCommand(final int id, final String firstName, final String lastName) {
		super();
		this.id = id;
		this.version = VERSION;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public final int getId() {
		return id;
	}

	public final void setId(final int id) {
		this.id = id;
	}

	public final String getFirstName() {
		return firstName;
	}

	public final void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public final String getLastName() {
		return lastName;
	}

	public final void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@Override
	public final int getClassVersion() {
		return VERSION;
	}

	@Override
	public final int getInstanceVersion() {
		return version;
	}

	@Override
	public final boolean isSameVersion() {
		return VERSION == version;
	}

}
// TESTCODE:END
