package com.ur.urcap.examples.ellipseswing;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.configuration.debugging.ProgramDebuggingSupport;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class EllipseProgramNodeService
		implements SwingProgramNodeService<EllipseProgramNodeContribution, EllipseProgramNodeView> {

	@Override
	public String getId() {
		return "Ellipse";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setDeprecated(false);
		configuration.setChildrenAllowed(true);
		configuration.setUserInsertable(true);

		ProgramDebuggingSupport programDebuggingSupport = configuration.getProgramDebuggingSupport();
		programDebuggingSupport.setAllowBreakpointOnChildNodesInSubtree(true);
		programDebuggingSupport.setAllowStartFromChildNodesInSubtree(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Ellipse";
	}

	@Override
	public EllipseProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new EllipseProgramNodeView(style);
	}

	@Override
	public EllipseProgramNodeContribution createNode(ProgramAPIProvider apiProvider, EllipseProgramNodeView view,
			DataModel model, CreationContext context) {
		return new EllipseProgramNodeContribution(apiProvider, view, model);
	}
}
