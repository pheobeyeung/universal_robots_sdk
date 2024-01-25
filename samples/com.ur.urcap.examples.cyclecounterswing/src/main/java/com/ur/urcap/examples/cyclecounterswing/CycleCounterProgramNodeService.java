package com.ur.urcap.examples.cyclecounterswing;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.configuration.debugging.ProgramDebuggingSupport;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.style.Style;
import com.ur.urcap.examples.style.V3Style;
import com.ur.urcap.examples.style.V5Style;

import java.util.Locale;

public class CycleCounterProgramNodeService implements SwingProgramNodeService<CycleCounterProgramNodeContribution, CycleCounterProgramNodeView> {

	@Override
	public String getId() {
		return "CycleCounterSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration contributionConfiguration) {
		contributionConfiguration.setChildrenAllowed(true);
		contributionConfiguration.setDeprecated(false);
		contributionConfiguration.setUserInsertable(true);

		ProgramDebuggingSupport programDebuggingSupport = contributionConfiguration.getProgramDebuggingSupport();
		programDebuggingSupport.setAllowBreakpointOnChildNodesInSubtree(true);
		programDebuggingSupport.setAllowStartFromChildNodesInSubtree(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Cycle Counter";
	}

	@Override
	public CycleCounterProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new CycleCounterProgramNodeView(style);
	}

	@Override
	public CycleCounterProgramNodeContribution createNode(ProgramAPIProvider apiProvider, CycleCounterProgramNodeView view, DataModel model, CreationContext creationContext) {
		return new CycleCounterProgramNodeContribution(apiProvider, view, model);
	}
}
