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
package org.fuin.auction.command.server.support;

import java.io.Serializable;

import org.axonframework.domain.AggregateIdentifier;
import org.fuin.objects4j.Contract;
import org.fuin.objects4j.Requires;

/**
 * An aggregate identifier that is based on a {@link Long} value.
 */
public final class AggregateIdentifierLong implements AggregateIdentifier, Serializable {

	private static final long serialVersionUID = -5486703238473050628L;

	private final Long id;

	/**
	 * Constructor with a given value.
	 * 
	 * @param value
	 *            The id value.
	 * 
	 * @throws IllegalAggregateIdentifierException
	 *             Error parsing the aggregate identifier string.
	 */
	@Requires("value!=null")
	public AggregateIdentifierLong(final Long value) throws IllegalAggregateIdentifierException {
		super();
		if (value == null) {
			throw new IllegalAggregateIdentifierException("null", AggregateIdentifierLong.class);
		}
		this.id = value;
	}

	/**
	 * Constructor with a given long value.
	 * 
	 * @param valueStr
	 *            String representation of the id value.
	 * 
	 * @throws IllegalAggregateIdentifierException
	 *             Error parsing the aggregate identifier string.
	 */
	@Requires("valueStr!=null && LongStrValidator.isValid(valueStr)")
	public AggregateIdentifierLong(final String valueStr)
	        throws IllegalAggregateIdentifierException {
		super();
		Contract.requireArgNotNull("valueStr", valueStr);
		try {
			this.id = Long.valueOf(valueStr);
		} catch (final NumberFormatException ex) {
			throw new IllegalAggregateIdentifierException(valueStr, AggregateIdentifierLong.class);
		}
	}

	// CHECKSTYLE:OFF Generated code

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		final AggregateIdentifierLong other = (AggregateIdentifierLong) obj;
		return id.equals(other.id);
	}

	// CHECKSTYLE:ON

	@Override
	public final String asString() {
		return id.toString();
	}

	/**
	 * Returns the aggregate identifier as long value.
	 * 
	 * @return Aggregate identifier.
	 */
	public final Long asLong() {
		return id;
	}

	@Override
	public final String toString() {
		return id.toString();
	}

}
