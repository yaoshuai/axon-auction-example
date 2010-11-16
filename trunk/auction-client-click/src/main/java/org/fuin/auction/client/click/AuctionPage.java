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
package org.fuin.auction.client.click;

import java.net.MalformedURLException;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.click.Page;
import org.apache.click.control.Form;
import org.apache.click.control.PasswordField;
import org.apache.click.control.TextField;
import org.apache.commons.lang.StringUtils;
import org.fuin.auction.command.api.base.AuctionCommandService;
import org.fuin.auction.command.api.support.CommandResult;
import org.fuin.auction.command.api.support.MessageKeyValue;
import org.fuin.objects4j.RenderClassInfo;
import org.fuin.objects4j.RenderFieldInfo;
import org.fuin.objects4j.TextFieldInfo;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Base page layout for all application pages.
 */
public abstract class AuctionPage extends Page {

	private static final long serialVersionUID = 1L;

	/** Error message code for an internal error. */
	protected static final String INTERNAL_ERROR = "0001";

	private final AuctionCommandService commandService;

	private final Validator validator;

	/**
	 * Default constructor.
	 */
	public AuctionPage() {
		super();

		// TODO michael Refactor the following when switching to Spring
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		try {
			commandService = (AuctionCommandService) new HessianProxyFactory().create(
			        AuctionCommandService.class,
			        "http://localhost:8080/auction-command-server/AuctionCommandService");
		} catch (final MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public final String getTemplate() {
		return "/auction-template.htm";
	}

	/**
	 * Returns the command service.
	 * 
	 * @return Command service.
	 */
	protected final AuctionCommandService getCommandService() {
		return commandService;
	}

	/**
	 * Sets the title of the page.
	 * 
	 * @param renderClassInfo
	 *            Render information for the class.
	 * @param defaultTitle
	 *            Default title.
	 */
	protected final void setTitle(final RenderClassInfo renderClassInfo, final String defaultTitle) {
		if (renderClassInfo.getLabelClassInfo() != null) {
			if (renderClassInfo.getLabelClassInfo().getText() == null) {
				addModel("title", defaultTitle);
			} else {
				addModel("title", renderClassInfo.getLabelClassInfo().getText());
			}
		} else {
			addModel("title", defaultTitle);
		}
	}

	/**
	 * Returns a formatted message.
	 * 
	 * @param result
	 *            Command result.
	 * 
	 * @return Formatted and localized message.
	 */
	protected final String getMessage(final CommandResult result) {
		final int msgId = result.getMessageId();
		final List<MessageKeyValue> msgKeyValues = result.getMessageKeyValues();
		final String msg = getMessage(StringUtils.leftPad("" + msgId, 4, "0"));
		// FIXME michael Handle key/values
		return msg;
	}

	/**
	 * Adds all fields of the renderClassInfo to the form.
	 * 
	 * @param renderClassInfo
	 *            Render information.
	 * @param form
	 *            Form to add the fields to.
	 */
	protected final void renderToForm(final RenderClassInfo<?> renderClassInfo, final Form form) {

		final List<RenderFieldInfo> fields = renderClassInfo.getRenderFields();
		for (final RenderFieldInfo field : fields) {

			// TODO michael Handle other controls than text fields
			if (field.getTextFieldInfo() != null) {
				final TextFieldInfo tf = field.getTextFieldInfo();
				final TextField textField;
				if (field.isPasswordField()) {
					textField = new PasswordField(tf.getField().getName(), field.getLabelText(),
					        field.isRequired());
				} else {
					textField = new BeanValidationTextField(validator, renderClassInfo.getClasz(),
					        tf.getField().getName(), field.getLabelText(), field.isRequired());
				}
				final Long minLength = field.getMinLength();
				if (minLength != null) {
					textField.setMinLength(minLength.intValue());
				}
				final Long maxLength = field.getMaxLength();
				if (maxLength != null) {
					textField.setMaxLength(maxLength.intValue());
				}
				if (tf.getWidth() > 0) {
					textField.setSize(tf.getWidth());
				}
				form.add(textField);
			}
		}

	}

}
