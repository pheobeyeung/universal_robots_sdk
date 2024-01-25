package com.ur.urcap.examples.nodeorderingswing.open;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class OpenProgramNodeContribution implements ProgramNodeContribution {

	private final OpenProgramNodeView nodeView;

	OpenProgramNodeContribution(OpenProgramNodeView view, double displayOrderId) {
		this.nodeView = view;
		nodeView.setNodeOrderId(displayOrderId);
	}

	@Override
	public void openView() {
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return "Open";
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.writeChildren();
	}

}
