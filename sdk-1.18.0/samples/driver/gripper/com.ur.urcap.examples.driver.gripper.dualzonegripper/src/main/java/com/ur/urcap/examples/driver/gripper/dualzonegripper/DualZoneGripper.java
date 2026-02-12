package com.ur.urcap.examples.driver.gripper.dualzonegripper;

import com.ur.urcap.api.contribution.driver.general.tcp.TCPConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.CustomUserInputConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.ValueChangedListener;
import com.ur.urcap.api.contribution.driver.general.userinput.selectableinput.BooleanUserInput;

import com.ur.urcap.api.contribution.driver.gripper.ContributionConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.GripperAPIProvider;
import com.ur.urcap.api.contribution.driver.gripper.GripperConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripperContribution;
import com.ur.urcap.api.contribution.driver.gripper.ReleaseActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.SystemConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.capability.GripVacuumCapability;
import com.ur.urcap.api.contribution.driver.gripper.capability.GripperCapabilities;
import com.ur.urcap.api.contribution.driver.gripper.capability.multigripper.GripperList;
import com.ur.urcap.api.contribution.driver.gripper.capability.multigripper.GripperListBuilder;
import com.ur.urcap.api.contribution.driver.gripper.capability.multigripper.GripperListProvider;

import com.ur.urcap.api.domain.program.nodes.contributable.device.gripper.configuration.SelectableGripper;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.value.PoseFactory;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.api.domain.value.simple.Pressure;

import javax.swing.ImageIcon;
import java.util.Locale;


public class DualZoneGripper implements GripperContribution {

	private static final String GRIPPER_TITLE = "Dual Zone Gripper";

	private static final String ZONE_A_NAME = "Zone A";
	private static final String ZONE_B_NAME = "Zone B";
	private static final String ZONE_AB_NAME = "Zone A+B";

	private static final String ZONE_A_TCP_NAME = "Zone_A";
	private static final String ZONE_B_TCP_NAME = "Zone_B";
	private static final String ZONE_AB_TCP_NAME = "Zone_AB";

	// Ids must remain constant over time and versions of the Gripper URCap, since they are used for persistence and
	// can be used by other URCaps for configuring Gripper program nodes.
	private static final String ZONE_A_ID = "ZoneA_id";
	private static final String ZONE_B_ID = "ZoneB_id";
	private static final String ZONE_AB_ID = "ZoneAB_id";

	private static final String FRAGILE_HANDLING_LABEL = "Use Fragile Handling";
	private static final String FRAGILE_HANDLING_ID = "fragile_handling_id";

	private SelectableGripper zoneAGripper;
	private SelectableGripper zoneBGripper;
	private SelectableGripper zoneABGripper;
	private GripVacuumCapability gripVacuumCapability;

	private BooleanUserInput fragileHandlingInput;

	@Override
	public String getTitle(Locale locale) {
		return GRIPPER_TITLE;
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setLogo(new ImageIcon(getClass().getResource("/logo/logo.png")));
	}

	@Override
	public void configureGripper(GripperConfiguration gripperConfiguration, GripperAPIProvider gripperAPIProvider) {
		GripperCapabilities capabilities = gripperConfiguration.getGripperCapabilities();

		capabilities.registerMultiGripperCapability(new GripperListProvider() {
			@Override
			public GripperList getGripperList(GripperListBuilder gripperListBuilder, Locale locale) {
				zoneAGripper = gripperListBuilder.createGripper(ZONE_A_ID, ZONE_A_NAME, true);
				zoneBGripper = gripperListBuilder.createGripper(ZONE_B_ID, ZONE_B_NAME, true);
				zoneABGripper = gripperListBuilder.createGripper(ZONE_AB_ID, ZONE_AB_NAME, true);

				return gripperListBuilder.buildList();
			}
		});

		gripVacuumCapability = capabilities.registerGrippingVacuumCapability(0, 100, 70, Pressure.Unit.KPA);
	}

	@Override
	public void configureInstallation(CustomUserInputConfiguration configurationUIBuilder,
									  SystemConfiguration systemConfiguration,
									  TCPConfiguration tcpConfiguration,
									  GripperAPIProvider gripperAPIProvider) {
		configureGripperTCPs(systemConfiguration, gripperAPIProvider);
		customizeInstallationScreen(configurationUIBuilder);
	}

