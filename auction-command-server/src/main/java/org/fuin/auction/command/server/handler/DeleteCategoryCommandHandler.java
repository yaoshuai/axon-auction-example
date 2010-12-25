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
package org.fuin.auction.command.server.handler;

import javax.inject.Named;

import org.axonframework.domain.AggregateIdentifier;
import org.fuin.auction.command.api.base.AggregateIdentifierResult;
import org.fuin.auction.command.api.base.DeleteCategoryCommand;
import org.fuin.auction.command.api.base.ResultCode;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.auction.command.server.domain.Category;
import org.fuin.auction.command.server.domain.IllegalCategoryStateException;

/**
 * Handler for managing {@link DeleteCategoryCommand} commands.
 */
@Named
public class DeleteCategoryCommandHandler extends AbstractDeleteCategoryCommandHandler {

	@Override
	protected final CommandResult handleIntern(final DeleteCategoryCommand command)
	        throws IllegalCategoryStateException {

		final AggregateIdentifier id = toAggregateId(command.getAggregateId());

		final Category category = getRepository().load(id);
		category.delete();

		// FIXME michael Remove category name from constraints -
		// Resolve the name by executing a query against the query server.
		// constraintSet.remove(categoryName);

		return new AggregateIdentifierResult(ResultCode.CATEGORY_SUCCESSFULLY_DELETED, category
		        .getIdentifier().toString());

	}

}
