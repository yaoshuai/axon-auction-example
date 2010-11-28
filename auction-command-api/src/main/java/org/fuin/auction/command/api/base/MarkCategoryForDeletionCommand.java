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

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.auction.command.api.support.Command;
import org.fuin.objects4j.Contract;
import org.fuin.objects4j.Label;
import org.fuin.objects4j.Requires;

/**
 * Marks an existing category for deletion.
 */
@Label("Mark the category for deletion")
public final class MarkCategoryForDeletionCommand implements Command {

	private static final long serialVersionUID = 2143094599433867751L;

	private long version = serialVersionUID;

	@NotNull
	private Long categoryId;

	/**
	 * Default constructor for serialization.
	 */
	protected MarkCategoryForDeletionCommand() {
		super();
	}

	/**
	 * Constructor with name.
	 * 
	 * @param categoryId
	 *            Id of the category.
	 */
	@Requires("(categoryId!=null)")
	public MarkCategoryForDeletionCommand(final Long categoryId) {
		super();
		this.categoryId = categoryId;
		Contract.requireValid(this);
	}

	/**
	 * Returns the aggregate id of the category.
	 * 
	 * @return Unique id.
	 */
	public final Long getCategoryId() {
		return categoryId;
	}

	/**
	 * Sets the id of the category.
	 * 
	 * @param categoryId
	 *            Unique id to set.
	 */
	public final void setCategoryId(final Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public final long getVersion() {
		return version;
	}

	@Override
	public final Set<Integer> getResultCodes() {
		final Set<Integer> codes = new HashSet<Integer>();
		codes.add(ResultCode.CATEGORY_SUCCESSFULLY_MARKED_FOR_DELETION.getCode());
		codes.add(ResultCode.ILLEGAL_CATEGORY_STATE.getCode());
		codes.add(ResultCode.ID_NOT_FOUND.getCode());
		codes.add(ResultCode.INVALID_COMMAND.getCode());
		codes.add(ResultCode.INTERNAL_ERROR.getCode());
		return codes;
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("categoryId", categoryId)
		        .append("version", version).toString();
	}

}
