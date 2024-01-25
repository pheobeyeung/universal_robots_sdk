package com.ur.urcap.examples.scriptfunctionswing.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class ScriptFunctionInstallationNodeService implements
		SwingInstallationNodeService<ScriptFunctionInstallationNodeContribution, ScriptFunctionInstallationNodeView> {

	public ScriptFunctionInstallationNodeService() { }

	@Override
	public void configureContribution(com.ur.urcap.api.contribution.installation.ContributionConfiguration configuration) {

	}

	@Override
	public String getTitle(Locale locale) {
		return "Script Function";
	}

	@Override
	public ScriptFunctionInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new ScriptFunctionInstallationNodeView();
	}

	@Override
	public ScriptFunctionInstallationNodeContribution createInstallationNode(
			InstallationAPIProvider apiProvider,
			ScriptFunctionInstallationNodeView view,
			DataModel model,
			com.ur.urcap.api.contribution.installation.CreationContext context) {
		return new ScriptFunctionInstallationNodeContribution(apiProvider, view, model);
	}
}
