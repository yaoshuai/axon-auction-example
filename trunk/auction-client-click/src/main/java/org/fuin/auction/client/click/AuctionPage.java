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

import java.net.MalformedURLException;

import org.apache.click.Page;
import org.fuin.auction.client.common.AuctionCmdService;
import org.fuin.auction.client.common.AuctionCmdServiceImpl;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Base page layout for all application pages.
 */
public abstract class AuctionPage extends Page {

	private static final long serialVersionUID = 1L;

	private final AuctionCmdService cmdService;

	/**
	 * Constructor with page title.
	 * 
	 * @param title
	 *            Page title.
	 */
	public AuctionPage(final String title) {
		super();
		addModel("title", title);

		// TODO michael 07.11.2010 Refactor when switching to Spring
		try {
			cmdService = new AuctionCmdServiceImpl(new HessianProxyFactory(),
			        "http://localhost:8080/auction-command-server/AuctionCommandService");
		} catch (final MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public final String getTemplate() {
		return "/auction-template.htm";
	}

	/**
	 * Returns the command service.
	 * 
	 * @return Command service.
	 */
	public final AuctionCmdService getCmdService() {
		return cmdService;
	}

}
