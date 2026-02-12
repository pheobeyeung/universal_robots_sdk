package com.ur.urcap.examples.pickorplaceswing.pickorplace;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class PickOrPlaceProgramNodeService
		implements SwingProgramNodeService<PickOrPlaceProgramNodeContribution, PickOrPlaceProgramNodeView> {

	@Override
	public String getId() {
		return "PickOrPlaceProgramNode";
	}

	@Override
	public String getTitle(Locale locale) {
		return "Pick or Place";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setDeprecated(false);
		configuration.setChildrenAllowed(true);
		configuration.setUserInsertable(true);
	}

	@Override
	public PickOrPlaceProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new PickOrPlaceProgramNodeView(style);
	}

	@Override
	public PickOrPlaceProgramNodeContribution createNode(ProgramAPIProvider apiProvider,
			PickOrPlaceProgramNodeView view, DataModel model, CreationContext context) {
		return new PickOrPlaceProgramNodeContribution(apiProvider, view, model, context);
	}

}
