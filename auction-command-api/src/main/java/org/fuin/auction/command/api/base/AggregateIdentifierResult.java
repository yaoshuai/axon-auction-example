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
package org.fuin.auction.command.api.base;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.objects4j.Contract;

/**
 * Result of successfully creating a new aggregate.
 */
public final class AggregateIdentifierResult extends AbstractCommandResult {

	private static final long serialVersionUID = 7124123040949548640L;

	@NotNull
	private String id;

	/**
	 * Default constructor.
	 */
	public AggregateIdentifierResult() {
		super();
	}

	/**
	 * Constructor with id.
	 * 
	 * @param resultCode
	 *            Result code.
	 * @param id
	 *            Unique internal id of the user.
	 */
	public AggregateIdentifierResult(final ResultCode resultCode, final String id) {
		super(resultCode);
		this.id = id;
		Contract.requireValid(this);
	}

	/**
	 * Returns the new ID.
	 * 
	 * @return Unique internal id.
	 */
	public final String getId() {
		return id;
	}

	/**
	 * Appends all properties to the builder.
	 * 
	 * @param builder
	 *            Builder to append key/values.
	 */
	protected void appendRegisterUserCommandResult(final ToStringBuilder builder) {
		builder.append("id", id);
	}

	@Override
	public final String toTraceString() {
		final ToStringBuilder builder = new ToStringBuilder(this);
		appendAbstractCommandResult(builder);
		appendRegisterUserCommandResult(builder);
		return builder.toString();
	}

}
