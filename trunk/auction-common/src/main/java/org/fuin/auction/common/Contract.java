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

/**
 * Utility class for doing assertions on parameters and object state.
 */
public final class Contract {

    /**
     * Private constructor to avoid instantiation.
     */
    private Contract() {
        throw new UnsupportedOperationException("You cannot create an instance of a utility class!");
    }

    /**
     * Checks if the given value is not <code>null</code> or throws an
     * {@link IllegalArgumentException} otherwise.
     * 
     * @param name
     *            Name of the argument to display on the error message.
     * @param value
     *            Value to check for <code>null</code>.
     * 
     * @pre name != null
     */
    public static void requireParamNotNull(final String name, final Object value) {
        if (value == null) {
            throw new IllegalArgumentException("The argument '" + name + "' cannot be null!");
        }
    }

    /**
     * Checks if the given property value is not <code>null</code> or throws an
     * {@link IllegalStateException} otherwise.
     * 
     * @param name
     *            Name of the property key to display on the error message.
     * @param value
     *            Value to check for <code>null</code>.
     * 
     * @pre name != null
     */
    public static void requirePropertyNotNull(final String name, final Object value) {
        if (value == null) {
            throw new IllegalStateException("The property key '" + name + "' was not found!");
        }
    }

}
