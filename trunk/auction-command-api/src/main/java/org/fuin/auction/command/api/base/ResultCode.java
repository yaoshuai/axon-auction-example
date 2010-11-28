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

	/** Internal error. */
	INTERNAL_ERROR(1, ERROR, "Internal error"),

	/** Invalid command. */
	INVALID_COMMAND(2, ERROR, "Invalid command"),

	/** User name already in use by another user. */
	DUPLICATE_USERNAME(3, ERROR, "User name already in use by another user"),

	/** Email address already registered for another user id. */
	DUPLICATE_EMAIL(4, ERROR, "Email address already registered for another user id"),

	/** Already registered with same user id and email address. */
	DUPLICATE_USERNAME_EMAIL_COMBINATION(5, ERROR,
	        "Already registered with same user id and email address"),

	/** Password wrong. */
	PASSWORD_WRONG(6, ERROR, "Password wrong"),

	/** Unique id not found. */
	ID_NOT_FOUND(7, ERROR, "Unique id not found"),

	/** Verification failed. */
	USER_EMAIL_VERIFICATION_FAILED(8, ERROR, "The verification of the email failed"),

	/** Category name already exists. */
	CATEGORY_ALREADY_EXISTS(9, ERROR, "Category name already exists"),

	/** A new user was successfully registered. */
	USER_SUCCESSFULLY_REGISTERED(10, SUCCESS, "Your registration was successful"),

	/** A user password was successfully changed. */
	PASSWORD_SUCCESSFULLY_CHANGED(11, SUCCESS, "The password was successfully changed"),

	/** A user's email was successfully verified. */
	USER_EMAIL_VERIFIED(12, SUCCESS, "Your email was successfully verified"),

	/** A new category was successfully created. */
	CATEGORY_SUCCESSFULLY_CREATED(13, SUCCESS, "A new category was created successfully"),

	/** A category was successfully prepared for deletion. */
	CATEGORY_SUCCESSFULLY_MARKED_FOR_DELETION(14, SUCCESS,
	        "The category was successfully marked for deletion"),

	/** The state of the category was not as expected. */
	ILLEGAL_CATEGORY_STATE(15, ERROR, "The category was not in an expected state"),

	/** A category was successfully deleted. */
	CATEGORY_SUCCESSFULLY_DELETED(16, SUCCESS, "The category was successfully deleted");

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
