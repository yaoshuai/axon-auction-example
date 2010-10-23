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
package org.fuin.auction.message.api;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.Contract;
import org.fuin.objects4j.Immutable;
import org.fuin.objects4j.Requires;
import org.fuin.objects4j.validation.UUIDStr;

/**
 * Aggregate id.
 */
@Immutable
public class AuctionAggregateId implements Comparable<AuctionAggregateId>, Serializable {

	private static final long serialVersionUID = -4122133623055950826L;

	@NotNull
	@UUIDStr
	private final String id;

	/**
	 * Constructor with aggregate id.
	 * 
	 * @param id
	 *            Aggregate identifier.
	 */
	@Requires("(id!=null) && UUIDStrValidator.isValid(id)")
	public AuctionAggregateId(final String id) {
		super();
		this.id = id.trim();
		Contract.requireValid(this);
	}

	// CHECKSTYLE:OFF Generated hashCode + equals
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuctionAggregateId other = (AuctionAggregateId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// CHECKSTYLE:ON

	/**
	 * Returns the length of the user id.
	 * 
	 * @return Number of characters.
	 */
	public final int length() {
		return id.length();
	}

	@Override
	public final int compareTo(final AuctionAggregateId obj) {
		final AuctionAggregateId other = (AuctionAggregateId) obj;
		return this.id.compareTo(other.id);
	}

	@Override
	public final String toString() {
		return id;
	}

}
