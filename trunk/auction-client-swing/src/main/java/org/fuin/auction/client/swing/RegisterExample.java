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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.fuin.auction.command.api.exceptions.AuctionCmdService;
import org.fuin.auction.command.api.exceptions.UserNameEmailCombinationAlreadyExistException;
import org.fuin.auction.common.Utils;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Quick'n dirty example usage.
 */
public final class RegisterExample {

	private static final int MINUTE = 60;

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

		// Display temp directory
		final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		System.out.println("java.io.tmpdir=" + tmpDir);

		// Delete old mail if it exists
		final String peterEmail = "peter@nowhere.com";
		final File mailFile = new File(tmpDir, peterEmail);
		if (mailFile.exists()) {
			if (!mailFile.delete()) {
				throw new RuntimeException("Cannot delete: " + mailFile);
			}
		}

		// Start example
		final HessianProxyFactory factory = new HessianProxyFactory();

		System.out.println("Application: "
		        + Utils.getProjectInfo(RegisterExample.class, "/auction-client-swing.properties")
		                .getVersion());

		final AuctionCmdService cmdService = new AuctionCmdServiceImpl(factory,
		        "http://localhost:8080/auction-command-server/AuctionCommandService");

		// Add user
		final String peterName = "peter";
		final String peterPw = "12345678";
		final String peterId = cmdService.registerUser(peterName, peterPw, peterEmail);
		System.out.println("REGISTERED " + peterName + "=" + peterId);

		// Test duplicate registration
		try {
			cmdService.registerUser(peterName, peterPw, peterEmail);
		} catch (final UserNameEmailCombinationAlreadyExistException ex) {
			System.out.println("REGISTERING AGAIN: " + ex.getMessage());
		}

		// Change password for peter
		cmdService.changeUserPassword(peterId, peterPw, "abc123def");
		System.out.println("CHANGE PASSWORD: " + peterName);

		// Wait for the welcome mail to arrive
		int secondsWaited = 0;
		while ((secondsWaited < MINUTE) && !mailFile.exists()) {
			sleepSeconds(1);
			secondsWaited++;
		}
		if (!mailFile.exists()) {
			throw new RuntimeException("Waited more than one minute...");
		}

		final String message = readMessage(mailFile);
		final String securityToken = extractToken(message, "?token=", "\"");
		System.out.println("securityToken: " + securityToken);

		cmdService.verifyUserEmail(peterId, securityToken);
		System.out.println("VERFIED USER EMAIL: " + peterName);

	}

	private static void sleepSeconds(final int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (final InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static String readMessage(final File file) throws IOException {
		final StringBuffer sb = new StringBuffer();
		final LineNumberReader reader = new LineNumberReader(new FileReader(file));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			reader.close();
		}
		return sb.toString();
	}

	private static String extractToken(final String message, final String startStr,
	        final String stopStr) {

		final int start = message.indexOf(startStr);
		if (start == -1) {
			throw new IllegalStateException("Cannot find '" + startStr + "'!");
		}
		final int stop = message.indexOf(stopStr, start + 1);
		if (stop == -1) {
			throw new IllegalStateException("Cannot find '" + stopStr + "'!");
		}
		return message.substring(start + startStr.length(), stop);
	}

}
