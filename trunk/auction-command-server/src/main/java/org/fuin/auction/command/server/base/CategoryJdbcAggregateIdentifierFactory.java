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
package org.fuin.auction.command.server.base;

import javax.inject.Named;

import org.fuin.auction.command.server.utils.AbstractJdbcAggregateIdentifierLongFactory;

/**
 * Pure JDBC service (Apache Derby!) that creates new aggregate identifiers.
 * Could be done more nice (database independent for example...) but it's just
 * to show that there is no need that a "full-blown" Hibernate or JPA here...
 */
@Named
public final class CategoryJdbcAggregateIdentifierFactory extends
        AbstractJdbcAggregateIdentifierLongFactory {

	/**
	 * Default constructor.
	 */
	public CategoryJdbcAggregateIdentifierFactory() {
		super(CategoryJdbcAggregateIdentifierFactory.class, "/jdbc.properties",
		        "VALUES (NEXT VALUE FOR COMMANDSERVER.CATEGORY_ID)");
	}

}
