package com.ur.urcap.examples.helloworldswing.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class HelloWorldProgramNodeContribution implements ProgramNodeContribution {
	private static final String NAME = "name";

	private final ProgramAPI programAPI;
	private final UndoRedoManager undoRedoManager;
	private final KeyboardInputFactory keyboardFactory;

	private final HelloWorldProgramNodeView view;
	private final DataModel model;

	public HelloWorldProgramNodeContribution(ProgramAPIProvider apiProvider, HelloWorldProgramNodeView view, DataModel model) {
		this.programAPI = apiProvider.getProgramAPI();
		this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();
		this.keyboardFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();

		this.view = view;
		this.model = model;
	}

	@Override
	public void openView() {
		view.setPopupText(getName());
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
		writer.appendLine("popup(\"" + generatePopupMessage() + "\", hello_world_swing_popup_title, False, False, blocking=True)");
		writer.writeChildren();
	}

	public KeyboardTextInput getKeyboardForTextField() {
		KeyboardTextInput keyboardInput = keyboardFactory.createStringKeyboardInput();
		keyboardInput.setInitialValue(getName());
		return keyboardInput;
	}

	public KeyboardInputCallback<String> getCallbackForTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setPopupTitle(value);
				view.setPopupText(value);
			}
		};
	}

	public void setPopupTitle(final String value) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				if ("".equals(value)) {
					model.remove(NAME);
				} else {
					model.set(NAME, value);
				}
			}
		});

		updatePopupMessageAndPreview();
	}

	private String generatePopupMessage() {
		return model.isSet(NAME) ? "Hello " + getName() + ", welcome to PolyScope!" : "No name set";
	}

	private void updatePopupMessageAndPreview() {
		view.setMessagePreview(generatePopupMessage());
		view.setTitlePreview(getInstallation().isDefined() ? getInstallation().getPopupTitle() : "No title set");
	}

	private String getName() {
		return model.get(NAME, "");
	}

	private HelloWorldInstallationNodeContribution getInstallation() {
		return programAPI.getInstallationNode(HelloWorldInstallationNodeContribution.class);
	}

}
