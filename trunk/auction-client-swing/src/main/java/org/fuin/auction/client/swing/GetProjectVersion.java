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
import org.fuin.auction.common.FailedToLoadProjectInfoException;
import org.fuin.auction.common.Utils;
import org.fuin.auction.query.api.AuctionQueryService;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Temporary test class to check server connection.
 */
public final class GetProjectVersion {

    /**
     * Private constructor to avoid instantiation.
     */
    private GetProjectVersion() {
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

        System.out.println(System.getProperty("java.io.tmpdir"));

        final HessianProxyFactory factory = new HessianProxyFactory();

        System.out.println("Application: "
                + Utils.getProjectInfo(GetProjectVersion.class, "/auction-client-swing.properties")
                        .getVersion());

        final AuctionQueryService queryService = (AuctionQueryService) factory.create(
                AuctionQueryService.class,
                "http://localhost:8080/auction-query-server/AuctionQueryService");
        System.out.println("Query-Server: " + queryService.getVersion());

        final AuctionCommandService commandService = (AuctionCommandService) factory.create(
                AuctionCommandService.class,
                "http://localhost:8080/auction-command-server/AuctionCommandService");
        System.out.println("Command-Server: " + commandService.getVersion());

    }

}
