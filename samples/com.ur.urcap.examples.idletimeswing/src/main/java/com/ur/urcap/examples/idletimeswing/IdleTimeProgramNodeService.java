package com.ur.urcap.examples.idletimeswing;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.style.Style;
import com.ur.urcap.examples.style.V3Style;
import com.ur.urcap.examples.style.V5Style;

import java.util.Locale;

public class IdleTimeProgramNodeService implements SwingProgramNodeService<IdleTimeProgramNodeContribution, IdleTimeProgramNodeView> {

	@Override
	public String getId() {
		return "IdleTimeSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration contributionConfiguration) {
		contributionConfiguration.setChildrenAllowed(true);
		contributionConfiguration.setDeprecated(false);
		contributionConfiguration.setUserInsertable(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Idle Time";
	}

	@Override
	public IdleTimeProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new IdleTimeProgramNodeView(style);
	}

	@Override
	public IdleTimeProgramNodeContribution createNode(ProgramAPIProvider apiProvider, IdleTimeProgramNodeView view, DataModel model, CreationContext creationContext) {
		return new IdleTimeProgramNodeContribution(apiProvider, view, model);
	}

}
