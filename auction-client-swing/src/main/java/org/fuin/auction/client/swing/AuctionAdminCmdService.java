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
package org.fuin.auction.client.swing;

import org.fuin.auction.command.api.base.AuctionCommandService;
import org.fuin.auction.common.CategoryName;

/**
 * Maps the result codes from the original {@link AuctionCommandService} back to
 * exceptions and defines explicit methods instead of using command objects.<br>
 * <br>
 * Every method maps 1:1 to a command with the same name and the suffix
 * "Command". The method's arguments map 1:1 to the command's attributes.<br>
 * <br>
 * <b>This interface is NOT implemented by the command server!</b> To use it
 * there has to be an implementation that does the mapping.
 */
public interface AuctionAdminCmdService {

	/**
	 * Creates a new category with agiven name.
	 * 
	 * @param name
	 *            Unique category name.
	 * 
	 * @return Aggregate identifier of the category.
	 * 
	 * @throws CategoryNameAlreadyExistException
	 *             The name is already assigned to another category.
	 */
	public Long createCategory(CategoryName name) throws CategoryNameAlreadyExistException;

}
