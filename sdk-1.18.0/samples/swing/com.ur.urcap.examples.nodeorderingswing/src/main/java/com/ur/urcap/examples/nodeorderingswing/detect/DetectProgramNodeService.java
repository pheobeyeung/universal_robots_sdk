package com.ur.urcap.examples.nodeorderingswing.detect;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.nodeorderingswing.style.Style;
import com.ur.urcap.examples.nodeorderingswing.style.V3Style;
import com.ur.urcap.examples.nodeorderingswing.style.V5Style;

import java.util.Locale;

public class DetectProgramNodeService implements SwingProgramNodeService<DetectProgramNodeContribution, DetectProgramNodeView> {

	private static final double DISPLAY_ORDER_ID = 5;

	@Override
	public String getId() {
		return "Detect";
	}

	@Override
	public void configureContribution(ContributionConfiguration contributionConfiguration) {
		contributionConfiguration.setDisplayOrderId(DISPLAY_ORDER_ID);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Detect";
	}

	@Override
	public DetectProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new DetectProgramNodeView(style);
	}

	@Override
	public DetectProgramNodeContribution createNode(ProgramAPIProvider apiProvider, DetectProgramNodeView view, DataModel model, CreationContext creationContext) {
		return new DetectProgramNodeContribution(view, DISPLAY_ORDER_ID);
	}
}
