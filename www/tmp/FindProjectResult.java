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

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.objects4j.TraceStringCapable;
import org.fuin.objects4j.validation.NotEmpty;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Type object.
 */
@XStreamAlias("find-project-result")
public final class FindProjectResult implements Serializable, TraceStringCapable,
        Comparable<FindProjectResult> {

    private static final long serialVersionUID = 0L;

    @XStreamAsAttribute
    @NotEmpty
    private String uuid;

    @XStreamAsAttribute
    @NotEmpty
    private String name;

    /**
     * Default constructor for deserialization.
     */
    protected FindProjectResult() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param uuid
     *            UUID.
     * @param name
     *            Name.
     */
    public FindProjectResult(final UUID uuid, final String name) {
        super();
        if (uuid == null) {
            throw new IllegalArgumentException("Argument 'uuid' cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Argument 'name' cannot be null");
        }
        this.uuid = uuid.toString();
        this.name = name;
    }

    /**
     * Returns: Unique identifier.
     * 
     * @return Unique identifier.
     */
    public final UUID getUUID() {
        return UUID.fromString(uuid);
    }

    /**
     * Returns the name.
     * 
     * @return Name.
     */
    public final String getName() {
        return name;
    }

    // CHECKSTYLE:OFF Generated code...

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FindProjectResult other = (FindProjectResult) obj;
        if (uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if (!uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

    // CHECKSTYLE:ON

    @Override
    public final int compareTo(final FindProjectResult other) {
        return name.compareTo(other.name);
    }

    @Override
    public final String toTraceString() {
        return new ToStringBuilder(this).append("uuid", uuid).append("name", name).toString();
    }

    @Override
    public final String toString() {
        return name;
    }

}