	private void configureGripperTCPs(SystemConfiguration systemConfiguration, GripperAPIProvider gripperAPIProvider) {
		PoseFactory poseFactory = gripperAPIProvider.getPoseFactory();

		TCPConfiguration zoneATCPConfiguration = systemConfiguration.getTCPConfiguration(zoneAGripper);
		zoneATCPConfiguration.setTCP(ZONE_A_TCP_NAME, poseFactory.createPose(75, 0, 50, 0, 0, 0, Length.Unit.MM, Angle.Unit.DEG));

		TCPConfiguration zoneBTCPConfiguration = systemConfiguration.getTCPConfiguration(zoneBGripper);
		zoneBTCPConfiguration.setTCP(ZONE_B_TCP_NAME, poseFactory.createPose(-75, 0, 50, 0, 0, 0, Length.Unit.MM, Angle.Unit.DEG));

		TCPConfiguration zoneABTCPConfiguration = systemConfiguration.getTCPConfiguration(zoneABGripper);
		zoneABTCPConfiguration.setTCP(ZONE_AB_TCP_NAME, poseFactory.createPose(0, 0, 50, 0, 0, 0, Length.Unit.MM, Angle.Unit.DEG));
	}

	private void customizeInstallationScreen(CustomUserInputConfiguration configurationUIBuilder) {
		fragileHandlingInput = configurationUIBuilder.registerBooleanInput(FRAGILE_HANDLING_ID, FRAGILE_HANDLING_LABEL, false);
		fragileHandlingInput.setValueChangedListener(new ValueChangedListener<Boolean>() {
			@Override
			public void onValueChanged(Boolean useFragileHandling) {
				updateVacuumCapability(useFragileHandling);
			}
		});
	}

	// This method updates the parameters of the registered vacuum capability for all individual grippers
	private void updateVacuumCapability(boolean useFragileHandling) {
		if (useFragileHandling) {
			gripVacuumCapability.updateCapability(0, 70, 40, Pressure.Unit.KPA);
		} else {
			gripVacuumCapability.updateCapability(0, 100, 70, Pressure.Unit.KPA);
		}
	}

	@Override
	public void generatePreambleScript(ScriptWriter scriptWriter) {
		// Intentionally left empty
	}

	@Override
	public void generateGripActionScript(ScriptWriter scriptWriter, GripActionParameters gripActionParameters) {
		System.out.println("Grip Action :");

		printFragileHandlingSelection();
		if (fragileHandlingInput.getValue()){
			// Simulate applying fragile handling
			scriptWriter.appendLine("sleep(0.01)");
		}

		SelectableGripper selectedGripper = gripActionParameters.getGripperSelection();
		printSelectedGripper(selectedGripper);

		if (zoneAGripper.equals(selectedGripper)) {
			scriptWriter.appendLine("set_tool_digital_out(0, True)");
		} else if (zoneBGripper.equals(selectedGripper)) {
			scriptWriter.appendLine("set_tool_digital_out(1, True)");
		} else if (zoneABGripper.equals(selectedGripper)) {
			scriptWriter.appendLine("set_tool_digital_out(0, True)");
			scriptWriter.appendLine("set_tool_digital_out(1, True)");
		}
	}

	@Override
	public void generateReleaseActionScript(ScriptWriter scriptWriter, ReleaseActionParameters releaseActionParameters) {
		System.out.println("Release Action :");

		printFragileHandlingSelection();
		if (fragileHandlingInput.getValue()){
			// Simulate applying fragile handling
			scriptWriter.appendLine("sleep(0.01)");
		}

		SelectableGripper selectedGripper = releaseActionParameters.getGripperSelection();
		printSelectedGripper(selectedGripper);

		if (zoneAGripper.equals(selectedGripper)) {
			scriptWriter.appendLine("set_tool_digital_out(0, False)");
		} else if (zoneBGripper.equals(selectedGripper)) {
			scriptWriter.appendLine("set_tool_digital_out(1, False)");
		} else if (zoneABGripper.equals(selectedGripper)) {
			scriptWriter.appendLine("set_tool_digital_out(0, False)");
			scriptWriter.appendLine("set_tool_digital_out(1, False)");
		}
	}

	private void printSelectedGripper(SelectableGripper selectedGripper) {
		System.out.println("Selected Gripper: " + selectedGripper.getDisplayName() + "\n");
	}

	private void printFragileHandlingSelection() {
		System.out.println("Using Fragile Handling: " + fragileHandlingInput.getValue());
	}
}