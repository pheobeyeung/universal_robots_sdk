package com.ur.urcap.examples.gripandrelease.program;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.gripandrelease.style.Style;
import com.ur.urcap.examples.gripandrelease.style.V3Style;
import com.ur.urcap.examples.gripandrelease.style.V5Style;

import java.util.Locale;


public class GripAndReleaseProgramNodeService implements SwingProgramNodeService<GripAndReleaseProgramNodeContribution, GripAndReleaseProgramNodeView> {

	@Override
	public String getId() {
		return "GripAndReleaseSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Grip And Release";
	}

	@Override
	public GripAndReleaseProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();

		return new GripAndReleaseProgramNodeView(style);
	}

	@Override
	public GripAndReleaseProgramNodeContribution createNode(
			ProgramAPIProvider apiProvider,
			GripAndReleaseProgramNodeView view,
			DataModel model,
			CreationContext context) {
		return new GripAndReleaseProgramNodeContribution(apiProvider, view, model);
	}
}
