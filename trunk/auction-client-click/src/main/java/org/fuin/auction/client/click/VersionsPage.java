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
import java.util.Date;

import org.apache.click.Page;
import org.apache.click.control.ActionLink;
import org.fuin.auction.command.api.AuctionCommandService;
import org.fuin.auction.common.FailedToLoadProjectInfoException;
import org.fuin.auction.common.Utils;
import org.fuin.auction.query.api.AuctionQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Page displaying the server and application versions.
 */
public class VersionsPage extends Page {

	private static final String ERROR_LOADING_VERSION_INFORMATION = "Error loading version information!";

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(VersionsPage.class);

	private String applicationVersion = "?";

	private String commandServerVersion = "?";

	private String queryServerVersion = "?";

	private String message = "";

	private Date lastLoad = null;

	/**
	 * Default constructor.
	 */
	public VersionsPage() {
		addControl(new ActionLink("load", this, "onLoad"));
	}

	/**
	 * Loads the version data.
	 * 
	 * @return If the loading was successful <code>true</code> else
	 *         <code>false</code>.
	 */
	public final boolean onLoad() {

		lastLoad = new Date();

		try {

			final HessianProxyFactory factory = new HessianProxyFactory();

			applicationVersion = Utils.getProjectInfo(this.getClass(),
			        "/auction-client-click.properties").getVersion();

			final AuctionQueryService queryService = (AuctionQueryService) factory.create(
			        AuctionQueryService.class,
			        "http://localhost:8080/auction-query-server/AuctionQueryService");
			queryServerVersion = queryService.getVersion();

			final AuctionCommandService commandService = (AuctionCommandService) factory.create(
			        AuctionCommandService.class,
			        "http://localhost:8080/auction-command-server/AuctionCommandService");

			// FIXME michael 20.10.2010 Add call to server

			return true;

		} catch (final RuntimeException ex) {
			LOG.error(ERROR_LOADING_VERSION_INFORMATION, ex);
		} catch (final FailedToLoadProjectInfoException ex) {
			LOG.error(ERROR_LOADING_VERSION_INFORMATION, ex);
		} catch (final MalformedURLException ex) {
			LOG.error(ERROR_LOADING_VERSION_INFORMATION, ex);
		}
		return false;

	}

	@Override
	public final void onRender() {
		if (lastLoad == null) {
			onLoad();
		}
		addModel("applicationVersion", applicationVersion);
		addModel("commandServerVersion", commandServerVersion);
		addModel("queryServerVersion", queryServerVersion);
		addModel("message", message);
		addModel("lastLoad", lastLoad);
	}

}
