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
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.auction.command.api.support.Command;
import org.fuin.objects4j.Contract;
import org.fuin.objects4j.Label;
import org.fuin.objects4j.Requires;
import org.fuin.objects4j.TextField;

/**
 * Create a new auction category.
 */
@Label("Create a new category")
public final class CreateCategoryCommand implements Command {

	private static final long serialVersionUID = 2143094599433867751L;

	private long version = serialVersionUID;

	@NotNull
	@Size(min = 1, max = 40)
	@Label("Category name")
	@TextField
	private String name;

	/**
	 * Default constructor for serialization.
	 */
	protected CreateCategoryCommand() {
		super();
	}

	/**
	 * Constructor with name.
	 * 
	 * @param name
	 *            Unique name of the category.
	 */
	@Requires("(name!=null) && (name.length()>=1) && (name.length()<=40)")
	public CreateCategoryCommand(final String name) {
		super();
		this.name = name;
		Contract.requireValid(this);
	}

	/**
	 * Returns the name.
	 * 
	 * @return Name of the category.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Sets the name to a new value.
	 * 
	 * @param name
	 *            Category name to set.
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	@Override
	public final long getVersion() {
		return version;
	}

	@Override
	public final Set<Integer> getResultCodes() {
		final Set<Integer> codes = new HashSet<Integer>();
		codes.add(ResultCode.CATEGORY_ALREADY_EXISTS.getCode());
		codes.add(ResultCode.INVALID_COMMAND.getCode());
		codes.add(ResultCode.INTERNAL_ERROR.getCode());
		return codes;
	}

	@Override
	public final String toTraceString() {
		return new ToStringBuilder(this).append("name", name).append("version", version).toString();
	}

}
