package com.ur.urcap.examples.moveuntildetection;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.UserInterfaceAPI;
import com.ur.urcap.api.domain.userinteraction.UserInteraction;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.examples.moveuntildetection.style.Style;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MoveUntilDetectionProgramNodeView implements SwingProgramNodeView<MoveUntilDetectionProgramNodeContribution> {
	private final static String INFO_TEXT =
			"<html> Move the robot in the Z- direction until a sensor detects an object (through IO input).<br></br><br></br>" +
					"If an object is detected, the motion is stopped, and an IO is set to close the gripper.<br></br>" +
					"If no object is detected after moving the maximum distance, a pop-up is displayed to alert the user.</html>";
	private static final String MM_TXT = "mm";

	private final KeyboardInputFactory keyboardFactory;
	private final ViewAPIProvider apiProvider;
	private final UIComponentFactory uiFactory;

	private JTextField maxDistanceInputField;
	private KeyboardNumberInput<Double> keyboard;
	private ContributionProvider<MoveUntilDetectionProgramNodeContribution> provider;

	public MoveUntilDetectionProgramNodeView(ViewAPIProvider apiProvider, Style style) {
		this.apiProvider = apiProvider;
		this.keyboardFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.uiFactory = new UIComponentFactory(style);
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<MoveUntilDetectionProgramNodeContribution> provider) {
		this.provider = provider;
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(uiFactory.createInfoSection(INFO_TEXT));
		panel.add(uiFactory.createVerticalSpacing(3));
		maxDistanceInputField = createMaxDistanceInput();
		panel.add(uiFactory.createInputSection("Maximum Distance", maxDistanceInputField, MM_TXT));
	}

	public void updateView(double distanceInMM) {
		String distance = String.valueOf(distanceInMM);
		maxDistanceInputField.setText(distance);
		setDistanceLimits(MoveUntilDetectionProgramNodeContribution.MIN_DISTANCE,
				MoveUntilDetectionProgramNodeContribution.MAX_DISTANCE);
	}

	public void setDistanceLimits(double min, double max) {
		UserInterfaceAPI userInterfaceAPI = apiProvider.getUserInterfaceAPI();
		UserInteraction userInteraction = userInterfaceAPI.getUserInteraction();
		InputValidationFactory inputValidationFactory = userInteraction.getInputValidationFactory();
		InputValidator<Double> validator = inputValidationFactory.createDoubleRangeValidator(min, max);
		keyboard.setErrorValidator(validator);
	}

	private JTextField createMaxDistanceInput() {
		keyboard = keyboardFactory.createDoubleKeypadInput();
		KeyboardInputCallback<Double> callback = new KeyboardInputCallback<Double>() {
			@Override
			public void onOk(Double value) {
				provider.get().setDistance(value);
				maxDistanceInputField.setText(String.valueOf(value));
			}
		};
		ValueProvider<Double> valueProvider = new ValueProvider<Double>() {
			@Override
			public Double get() {
				return provider.get().getDistanceInMM();
			}
		};
		return uiFactory.createNumberInputField(keyboard, callback, valueProvider);
	}
}
