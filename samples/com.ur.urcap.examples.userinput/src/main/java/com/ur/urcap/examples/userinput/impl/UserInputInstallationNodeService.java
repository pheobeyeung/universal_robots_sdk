package com.ur.urcap.examples.userinput.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class UserInputInstallationNodeService implements SwingInstallationNodeService<UserInputInstallationNodeContribution, UserInputInstallationNodeView> {

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public String getTitle(Locale locale) {
		return "User Input";
	}

	@Override
	public UserInputInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new UserInputInstallationNodeView(style);
	}

	@Override
	public UserInputInstallationNodeContribution createInstallationNode(
			InstallationAPIProvider apiProvider,
			UserInputInstallationNodeView view,
			DataModel model,
			CreationContext context) {
		return new UserInputInstallationNodeContribution(apiProvider, view, model);
	}
}
