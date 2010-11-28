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
package org.fuin.auction.command.server.domain;

import org.axonframework.domain.AggregateIdentifier;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.fuin.auction.command.server.events.CategoryCreatedEvent;
import org.fuin.auction.command.server.events.CategoryDeletedEvent;
import org.fuin.auction.command.server.events.CategoryMarkedForDeletionEvent;
import org.fuin.auction.common.CategoryName;
import org.fuin.auction.common.CategoryState;

/**
 * Represents an auction category.
 */
public final class Category extends AbstractAnnotatedAggregateRoot {

	private CategoryState state = CategoryState.INITIAL;

	/**
	 * Constructor with id that fires NO EVENT.
	 * 
	 * @param identifier
	 *            Unique aggregate root id.
	 */
	public Category(final AggregateIdentifier identifier) {
		super(identifier);
	}

	/**
	 * Constructor with id that fires a {@link CategoryCreatedEvent}.
	 * 
	 * @param identifier
	 *            Unique id of the category.
	 * @param name
	 *            Unique name of the category.
	 */
	public Category(final AggregateIdentifier identifier, final CategoryName name) {
		super(identifier);
		apply(new CategoryCreatedEvent(name));
	}

	/**
	 * Marks the category as deleted.
	 * 
	 * @throws IllegalCategoryStateException
	 *             If the category state is not {@link CategoryState#ACTIVE}.
	 */
	public final void markForDeletion() throws IllegalCategoryStateException {
		if (!state.equals(CategoryState.ACTIVE)) {
			throw new IllegalCategoryStateException(state, CategoryState.ACTIVE);
		}
		apply(new CategoryMarkedForDeletionEvent());
	}

	/**
	 * Deletes the category.
	 * 
	 * @throws IllegalCategoryStateException
	 *             If the category state is not {@link CategoryState#DELETED}.
	 */
	public final void delete() throws IllegalCategoryStateException {
		if (!state.equals(CategoryState.MARKED_FOR_DELETION)) {
			throw new IllegalCategoryStateException(state, CategoryState.MARKED_FOR_DELETION);
		}
		apply(new CategoryDeletedEvent());
	}

	/**
	 * Handles the event without checking any constraints.
	 * 
	 * @param event
	 *            Event.
	 */
	@EventHandler
	public final void handleCategoryCreatedEvent(final CategoryCreatedEvent event) {
		state = CategoryState.ACTIVE;
	}

	/**
	 * Handles the event without checking any constraints.
	 * 
	 * @param event
	 *            Event.
	 */
	@EventHandler
	public final void handleCategoryMarkedForDeletionEvent(
	        final CategoryMarkedForDeletionEvent event) {
		state = CategoryState.MARKED_FOR_DELETION;
	}

	/**
	 * Handles the event without checking any constraints.
	 * 
	 * @param event
	 *            Event.
	 */
	@EventHandler
	public final void handleCategoryDeletedEvent(final CategoryDeletedEvent event) {
		state = CategoryState.DELETED;
	}

}
