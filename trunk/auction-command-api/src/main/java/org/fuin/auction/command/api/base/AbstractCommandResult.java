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

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.auction.command.api.support.MessageKeyValue;
import org.fuin.objects4j.Requires;

/**
 * Basic result of sending/executing a command.
 */
public abstract class AbstractCommandResult implements CommandResult {

	private static final long serialVersionUID = 2861284299330260251L;

	private boolean success;

	private boolean warning;

	private boolean error;

	@Min(0)
	private int code;

	@NotNull
	private String text;

	private List<MessageKeyValue> messageKeyValues;

	/**
	 * Default constructor.
	 */
	protected AbstractCommandResult() {
		super();
		success = false;
		warning = false;
		code = -1;
		text = null;
		messageKeyValues = null;
	}

	/**
	 * Constructor without key/values.
	 * 
	 * @param resultCode
	 *            Result code.
	 */
	@Requires("resultCode!=null")
	public AbstractCommandResult(final ResultCode resultCode) {
		this(resultCode, (List<MessageKeyValue>) null);
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
	public AbstractCommandResult(final ResultCode resultCode,
	        final MessageKeyValue... messageKeyValues) {
		this(resultCode, Arrays.asList(messageKeyValues));
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
	public AbstractCommandResult(final ResultCode resultCode,
	        final List<MessageKeyValue> messageKeyValues) {
		this.success = resultCode.getType().equals(CommandResultType.SUCCESS);
		this.warning = resultCode.getType().equals(CommandResultType.WARNING);
		this.error = resultCode.getType().equals(CommandResultType.ERROR);
		this.code = resultCode.getCode();
		this.text = resultCode.getText();
		this.messageKeyValues = messageKeyValues;
	}

	@Override
	public final boolean isSuccess() {
		return success;
	}

	@Override
	public final boolean isWarning() {
		return warning;
	}

	@Override
	public final boolean isError() {
		return error;
	}

	@Override
	public final int getCode() {
		return code;
	}

	@Override
	public final String getCodeStr() {
		return StringUtils.leftPad("" + code, 5, "0");
	}

	@Override
	public final String getText() {
		return text;
	}

	@Override
	public final List<MessageKeyValue> getMessageKeyValues() {
		return messageKeyValues;
	}

	/**
	 * Appends all properties to the builder.
	 * 
	 * @param builder
	 *            Builder to append key/values.
	 */
	protected final void appendAbstractCommandResult(final ToStringBuilder builder) {
		builder.append("success", success).append("warning", warning).append("error", error)
		        .append("code", code).append("text", text).append("messageKeyValues",
		                messageKeyValues);
	}

}
