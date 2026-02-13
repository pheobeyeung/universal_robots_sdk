package com.ur.urcap.examples.driver.gripper.customgrippersetup;

import com.ur.urcap.api.contribution.driver.general.tcp.TCPConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.CustomUserInputConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.UserInput;
import com.ur.urcap.api.contribution.driver.general.userinput.ValueChangedListener;
import com.ur.urcap.api.contribution.driver.general.userinput.selectableinput.ElementResolver;
import com.ur.urcap.api.contribution.driver.gripper.ContributionConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.GripperAPIProvider;
import com.ur.urcap.api.contribution.driver.gripper.GripperConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripperContribution;
import com.ur.urcap.api.contribution.driver.gripper.ReleaseActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.SystemConfiguration;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.value.Pose;
import com.ur.urcap.api.domain.value.PoseFactory;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;

import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.Locale;


public class CustomGripperSetup implements GripperContribution {

	private static final String GRIPPER_NAME = "Custom Setup";
	private static final String UNINITIALIZED_IP = "0.0.0.0";

	private static final String EXTERNAL_GRIPPING_INPUT_ID = "externalGripping";
	private static final String MOUNTING_INPUT_ID = "mounting";
	private static final String IP_ADDRESS_INPUT_ID = "ipAddress";

	private GripperAPIProvider gripperAPIProvider;
	private TCPConfiguration tcpConfiguration;

	private UserInput<Boolean> externalGrippingCheckBox;
	private UserInput<GripperMounting> gripperMountingComboBox;
	private UserInput<String> ipAddress;

	private Pose standardTCPPose;
	private Pose position1TCPPose;
	private Pose position2TCPPose;

	private enum GripperMounting {
		STANDARD("default_id", "Standard"),
		POSITION_1("position1_id","Position 1"),
		POSITION_2("position2_id", "Position 2");

		private final String id;
		private final String displayName;

		GripperMounting(String id, String displayName) {
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
	public void configureInstallation(CustomUserInputConfiguration configurationUIBuilder, SystemConfiguration systemConfiguration,
	                                  TCPConfiguration tcpConfiguration, GripperAPIProvider gripperAPIProvider) {
		this.gripperAPIProvider = gripperAPIProvider;
		this.tcpConfiguration = tcpConfiguration;

		createMountingTCPPoses();
		tcpConfiguration.setTCP("Gripper", standardTCPPose);

		customizeInstallationScreen(configurationUIBuilder);
	}

	private void createMountingTCPPoses() {
		PoseFactory poseFactory = gripperAPIProvider.getPoseFactory();

		standardTCPPose = poseFactory.createPose(0, 0, 100, 0, 0, 0, Length.Unit.MM, Angle.Unit.RAD);
		position1TCPPose = poseFactory.createPose(50, 0, 80, 0, 0.61, 0, Length.Unit.MM, Angle.Unit.RAD);
		position2TCPPose = poseFactory.createPose(-50, 0, 80, 0, -0.61, 0, Length.Unit.MM, Angle.Unit.RAD);
	}

	private Pose getTCPPose(GripperMounting gripperMounting) {
		switch (gripperMounting) {
			case STANDARD:
				return standardTCPPose;
			case POSITION_1:
				return position1TCPPose;
			case POSITION_2:
				return position2TCPPose;
		}
		return standardTCPPose;
	}

	private void customizeInstallationScreen(CustomUserInputConfiguration configurationUIBuilder) {
		configurationUIBuilder.setDescriptionText("Configure the setup of the gripper.");

		externalGrippingCheckBox = configurationUIBuilder.registerBooleanInput(EXTERNAL_GRIPPING_INPUT_ID, "Use External Gripping", true);
		externalGrippingCheckBox.setValueChangedListener(new ValueChangedListener<Boolean>() {
			@Override
			public void onValueChanged(Boolean value) {
				System.out.println("External gripping selection: " + value);
			}
		});

		configurationUIBuilder.addFiller();

		gripperMountingComboBox = configurationUIBuilder.registerPreselectedComboBoxInput(MOUNTING_INPUT_ID, "Gripper Mounting", GripperMounting.STANDARD,
				Arrays.asList(GripperMounting.values()), new ElementResolver<GripperMounting>() {
					@Override
					public String getId(GripperMounting element) {
						return element.getId();
					}

					@Override
					public String getDisplayName(GripperMounting element) {
						return element.getDisplayName();
					}
				});

		gripperMountingComboBox.setValueChangedListener(new ValueChangedListener<GripperMounting>() {
			@Override
			public void onValueChanged(GripperMounting value) {
				tcpConfiguration.updateTCP(getTCPPose(value));
				System.out.println("Selected element in combobox: " + value.toString());
			}
		});

		configurationUIBuilder.addFiller();
		configurationUIBuilder.addFiller();
		configurationUIBuilder.addFiller();

		ipAddress = configurationUIBuilder.registerIPAddressInput(IP_ADDRESS_INPUT_ID, "IP Address", UNINITIALIZED_IP);
		ipAddress.setValueChangedListener(new ValueChangedListener<String>() {
			@Override
			public void onValueChanged(String value) {
				System.out.println("Checking connection on IP: " + value);
			}
		});
	}

	@Override
	public void generatePreambleScript(ScriptWriter scriptWriter) {
		if (externalGrippingCheckBox.getValue()) {
			scriptWriter.appendLine("#configure external gripping");
		} else {
			scriptWriter.appendLine("#configure internal gripping");
		}

		scriptWriter.appendLine("popup(\"<b>Gripper setup</b> <br/>" +
				printExternalGrippingSelection() + "<br/>" +
				printTCPPose() + "<br/>" +
				printIPAddress() + "\")");
	}

	private String printExternalGrippingSelection() {
		return "Gripping type: " + (externalGrippingCheckBox.getValue() ? "External":"Internal");
	}

	private String printTCPPose() {
		return "TCP pose: " + getTCPPose(gripperMountingComboBox.getValue());
	}

	private String printIPAddress() {
		if (ipAddress.getValue().equals(UNINITIALIZED_IP)) {
			return "";
		}
		return "Communicating on: " + ipAddress.getValue();
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
