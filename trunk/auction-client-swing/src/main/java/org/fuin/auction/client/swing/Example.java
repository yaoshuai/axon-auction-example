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

import org.fuin.auction.command.api.AuctionCommandService;
import org.fuin.auction.command.api.GetServerInfoCommand;
import org.fuin.auction.command.api.GetServerInfoCommandResult;
import org.fuin.auction.command.api.RegisterUserCommand;
import org.fuin.auction.command.api.RegisterUserCommandResult;
import org.fuin.auction.common.FailedToLoadProjectInfoException;
import org.fuin.auction.common.Utils;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Temporary test class to check server connection.
 */
public final class Example {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private Example() {
		throw new UnsupportedOperationException("You cannot create an instance of a utility class!");
	}

	/**
	 * Dummy test method.
	 * 
	 * @param args
	 *            Not used.
	 * 
	 * @throws MalformedURLException
	 *             Should never happen.
	 * @throws FailedToLoadProjectInfoException
	 *             Should never happen.
	 */
	public static void main(final String[] args) throws MalformedURLException,
	        FailedToLoadProjectInfoException {

		System.out.println("java.io.tmpdir=" + System.getProperty("java.io.tmpdir"));

		final HessianProxyFactory factory = new HessianProxyFactory();

		System.out.println("Application: "
		        + Utils.getProjectInfo(Example.class, "/auction-client-swing.properties")
		                .getVersion());

		final AuctionCommandService commandService = (AuctionCommandService) factory.create(
		        AuctionCommandService.class,
		        "http://localhost:8080/auction-command-server/AuctionCommandService");
		final GetServerInfoCommandResult info = commandService.send(new GetServerInfoCommand());
		System.out.println("Server-Info: " + info);

		// Add three user
		System.out.println("Register: "
		        + register(commandService, "peter1", "12345678", "peter1@nowhere.com"));
		System.out.println("Register: "
		        + register(commandService, "peter2", "12345678", "peter2@nowhere.com"));
		System.out.println("Register: "
		        + register(commandService, "peter3", "12345678", "peter3@nowhere.com"));

		// Error because of a duplicate user id
		System.out.println("Register: "
		        + register(commandService, "peter3", "12345678", "peter@nowhere.com"));

		// final AuctionQueryService queryService = (AuctionQueryService)
		// factory.create(
		// AuctionQueryService.class,
		// "http://localhost:8080/auction-query-server/AuctionQueryService");
		// System.out.println("Query-Server: " + queryService.getVersion());

	}

	private static RegisterUserCommandResult register(final AuctionCommandService commandService,
	        final String userId, final String password, final String email) {
		final RegisterUserCommand cmd = new RegisterUserCommand();
		cmd.setUserId(userId);
		cmd.setPassword(password);
		cmd.setEmail(email);
		return commandService.send(cmd);
	}

}
