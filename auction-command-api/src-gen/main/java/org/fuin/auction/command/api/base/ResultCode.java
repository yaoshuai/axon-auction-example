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

import static org.fuin.auction.command.api.base.CommandResultType.ERROR;
import static org.fuin.auction.command.api.base.CommandResultType.SUCCESS;

import org.apache.commons.lang.StringUtils;
import org.fuin.objects4j.Ensures;
import org.fuin.objects4j.Requires;

/**
 * All possible result codes.
 */
public enum ResultCode {
	/** The combination of user name and email is already registered */
	DUPLICATE_USERNAME_EMAIL_COMBINATION(101, ERROR,
	        "The combination of user name and email is already registered"),

	/** The name is already used by another user */
	DUPLICATE_USERNAME(102, ERROR, "The name is already used by another user"),

	/** The email address is already registered with another user */
	DUPLICATE_EMAIL(103, ERROR, "The email address is already registered with another user"),

	/** You registered successfully! A confirmation email has been sent to you */
	USER_SUCCESSFULLY_REGISTERED(104, SUCCESS,
	        "You registered successfully! A confirmation email has been sent to you"),

	/** The old password is not equal to the stored password. */
	PASSWORD_WRONG(105, ERROR, "The old password is not equal to the stored password."),

	/** The password was successfully changed. */
	PASSWORD_SUCCESSFULLY_CHANGED(106, SUCCESS, "The password was successfully changed."),

	/** The given token was not equal to the user's verification token. */
	USER_EMAIL_VERIFICATION_FAILED(108, ERROR,
	        "The given token was not equal to the user's verification token."),

	/** Your email address was confirmed successfully */
	USER_EMAIL_VERIFIED(109, SUCCESS, "Your email address was confirmed successfully"),

	/** Category name already exists */
	CATEGORY_ALREADY_EXISTS(110, ERROR, "Category name already exists"),

	/** The new category was created successfully */
	CATEGORY_SUCCESSFULLY_CREATED(113, SUCCESS, "The new category was created successfully"),

	/** The category was successfully marked for deletion. */
	CATEGORY_SUCCESSFULLY_MARKED_FOR_DELETION(114, SUCCESS,
	        "The category was successfully marked for deletion."),

	/** The category was not in an active state. */
	CATEGORY_TO_MARK_NOT_ACTIVE(115, ERROR, "The category was not in an active state."),

	/** The category was successfully deleted. */
	CATEGORY_SUCCESSFULLY_DELETED(116, SUCCESS, "The category was successfully deleted."),

	/** The state of the user was not NEW or RESET. */
	ILLEGAL_USER_STATE(117, ERROR, "The state of the user was not NEW or RESET."),

	/** The category is not marked for deletion. */
	CATEGORY_TO_DELETE_NOT_MARKED(118, ERROR, "The category is not marked for deletion."),

	/** Internal error. */
	INTERNAL_ERROR(1, ERROR, "Internal error"),

	/** Invalid command. */
	INVALID_COMMAND(2, ERROR, "Invalid command"),

	/** Aggregate id not found. */
	ID_NOT_FOUND(3, ERROR, "Aggregate id not found");
	private final int code;
	private final CommandResultType type;
	private final String text;

	/**
	 * Constrcutor with code and text.
	 * 
	 * @param code
	 *            Code.
	 * @param type
	 *            Type of result.
	 * @param text
	 *            Default text.
	 */
	private ResultCode(final int code, final CommandResultType type, final String text) {
		this.code = code;
		this.type = type;
		this.text = text;
	}

	/**
	 * Returns the code.
	 * 
	 * @return Code.
	 */
	public final int getCode() {
		return code;
	}

	/**
	 * Returns a five character code left padded with zeros.
	 * 
	 * @return Code with leading zeros.
	 */
	public final String getCodeStr() {
		return StringUtils.leftPad("" + code, 5, "0");
	}

	/**
	 * Returns the type of the result.
	 * 
	 * @return Type.
	 */
	public final CommandResultType getType() {
		return type;
	}

	/**
	 * Returns the text.
	 * 
	 * @return Default text.
	 */
	public final String getText() {
		return text;
	}

	@Override
	public final String toString() {
		return text + " [" + code + "]";
	}

	/**
	 * Returns if the given code is known.
	 * 
	 * @param code
	 *            Code to find.
	 * 
	 * @return If the code exists <code>true</code> else <code>false</code>.
	 */
	public static boolean exists(final int code) {
		final ResultCode[] values = ResultCode.values();

		for (final ResultCode resultCode : values) {
			if (resultCode.getCode() == code) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the instance for a given code. If the code is unknown an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param code
	 *            Code to find.
	 * 
	 * @return Instance.
	 */
	@Requires("ResultCode.exists(code)")
	@Ensures("\result!=null")
	public static ResultCode forCode(final int code) {
		final ResultCode[] values = ResultCode.values();

		for (final ResultCode resultCode : values) {
			if (resultCode.getCode() == code) {
				return resultCode;
			}
		}

		throw new IllegalArgumentException("Code # " + code + " is unknown!");
	}
}
