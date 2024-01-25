package com.ur.urcap.examples.moveuntildetection;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.moveuntildetection.style.Style;
import com.ur.urcap.examples.moveuntildetection.style.V3Style;
import com.ur.urcap.examples.moveuntildetection.style.V5Style;

import java.util.Locale;

public class MoveUntilDetectionService implements SwingProgramNodeService<MoveUntilDetectionProgramNodeContribution, MoveUntilDetectionProgramNodeView> {
	@Override
	public String getId() {
		return "MoveUntilDetectionSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Move Until Detection";
	}

	@Override
	public MoveUntilDetectionProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();

		return new MoveUntilDetectionProgramNodeView(apiProvider, style);
	}

	@Override
	public MoveUntilDetectionProgramNodeContribution createNode(ProgramAPIProvider apiProvider,
																MoveUntilDetectionProgramNodeView view,
																DataModel model,
																CreationContext context) {
		return new MoveUntilDetectionProgramNodeContribution(apiProvider, view, model, context);
	}
}
