package com.ur.urcap.examples.tooliocontrol.program;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.examples.tooliocontrol.installation.ToolIOControlInstallationNodeContribution;

public class ToolIOControlProgramNodeContribution implements ProgramNodeContribution {
	private final ProgramAPIProvider apiProvider;
	private final ToolIOControlProgramNodeView view;

	public ToolIOControlProgramNodeContribution(ProgramAPIProvider apiProvider, ToolIOControlProgramNodeView view, DataModel model, CreationContext context) {
		this.apiProvider = apiProvider;
		this.view = view;
	}

	@Override
	public void openView() {
		view.showHelp(!isSetupCorrect());
	}

	@Override
	public void closeView() {

	}

	@Override
	public String getTitle() {
		return "Tool I/O Control";
	}

	@Override
	public boolean isDefined() {
		return isSetupCorrect();
	}

	private boolean isSetupCorrect() {
		ToolIOControlInstallationNodeContribution installationNode = getInstallationNode();
		return installationNode.isSetupCorrect();
	}

	private ToolIOControlInstallationNodeContribution getInstallationNode() {
		return getProgramAPI().getInstallationNode(ToolIOControlInstallationNodeContribution.class);
	}

	private ProgramAPI getProgramAPI() {
		return apiProvider.getProgramAPI();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		//Start welding torch
		writer.writeChildren();
		//Stop welding torch
	}
}
