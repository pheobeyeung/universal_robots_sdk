package com.ur.urcap.examples.helloworldswing.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class HelloWorldInstallationNodeService implements SwingInstallationNodeService<HelloWorldInstallationNodeContribution, HelloWorldInstallationNodeView> {

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public String getTitle(Locale locale) {
		return "Hello World";
	}

	@Override
	public HelloWorldInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new HelloWorldInstallationNodeView(style);
	}

	@Override
	public HelloWorldInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, HelloWorldInstallationNodeView view, DataModel model, CreationContext context) {
		return new HelloWorldInstallationNodeContribution(apiProvider, model, view);
	}
}
