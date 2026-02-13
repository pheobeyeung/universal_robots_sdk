package com.ur.urcap.examples.helloworld.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputTextField;

public class HelloWorldInstallationNodeContribution implements InstallationNodeContribution {

	private static final String POPUPTITLE_KEY = "popuptitle";
	private static final String DEFAULT_VALUE = "Hello World";

	private DataModel model;

	public HelloWorldInstallationNodeContribution(DataModel model) {
		this.model = model;
	}

	@Input(id = POPUPTITLE_KEY)
	private InputTextField popupTitleField;

	@Input(id = POPUPTITLE_KEY)
	public void onMessageChange(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_CHANGE) {
			setPopupTitle(popupTitleField.getText());
		}
	}

	@Override
	public void openView() {
		popupTitleField.setText(getPopupTitle());
	}

	@Override
	public void closeView() { }

	public boolean isDefined() {
		return !getPopupTitle().isEmpty();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Store the popup title in a global variable so it is globally available to all HelloWorld program nodes.
		writer.assign("hello_world_popup_title", "\"" + getPopupTitle() + "\"");
	}

	public String getPopupTitle() {
		return model.get(POPUPTITLE_KEY, DEFAULT_VALUE);
	}

	private void setPopupTitle(String message) {
		if ("".equals(message)) {
			resetToDefaultValue();
		} else {
			model.set(POPUPTITLE_KEY, message);
		}
	}

	private void resetToDefaultValue() {
		popupTitleField.setText(DEFAULT_VALUE);
		model.set(POPUPTITLE_KEY, DEFAULT_VALUE);
	}
}
