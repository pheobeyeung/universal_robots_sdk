package com.ur.urcap.examples.driver.general.customuserinput;

import com.ur.urcap.api.contribution.driver.general.tcp.TCPConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.CustomUserInputConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.TextComponent;
import com.ur.urcap.api.contribution.driver.general.userinput.UserInput;
import com.ur.urcap.api.contribution.driver.general.userinput.ValueChangedListener;
import com.ur.urcap.api.contribution.driver.general.userinput.enterableinput.StringUserInput;
import com.ur.urcap.api.contribution.driver.general.userinput.selectableinput.ElementResolver;
import com.ur.urcap.api.contribution.driver.gripper.ContributionConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.GripperAPIProvider;
import com.ur.urcap.api.contribution.driver.gripper.GripperConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripperContribution;
import com.ur.urcap.api.contribution.driver.gripper.ReleaseActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.SystemConfiguration;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;

import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.Locale;


public class CustomUserInputs implements GripperContribution {

	private static final String GRIPPER_NAME = "Custom User Inputs";

	private static final ImageIcon CONNECTED_ICON = new ImageIcon(CustomUserInputs.class.getResource("/logo/connected.png"));
	private static final ImageIcon DISCONNECTED_ICON = new ImageIcon(CustomUserInputs.class.getResource("/logo/disconnected.png"));

	private TextComponent ipStatus;
	private StringUserInput ipAddress;

	// A custom enum type displayed in a combo box input
	private enum Preset {
		PRESET_1("Preset1_id", "Preset 1"),
		PRESET_2("Preset2_id", "Preset 2"),
		PRESET_3("Preset3_id", "Preset 3"),
		PRESET_4("Preset4_id", "Preset 4");

		private final String id;
		private final String displayName;

		Preset(String id, String displayName) {
			this.id = id;
			this.displayName = displayName;
		}

		public String getId() {
			return id;
		}

		public String getDisplayName() {
			return displayName;
		}
	};

	// A custom enum type displayed in a combo box input
	private enum Option {
		OPTION_1("option1_id", "Option 1"),
		OPTION_2("option2_id", "Option 2"),
		OPTION_3("option3_id", "Option 3");

		private final String id;
		private final String displayName;

		Option(String id, String displayName) {
			this.id = id;
			this.displayName = displayName;
		}

