package com.ur.urcap.examples.toolchanger.program;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.toolchanger.style.Style;
import com.ur.urcap.examples.toolchanger.style.V3Style;
import com.ur.urcap.examples.toolchanger.style.V5Style;

import java.util.Locale;


public class ToolChangerProgramNodeService implements SwingProgramNodeService<ToolChangerProgramNodeContribution, ToolChangerProgramNodeView> {

	@Override
	public String getId() {
		return "ToolChangerSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(false);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Change Tool";
	}

	@Override
	public ToolChangerProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();

		return new ToolChangerProgramNodeView(style);
	}

	@Override
	public ToolChangerProgramNodeContribution createNode(
			ProgramAPIProvider apiProvider,
			ToolChangerProgramNodeView view,
			DataModel model,
			CreationContext context) {
		return new ToolChangerProgramNodeContribution(apiProvider, view, model);
	}
}
