package com.ur.urcap.examples.nodeorderingswing.measure;

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

public class MeasureProgramNodeService implements SwingProgramNodeService<MeasureProgramNodeContribution, MeasureProgramNodeView> {

	private static final double DISPLAY_ORDER_ID = 6;

	@Override
	public String getId() {
		return "Measure";
	}

	@Override
	public void configureContribution(ContributionConfiguration contributionConfiguration) {
		contributionConfiguration.setDisplayOrderId(DISPLAY_ORDER_ID);
	}

	@Override
	public String getTitle(Locale locale) {
		return "Measure";
	}

	@Override
	public MeasureProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new MeasureProgramNodeView(style);
	}

	@Override
	public MeasureProgramNodeContribution createNode(ProgramAPIProvider apiProvider, MeasureProgramNodeView view, DataModel model, CreationContext creationContext) {
		return new MeasureProgramNodeContribution(view, DISPLAY_ORDER_ID);
	}
}
