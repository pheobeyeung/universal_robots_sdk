package com.ur.urcap.examples.driver.gripper.simplegripper;

import com.ur.urcap.api.contribution.driver.general.tcp.TCPConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.CustomUserInputConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.ContributionConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.GripperAPIProvider;
import com.ur.urcap.api.contribution.driver.gripper.GripperConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripperContribution;
import com.ur.urcap.api.contribution.driver.gripper.ReleaseActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.SystemConfiguration;
import com.ur.urcap.api.domain.script.ScriptWriter;

import javax.swing.ImageIcon;
import java.util.Locale;


public class SimpleGripper implements GripperContribution {

	private static final String GRIPPER_NAME = "Simple Gripper";

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
		// Intentionally left empty
	}

	@Override
	public void generatePreambleScript(ScriptWriter scriptWriter) {
		// Intentionally left empty
	}

	@Override
	public void generateGripActionScript(ScriptWriter scriptWriter, GripActionParameters gripActionParameters) {
		scriptWriter.appendLine("set_tool_digital_out(0, False)");
		scriptWriter.appendLine("set_tool_digital_out(1, True)");
	}

	@Override
	public void generateReleaseActionScript(ScriptWriter scriptWriter, ReleaseActionParameters releaseActionParameters) {
		scriptWriter.appendLine("set_tool_digital_out(0, True)");
		scriptWriter.appendLine("set_tool_digital_out(1, False)");
	}
}