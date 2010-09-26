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
package org.fuin.auction.command.server;

import org.fuin.auction.command.api.AuctionCommandService;
import org.fuin.auction.common.FailedToLoadProjectInfoException;
import org.fuin.auction.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the {@link AuctionCommandService}.
 */
public class AuctionCommandServiceImpl implements AuctionCommandService {

    private static final Logger LOG = LoggerFactory.getLogger(AuctionCommandServiceImpl.class);

    @Override
    public final String getVersion() {
        try {
            return Utils.getProjectInfo(this.getClass(), "/auction-command-server.properties")
                    .getVersion();
        } catch (final FailedToLoadProjectInfoException ex) {
            final String message = "Cannot retrieve version!";
            LOG.error(message, ex);
            throw new RuntimeException(message);
        }
    }

}
