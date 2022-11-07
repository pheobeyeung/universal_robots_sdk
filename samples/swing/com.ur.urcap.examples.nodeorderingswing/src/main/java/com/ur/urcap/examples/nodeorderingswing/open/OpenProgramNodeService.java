package com.ur.urcap.examples.nodeorderingswing.open;

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

public class OpenProgramNodeService implements SwingProgramNodeService<OpenProgramNodeContribution, OpenProgramNodeView> {

	private static final double DISPLAY_ORDER_ID = 1;

	@Override
	public String getId() {
		return "OpenSwing";
	}

	@Override
	public void configureContribution(ContributionConfiguration contributionConfiguration) {
		contributionConfiguration.setDisplayOrderId(DISPLAY_ORDER_ID);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Open";
	}

	@Override
	public OpenProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new OpenProgramNodeView(style);
	}

	@Override
	public OpenProgramNodeContribution createNode(ProgramAPIProvider apiProvider, OpenProgramNodeView view, DataModel model, CreationContext creationContext) {
		return new OpenProgramNodeContribution(view, DISPLAY_ORDER_ID);
	}
}
