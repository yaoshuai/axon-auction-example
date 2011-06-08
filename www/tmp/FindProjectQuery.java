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
package org.fuin.metacqrs.query.api.findProject;

import java.util.UUID;

import org.fuin.metacqrs.query.api.base.MetaCqrsQuery;

/**
 * Query to return a project by it's UUID.
 */
public interface FindProjectQuery extends MetaCqrsQuery {

    /**
     * Find a project by it's UUID.
     * 
     * @param projectUUID
     *            UUID of the project to find.
     * 
     * @return Type.
     */
    public FindProjectResult findProject(UUID projectUUID);

}
