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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.objects4j.Contract;

/**
 * A category was marked for deletion.
 */
public final class CategoryMarkedForDeletionMessage implements AuctionMessage {

	private static final long serialVersionUID = -6015129806828558569L;

	@NotNull
	private Long id;

	/**
	 * Default constructor for serialization.
	 */
	protected CategoryMarkedForDeletionMessage() {
		super();
	}

	/**
	 * Constructor with all attributes.
	 * 
	 * @param id
	 *            Category's aggregate id
	 */
	public CategoryMarkedForDeletionMessage(final Long id) {
		super();
		this.id = id;
		Contract.requireValid(this);
	}

	/**
	 * Returns the category's aggregate id.
	 * 
	 * @return Unique id.
	 */
	public final Long getId() {
		return id;
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("id", id).toString();
	}

}
