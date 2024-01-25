package com.ur.urcap.examples.tooliocontrol.program;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class ToolIOControlProgramNodeService implements SwingProgramNodeService<ToolIOControlProgramNodeContribution, ToolIOControlProgramNodeView> {
	@Override
	public String getId() {
		return "ToolIOControl";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Tool I/O Control";
	}

	@Override
	public ToolIOControlProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new ToolIOControlProgramNodeView(apiProvider);
	}

	@Override
	public ToolIOControlProgramNodeContribution createNode(ProgramAPIProvider apiProvider, ToolIOControlProgramNodeView view, DataModel model, CreationContext context) {
		return new ToolIOControlProgramNodeContribution(apiProvider, view, model, context);
	}
}
