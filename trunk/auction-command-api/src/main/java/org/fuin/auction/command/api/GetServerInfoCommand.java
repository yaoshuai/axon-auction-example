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
package org.fuin.auction.command.api;

/**
 * Register a new user.
 */
public final class GetServerInfoCommand extends AbstractCommand<GetServerInfoCommandResult> {

	private static final long serialVersionUID = 5381295115581408651L;

	private static final int VERSION = 1;

	/**
	 * Default constructor.
	 */
	public GetServerInfoCommand() {
		super();
	}

	@Override
	public final int getVersion() {
		return VERSION;
	}

	@Override
	public final GetServerInfoCommandResult internalError(final String message) {
		return new GetServerInfoCommandResult().internalError(message);
	}

	@Override
	public final GetServerInfoCommandResult invalidCommand(final String message) {
		return new GetServerInfoCommandResult().invalidCommand(message);
	}

}
