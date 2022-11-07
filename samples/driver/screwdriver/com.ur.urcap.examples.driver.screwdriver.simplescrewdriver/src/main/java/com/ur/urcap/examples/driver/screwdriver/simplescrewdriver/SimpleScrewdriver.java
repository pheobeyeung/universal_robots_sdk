package com.ur.urcap.examples.driver.screwdriver.simplescrewdriver;

import com.ur.urcap.api.contribution.driver.general.tcp.TCPConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.CustomUserInputConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.ContributionConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverAPIProvider;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverContribution;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.SystemConfiguration;
import com.ur.urcap.api.domain.script.ScriptWriter;

import javax.swing.ImageIcon;
import java.util.Locale;


public class SimpleScrewdriver implements ScrewdriverContribution {

	@Override
	public String getTitle(Locale locale) {
		return "Simple Screwdriver";
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
		// Intentionally left empty
	}

	@Override
	public void generatePreambleScript(ScriptWriter scriptWriter) {
		// Intentionally left empty
	}

	@Override
	public void generateStartScrewdriverScript(ScriptWriter scriptWriter, ScrewdriverParameters parameters) {
		scriptWriter.appendLine("set_standard_digital_out(1, True)");
	}

	@Override
	public void generateStopScrewdriverScript(ScriptWriter scriptWriter, ScrewdriverParameters parameters) {
		scriptWriter.appendLine("set_standard_digital_out(2, False)");
	}
}