/**
 * Copyright (C) 2011 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.metacqrs.query.server.lib.manager;

import javax.inject.Inject;
import javax.inject.Named;

import org.axonframework.eventhandling.FullConcurrencyPolicy;
import org.axonframework.eventhandling.annotation.AsynchronousEventListener;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.fuin.metacqrs.command.server.lib.event.project.ProjectCreatedEvent;
import org.fuin.metacqrs.command.server.lib.event.project.ProjectRemovedEvent;
import org.fuin.metacqrs.query.api.findProject.FindProjectQuery;
import org.fuin.metacqrs.query.api.findProject.FindProjectResult;
import org.fuin.metacqrs.query.server.lib.base.InternalQueryService;
import org.fuin.metacqrs.query.server.lib.base.ManagerOfQuery;
import org.fuin.metacqrs.query.server.lib.base.QueryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listens to events relevant for the {@link FindProjectQuery} and populates the
 * data storage with that information.
 */
@Named
@AsynchronousEventListener(sequencingPolicyClass = FullConcurrencyPolicy.class)
@ManagerOfQuery(FindProjectQuery.class)
public class FindProjectQueryManager implements QueryManager {

    private static final Logger LOG = LoggerFactory.getLogger(FindProjectQueryManager.class);

    @Inject
    private InternalQueryService service;

    /**
     * Handles the event.
     * 
     * @param event
     *            Event to handle.
     */
    @EventHandler
    public void handle(final ProjectCreatedEvent event) {
        LOG.debug("handle(ProjectCreatedEvent)");

        final FindProjectResult project = new FindProjectResult(event.getProjectUUID(), event
                .getName());
        service
                .objectAddOrUpdate(project, FindProjectQuery.class.getName(), event
                        .getProjectUuid());

    }

    /**
     * Handles the event.
     * 
     * @param event
     *            Event to handle.
     */
    @EventHandler
    public void handle(final ProjectRemovedEvent event) {
        LOG.debug("handle(ProjectRemovedEvent)");

        service.deleteAll(FindProjectQuery.class.getName(), event.getProjectUuid());

    }

    @Override
    public void deleteData() {
        LOG.debug("deleteData()");
        service.deleteAll(FindProjectQuery.class.getName());
    }

}
