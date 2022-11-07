package com.ur.urcap.examples.createpayload.program;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;


public class UsePayloadProgramNodeService implements SwingProgramNodeService<UsePayloadProgramNodeContribution, UsePayloadProgramNodeView> {

	@Override
	public String getId() {
		return "UsePayloadSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Use Payload";
	}

	@Override
	public UsePayloadProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new UsePayloadProgramNodeView();
	}

	@Override
	public UsePayloadProgramNodeContribution createNode(
			ProgramAPIProvider apiProvider,
			UsePayloadProgramNodeView view,
			DataModel model,
			CreationContext context) {
		return new UsePayloadProgramNodeContribution(apiProvider, view);
	}
}