		public String getId() {
			return id;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	@Override
	public String getTitle(Locale locale) {
		return GRIPPER_NAME;
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setLogo(new ImageIcon(getClass().getResource("/logo/logo.png")));
	}

	@Override
	public void configureGripper(GripperConfiguration gripperConfiguration, GripperAPIProvider gripperAPIProvider) {
		// Intentionally left empty
	}

	@Override
	public void configureInstallation(CustomUserInputConfiguration configuration,
									  SystemConfiguration systemConfiguration,
									  TCPConfiguration tcpConfiguration,
									  GripperAPIProvider gripperAPIProvider) {
		configuration.setDescriptionText("This sample demonstrates how to create different user inputs " +
				"(e.g. checkbox and combo box inputs) and other UI elements (e.g. text components) as well as using " +
				"the filler UI element for controlling the layout.");

		customizeInstallationScreen(configuration);
	}

	private void customizeInstallationScreen(CustomUserInputConfiguration configurationUIBuilder) {
		registerDoubleInput(configurationUIBuilder);
		configurationUIBuilder.addFiller();

		registerBooleanInput(configurationUIBuilder);
		configurationUIBuilder.addFiller();

		registerComboBoxInput(configurationUIBuilder);
		registerPreselectedComboBox(configurationUIBuilder);

		configurationUIBuilder.addFiller();
		configurationUIBuilder.addFiller();

		registerIPAddressInput(configurationUIBuilder);

		ipStatus = configurationUIBuilder.addText("Connection State (Text Component)", "");
		updateConnectionStatusTextAndIcon(ipAddress.getValue());
	}

	private void registerIPAddressInput(CustomUserInputConfiguration configuration) {
		ipAddress = configuration.registerIPAddressInput("ipAddress", "IP Address Input", "10.10.10.10");
		ipAddress.setErrorValidator(new InputValidator<String>() {
			@Override
			public boolean isValid(String value) {
				// Custom validation of the IP address
				if ("0.0.0.0".equals(value) || "127.0.0.1".equals(value)) {
					return false;
				}

				return true;
			}

			@Override
			public String getMessage(String value) {
				return "IP address cannot be 0.0.0.0 or 127.0.0.1";
			}
		});
		ipAddress.setValueChangedListener(new ValueChangedListener<String>() {
			@Override
			public void onValueChanged(String value) {
				updateConnectionStatusTextAndIcon(value);
			}
		});
	}

	private void registerPreselectedComboBox(CustomUserInputConfiguration configuration) {
		UserInput comboBoxInput = configuration.registerPreselectedComboBoxInput("preselectedComboBoxInput", "Preselected Combo box Input", Option.OPTION_1,
				Arrays.asList(Option.values()),
				new ElementResolver<Option>() {
					@Override
					public String getId(Option element) {
						return element.getId();
					}

					@Override
					public String getDisplayName(Option element) {
						return element.getDisplayName();
					}
				});
		comboBoxInput.setValueChangedListener(new ValueChangedListener() {
			@Override
			public void onValueChanged(Object value) {
				System.out.println("Selection in preselected combo box input: " + value.toString());
			}
		});
	}

	private void registerComboBoxInput(CustomUserInputConfiguration configuration) {
		UserInput comboBoxInput = configuration.registerComboBoxInput("comboBoxInput", "Combo Box Input", "<Select preset>",
				Arrays.asList(Preset.values()), new ElementResolver<Preset>() {
					@Override
					public String getId(Preset element) {
						return element.getId();
					}

					@Override
					public String getDisplayName(Preset element) {
						return element.getDisplayName();
					}
				});
		comboBoxInput.setValueChangedListener(new ValueChangedListener() {
			@Override
			public void onValueChanged(Object value) {
				if (value != null) {
					System.out.println("Selection in combo box input: " + value.toString());
				} else {
					System.out.println("No valid Selection in combo box input");
				}
			}
		});
	}

	private void registerBooleanInput(CustomUserInputConfiguration configuration) {
		UserInput<Boolean> booleanInput = configuration.registerBooleanInput("booleanInput", "Checkbox Input", true);
		booleanInput.setValueChangedListener(new ValueChangedListener<Boolean>() {
			@Override
			public void onValueChanged(Boolean value) {
				System.out.println("Boolean value changed to: " + value);
			}
		});
	}

	private void registerDoubleInput(CustomUserInputConfiguration configuration) {
		UserInput<Double> doubleInput = configuration.registerDoubleInput("doubleInput", "Decimal Number Input", "mm", 5, 0, 200);
		doubleInput.setValueChangedListener(new ValueChangedListener<Double>() {
			@Override
			public void onValueChanged(Double value) {
				System.out.println("Double value changed to: " + value);
			}
		});
	}

	private void updateConnectionStatusTextAndIcon(String ipAddress) {
		if (pingIpAddress(ipAddress)) {
			ipStatus.setText("Connected");
			ipStatus.setIcon(CONNECTED_ICON);
		} else {
			ipStatus.setText("Not connected");
			ipStatus.setIcon(DISCONNECTED_ICON);
		}
	}

	private boolean pingIpAddress(String ipAddress) {
		// "Simulate" pinging the entered IP address using the 1st segment of the IP address
		String[] splitArray = ipAddress.split("\\.");
		int firstSegmentOfIpAddress = Integer.parseInt(splitArray[0]);

		// The result of the ping operation will be successful, if the first of the IP address is an even number
		return firstSegmentOfIpAddress % 2 == 0;
	}

	@Override
	public void generatePreambleScript(ScriptWriter scriptWriter) {
		// Intentionally left empty
	}

	@Override
	public void generateGripActionScript(ScriptWriter scriptWriter, GripActionParameters gripActionParameters) {
		// Intentionally left empty
	}

	@Override
	public void generateReleaseActionScript(ScriptWriter scriptWriter, ReleaseActionParameters releaseActionParameters) {
		// Intentionally left empty
	}
}