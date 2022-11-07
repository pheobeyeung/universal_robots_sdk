package com.ur.urcap.examples.createfeature.installation;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class CreateFeatureInstallationNodeService implements SwingInstallationNodeService<CreateFeatureInstallationNodeContribution, CreateFeatureInstallationNodeView> {
	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// Intentionally left empty
	}

	@Override
	public String getTitle(Locale locale) {
		return "Create Feature";
	}

	@Override
	public CreateFeatureInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new CreateFeatureInstallationNodeView();
	}

	@Override
	public CreateFeatureInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, CreateFeatureInstallationNodeView view, DataModel model, CreationContext context) {
		return new CreateFeatureInstallationNodeContribution(apiProvider, model, view);
	}
}
