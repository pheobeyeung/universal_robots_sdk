package com.ur.urcap.examples.createfeature.program;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;


public class UseFeatureProgramNodeService implements SwingProgramNodeService<UseFeatureProgramNodeContribution, UseFeatureProgramNodeView> {

	@Override
	public String getId() {
		return "UseFeatureSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Use Feature";
	}

	@Override
	public UseFeatureProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new UseFeatureProgramNodeView();
	}

	@Override
	public UseFeatureProgramNodeContribution createNode(
			ProgramAPIProvider apiProvider,
			UseFeatureProgramNodeView view,
			DataModel model,
			CreationContext context) {
		return new UseFeatureProgramNodeContribution(apiProvider, view);
	}
}
