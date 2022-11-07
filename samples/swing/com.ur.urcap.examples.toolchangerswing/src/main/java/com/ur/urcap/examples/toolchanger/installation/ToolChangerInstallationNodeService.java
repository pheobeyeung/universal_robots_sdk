package com.ur.urcap.examples.toolchanger.installation;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.toolchanger.style.Style;
import com.ur.urcap.examples.toolchanger.style.V3Style;
import com.ur.urcap.examples.toolchanger.style.V5Style;

import java.util.Locale;


public class ToolChangerInstallationNodeService implements SwingInstallationNodeService<ToolChangerInstallationNodeContribution, ToolChangerInstallationNodeView> {

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public String getTitle(Locale locale) {
		return "Tool Changer";
	}

	@Override
	public ToolChangerInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();

		return new ToolChangerInstallationNodeView(style);
	}

	@Override
	public ToolChangerInstallationNodeContribution createInstallationNode(
			InstallationAPIProvider apiProvider,
			ToolChangerInstallationNodeView view,
			DataModel model,
			CreationContext context) {
		return new ToolChangerInstallationNodeContribution(apiProvider, model, view);
	}
}
