package com.ur.urcap.examples.tooliocontrol.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.resource.ControllableResourceModel;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class ToolIOControlInstallationNodeContribution implements InstallationNodeContribution {
	private final ToolIOControlInstallationNodeView view;
	private final ToolIOController toolIOController;

	public ToolIOControlInstallationNodeContribution(InstallationAPIProvider apiProvider, ToolIOControlInstallationNodeView view) {
		this.view = view;

		ControllableResourceModel resourceModel = apiProvider.getInstallationAPI().getControllableResourceModel();
		toolIOController = new ToolIOController(resourceModel, apiProvider.getSystemAPI().getCapabilityManager());
		resourceModel.requestControl(toolIOController);
	}

	@Override
	public void openView() {
		view.showHelp(!toolIOController.hasControl());
	}

	@Override
	public void closeView() {

	}

	@Override
	public void generateScript(ScriptWriter writer) {

	}

	public boolean isSetupCorrect() {
		return toolIOController.isSetupCorrect();
	}
}
