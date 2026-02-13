package com.ur.urcap.examples.driver.screwdriver.customscrewdriver;

import com.ur.urcap.api.contribution.driver.general.tcp.TCPConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.CustomUserInputConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.enterableinput.StringUserInput;
import com.ur.urcap.api.contribution.driver.general.userinput.selectableinput.BooleanUserInput;
import com.ur.urcap.api.contribution.driver.screwdriver.ContributionConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverAPIProvider;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverContribution;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.SystemConfiguration;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.value.Pose;
import com.ur.urcap.api.domain.value.PoseFactory;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;

import javax.swing.ImageIcon;
import java.util.Locale;


public class CustomScrewdriver implements ScrewdriverContribution {

	private static final String SHOW_POPUP_INPUT_ID = "showPopupInputID";
	private static final String IP_ADDRESS_INPUT_ID = "IPAddressInputID";

	private BooleanUserInput showPopup;
	private StringUserInput ipAddress;

	@Override
	public String getTitle(Locale locale) {
		return "Custom Screwdriver";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setLogo(new ImageIcon(getClass().getResource("/logo/logo.png")));
	}

	@Override
	public void configureScrewdriver(ScrewdriverConfiguration screwdriverConfiguration, ScrewdriverAPIProvider screwdriverAPIProvider) {
		// Intentionally left empty
	}

	@Override
	public void configureInstallation(CustomUserInputConfiguration configurationUIBuilder,
									  SystemConfiguration systemConfiguration,
									  TCPConfiguration tcpConfiguration,
									  ScrewdriverAPIProvider apiProvider) {
		addScrewdriverTCP(tcpConfiguration, apiProvider.getPoseFactory());
		customizeInstallationScreen(configurationUIBuilder);
	}

	private void addScrewdriverTCP(TCPConfiguration tcpConfiguration, PoseFactory poseFactory) {
		Pose tcpOffset = poseFactory.createPose(0, 80, 120, 0, 0, 0, Length.Unit.MM, Angle.Unit.RAD);
		tcpConfiguration.setTCP("My_Screwdriver", tcpOffset);
	}

	private void customizeInstallationScreen(CustomUserInputConfiguration configurationUIBuilder) {
		this.showPopup = configurationUIBuilder.registerBooleanInput(SHOW_POPUP_INPUT_ID, "Show popup", true);
		this.ipAddress = configurationUIBuilder.registerIPAddressInput(IP_ADDRESS_INPUT_ID, "IP Address", "127.0.0.1");
	}

	@Override
	public void generatePreambleScript(ScriptWriter scriptWriter) {
		// Intentionally left empty
	}

	@Override
	public void generateStartScrewdriverScript(ScriptWriter scriptWriter, ScrewdriverParameters parameters) {
		if (showPopup.getValue()) {
			scriptWriter.appendLine("popup(\"IP Address: " + ipAddress.getValue() + "\",blocking=True)");
		}
		scriptWriter.appendLine("#start screwdriver at IP Address: " + ipAddress.getValue());
	}

	@Override
	public void generateStopScrewdriverScript(ScriptWriter scriptWriter, ScrewdriverParameters parameters) {
		scriptWriter.appendLine("#stop screwdriver at IP Address: " + ipAddress.getValue());
	}
}