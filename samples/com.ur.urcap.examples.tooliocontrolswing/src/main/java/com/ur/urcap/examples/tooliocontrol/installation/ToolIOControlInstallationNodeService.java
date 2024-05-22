package com.ur.urcap.examples.tooliocontrol.installation;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class ToolIOControlInstallationNodeService implements SwingInstallationNodeService<ToolIOControlInstallationNodeContribution, ToolIOControlInstallationNodeView> {

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public String getTitle(Locale locale) {
		return "Tool I/O Control";
	}

	@Override
	public ToolIOControlInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new ToolIOControlInstallationNodeView(apiProvider);
	}

	@Override
	public ToolIOControlInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, ToolIOControlInstallationNodeView view, DataModel model, CreationContext context) {
		return new ToolIOControlInstallationNodeContribution(apiProvider, view);
	}
}
