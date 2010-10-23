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
package org.fuin.auction.command.api.support;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.Contract;

/**
 * A key/value pair to use for an error message.
 */
public abstract class MessageKeyValue {

	@NotNull
	private final String key;

	/**
	 * Constructor with key.
	 * 
	 * @param key
	 *            Unique key.
	 */
	public MessageKeyValue(final String key) {
		super();
		this.key = key;
		Contract.requireValid(this);
	}

	/**
	 * Returns the key of the couple.
	 * 
	 * @return Unique key.
	 */
	public final String getKey() {
		return key;
	}

	// CHECKSTYLE:OFF Generated code
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		final MessageKeyValue other = (MessageKeyValue) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		return true;
	}
	// CHECKSTYLE:ON

}
