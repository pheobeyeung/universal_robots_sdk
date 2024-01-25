package com.ur.urcap.examples.createpayload.installation;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class CreatePayloadInstallationNodeService implements SwingInstallationNodeService<CreatePayloadInstallationNodeContribution, CreatePayloadInstallationNodeView> {

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		// Intentionally left empty
	}

	@Override
	public String getTitle(Locale locale) {
		return "Create Payload";
	}

	@Override
	public CreatePayloadInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new CreatePayloadInstallationNodeView();
	}

	@Override
	public CreatePayloadInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, CreatePayloadInstallationNodeView view, DataModel model, CreationContext context) {
		return new CreatePayloadInstallationNodeContribution(apiProvider, model, view);
	}
}
