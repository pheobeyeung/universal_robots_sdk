package com.ur.urcap.examples.userinput.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserInputInstallationNodeContribution implements InstallationNodeContribution {

	private static final String IP_ADDRESS = "IpAddress";
	private static final String POSITIVE_DOUBLE = "PositiveDouble";
	private static final String TEXT = "Text";
	private static final String PASSWORD_KEY = "Password";

	private static final String IP_ADDRESS_DEFAULT = "0.0.0.0";

	private final UserInputInstallationNodeView view;
	private final DataModel model;
	private final InputValidationFactory validatorFactory;
	private final KeyboardInputFactory keyboardInputFactory;
	private MessageDigest messageDigest;

	public UserInputInstallationNodeContribution(InstallationAPIProvider apiProvider,
	                                             UserInputInstallationNodeView view,
	                                             DataModel model) {
		this.view = view;
		this.model = model;
		this.keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.validatorFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();

		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException ignored) {
		}
	}

	@Override
	public void openView() {
		view.setIpAddress(getIpAddress());
		view.setPositiveDouble(getPositiveDouble());
		view.setText(getText());
		view.setPasswordField(getPasswordPlaceholder());
		view.hidePasswordInfo();
	}

	@Override
	public void closeView() {

	}

	@Override
	public void generateScript(ScriptWriter writer) {

	}

	public KeyboardTextInput getKeyboardForIpAddress() {
		KeyboardTextInput keyboard = keyboardInputFactory.createIPAddressKeyboardInput();
		keyboard.setInitialValue(model.get(IP_ADDRESS, ""));
		return keyboard;
	}

	public KeyboardInputCallback<String> getCallbackForIpAddress() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				model.set(IP_ADDRESS, value);
				view.setIpAddress(value);
			}
		};
	}

	public KeyboardNumberInput<Double> getKeyboardForPositiveNumber() {
		KeyboardNumberInput<Double> keyboard = keyboardInputFactory.createPositiveDoubleKeypadInput();
		keyboard.setInitialValue(model.get(POSITIVE_DOUBLE, 0D));
		return keyboard;
	}

	public KeyboardInputCallback<Double> getCallbackForPositiveNumber() {
		return new KeyboardInputCallback<Double>() {
			@Override
			public void onOk(Double value) {
				model.set(POSITIVE_DOUBLE, value);
				view.setPositiveDouble(value);
			}
		};
	}

	public KeyboardTextInput getKeyboardForTextField() {
		KeyboardTextInput keyboardInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardInput.setInitialValue(model.get(TEXT, ""));
		keyboardInput.setErrorValidator(validatorFactory.createStringLengthValidator(1, 15));
		return keyboardInput;
	}

	public KeyboardInputCallback<String> getCallbackForTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				model.set(TEXT, value);
				view.setText(value);
			}
		};
	}

	public KeyboardTextInput getKeyboardForPassword() {
		KeyboardTextInput keyboard = keyboardInputFactory.createPasswordKeyboardInput();
		keyboard.setErrorValidator(new PasswordValidator());
		return keyboard;
	}

	public KeyboardInputCallback<String> getCallbackForPassword() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				model.set(PASSWORD_KEY, encrypt(value));
				view.setPasswordField(getPasswordPlaceholder());
			}
		};
	}

	public KeyboardTextInput getKeyboardForTestPassword() {
		return keyboardInputFactory.createPasswordKeyboardInput();
	}

	public KeyboardInputCallback<String> getCallbackForTestPassword() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				if (!model.isSet(PASSWORD_KEY)) {
					view.setPasswordNotSet();
				} else if (model.get(PASSWORD_KEY, "").equals(encrypt(value))) {
					view.setPasswordIsCorrect();
				} else {
					view.setPasswordIsNotCorrect();
				}
			}
		};
	}

	public void onClearPasswordClicked() {
		model.remove(PASSWORD_KEY);
		view.setPasswordField("");
	}

	private String encrypt(String value) {
		byte[] bytes = messageDigest.digest(value.getBytes());
		return convertByteToHex(bytes);
	}

	private String convertByteToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte data : bytes) {
			sb.append(Integer.toString((data & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	private String getIpAddress() {
		return model.get(IP_ADDRESS, IP_ADDRESS_DEFAULT);
	}

	private double getPositiveDouble() {
		return model.get(POSITIVE_DOUBLE, 0D);
	}

	private String getText() {
		return model.get(TEXT, "");
	}

	private String getPasswordPlaceholder() {
		return model.isSet(PASSWORD_KEY) ? "Placeholder" : "";
	}
}
