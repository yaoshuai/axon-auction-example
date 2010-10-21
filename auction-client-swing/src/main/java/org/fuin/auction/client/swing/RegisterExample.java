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

import org.fuin.auction.command.api.UserIdAlreadyExistException;
import org.fuin.auction.common.Utils;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Temporary test class to check command server connection.
 */
public final class RegisterExample {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private RegisterExample() {
		throw new UnsupportedOperationException("You cannot create an instance of a utility class!");
	}

	/**
	 * Dummy test method.
	 * 
	 * @param args
	 *            Not used.
	 * 
	 * @throws Exception
	 *             Some error happened.
	 */
	public static void main(final String[] args) throws Exception {

		System.out.println("java.io.tmpdir=" + System.getProperty("java.io.tmpdir"));

		final HessianProxyFactory factory = new HessianProxyFactory();

		System.out.println("Application: "
		        + Utils.getProjectInfo(RegisterExample.class, "/auction-client-swing.properties")
		                .getVersion());

		final AuctionCmdService cmdService = new AuctionCmdServiceImpl(factory,
		        "http://localhost:8080/auction-command-server/AuctionCommandService");

		// Add three users
		final String peter1Pw = "12345678";
		final String peter1Id = cmdService.registerUser("peter1", peter1Pw, "peter1@nowhere.com");
		System.out.println("peter1=" + peter1Id);
		System.out.println("peter2="
		        + cmdService.registerUser("peter2", "90123456", "peter2@nowhere.com"));
		System.out.println("peter3="
		        + cmdService.registerUser("peter3", "78901234", "peter3@nowhere.com"));

		// Error because of a duplicate user id
		try {
			cmdService.registerUser("peter3", "12345678", "peter@nowhere.com");
		} catch (final UserIdAlreadyExistException ex) {
			System.out.println(ex.getMessage());
		}

		// Change password for peter1
		cmdService.changePassword(peter1Id, peter1Pw, "abc123def");
		System.out.println("Password for peter1 changed!");

	}

}
