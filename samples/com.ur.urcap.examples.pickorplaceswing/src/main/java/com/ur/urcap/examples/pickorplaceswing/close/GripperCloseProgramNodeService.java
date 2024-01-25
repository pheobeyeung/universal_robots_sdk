package com.ur.urcap.examples.pickorplaceswing.close;

import java.util.Locale;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.examples.pickorplaceswing.pickorplace.Style;
import com.ur.urcap.examples.pickorplaceswing.pickorplace.V3Style;
import com.ur.urcap.examples.pickorplaceswing.pickorplace.V5Style;

public class GripperCloseProgramNodeService
		implements SwingProgramNodeService<GripperCloseProgramNodeContribution, GripperCloseProgramNodeView> {

	public GripperCloseProgramNodeService() {
	}

	@Override
	public String getTitle(Locale locale) {
		return "Gripper Close";
	}

	@Override
	public String getId() {
		return "GripperProgramNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setDeprecated(false);
		configuration.setUserInsertable(false);
		configuration.setChildrenAllowed(true);
	}

	@Override
	public GripperCloseProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new GripperCloseProgramNodeView(style);
	}

	@Override
	public GripperCloseProgramNodeContribution createNode(ProgramAPIProvider apiProvider,
			GripperCloseProgramNodeView view, DataModel model, CreationContext context) {
		return new GripperCloseProgramNodeContribution(apiProvider);
	}

}
