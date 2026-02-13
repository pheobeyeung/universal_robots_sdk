package com.ur.urcap.examples.localizationswing.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.localizationswing.i18n.CommandNamesResource;
import java.util.Locale;

public class LocalizationProgramNodeService implements
		SwingProgramNodeService<LocalizationProgramNodeContribution, LocalizationProgramNodeView> {

	@Override
	public String getId() {
		return "LocalizationNodeSwing";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public String getTitle(Locale locale) {
		CommandNamesResource commandNames = new CommandNamesResource(locale);
		return commandNames.nodeName();
	}

	@Override
	public LocalizationProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new LocalizationProgramNodeView(apiProvider, style);
	}

	@Override
	public LocalizationProgramNodeContribution createNode(
			ProgramAPIProvider apiProvider,
			LocalizationProgramNodeView view,
			DataModel model,
			CreationContext context) {
		return new LocalizationProgramNodeContribution(apiProvider, view, model);
	}
}
