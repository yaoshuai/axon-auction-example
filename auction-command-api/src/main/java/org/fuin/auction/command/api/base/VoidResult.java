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

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.auction.command.api.support.MessageKeyValue;
import org.fuin.objects4j.Requires;

/**
 * Result that signals success without any additional data.
 */
public final class VoidResult extends AbstractCommandResult {

	private static final long serialVersionUID = -3123164570987026143L;

	/**
	 * Default constructor.
	 */
	protected VoidResult() {
		super();
	}

	/**
	 * Constructor without key/values.
	 * 
	 * @param resultCode
	 *            Result code.
	 */
	@Requires("resultCode!=null")
	public VoidResult(final ResultCode resultCode) {
		super(resultCode);
	}

	/**
	 * Constructor with key/value list.
	 * 
	 * @param resultCode
	 *            Result code.
	 * @param messageKeyValues
	 *            Set of key/value pairs for localized error message.
	 */
	@Requires("resultCode!=null")
	public VoidResult(final ResultCode resultCode, final List<MessageKeyValue> messageKeyValues) {
		super(resultCode, messageKeyValues);
	}

	/**
	 * Constructor with key/value array.
	 * 
	 * @param resultCode
	 *            Result code.
	 * @param messageKeyValues
	 *            Array of key/value pairs for localized error message.
	 */
	@Requires("resultCode!=null")
	public VoidResult(final ResultCode resultCode, final MessageKeyValue... messageKeyValues) {
		super(resultCode, messageKeyValues);
	}

	@Override
	public final String toTraceString() {
		final ToStringBuilder builder = new ToStringBuilder(this);
		appendAbstractCommandResult(builder);
		return builder.toString();
	}

}
