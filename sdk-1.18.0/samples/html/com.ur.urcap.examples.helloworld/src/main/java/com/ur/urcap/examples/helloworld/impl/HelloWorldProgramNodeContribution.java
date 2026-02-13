package com.ur.urcap.examples.helloworld.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputTextField;
import com.ur.urcap.api.ui.component.LabelComponent;

public class HelloWorldProgramNodeContribution implements ProgramNodeContribution {
	private static final String NAME = "name";

	private final DataModel model;
	private final URCapAPI api;

	public HelloWorldProgramNodeContribution(URCapAPI api, DataModel model) {
		this.api = api;
		this.model = model;
	}

	@Input(id = "yourname")
	private InputTextField nameTextField;

	@Label(id = "titlePreviewLabel")
	private LabelComponent titlePreviewLabel;

	@Label(id = "messagePreviewLabel")
	private LabelComponent messagePreviewLabel;

	@Input(id = "yourname")
	public void onInput(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_CHANGE) {
			setName(nameTextField.getText());
			updatePopupMessageAndPreview();
		}
	}

	@Override
	public void openView() {
		nameTextField.setText(getName());
		updatePopupMessageAndPreview();
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return "Hello World: " + (model.isSet(NAME) ? getName() : "");
	}

	@Override
	public boolean isDefined() {
		return getInstallation().isDefined() && !getName().isEmpty();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Directly generate this Program Node's popup message + access the popup title through a global variable
		writer.appendLine("popup(\"" + generatePopupMessage() + "\", hello_world_popup_title, False, False, blocking=True)");
		writer.writeChildren();
	}

	private String generatePopupMessage() {
		return model.isSet(NAME) ? "Hello " + getName() + ", welcome to PolyScope!" : "No name set";
	}

	private void updatePopupMessageAndPreview() {
		messagePreviewLabel.setText(generatePopupMessage());
		titlePreviewLabel.setText(getInstallation().isDefined() ? getInstallation().getPopupTitle() : "No title set");
	}

	private String getName() {
		return model.get(NAME, "");
	}

	private void setName(String name) {
		if ("".equals(name)){
			model.remove(NAME);
		}else{
			model.set(NAME, name);
		}
	}

	private HelloWorldInstallationNodeContribution getInstallation() {
		return api.getInstallationNode(HelloWorldInstallationNodeContribution.class);
	}

}
