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
package org.fuin.auction.common;

import java.io.Serializable;

/**
 * An object that has a version information for the class. This is NOT the same
 * as the {@link Serializable} interface. This version is intended to handle
 * different versions of an object (do a conversion for example).
 */
public interface Versioned {

	/**
	 * Returns the version of the instance.
	 * 
	 * @return Version number.
	 */
	public int getInstanceVersion();

	/**
	 * Returns the version of the class.
	 * 
	 * @return Version number.
	 */
	public int getClassVersion();

	/**
	 * Returns if class and instance version are the same.
	 * 
	 * @return If both have the same value <code>true</code> else
	 *         <code>false</code>.
	 */
	public boolean isSameVersion();

}
