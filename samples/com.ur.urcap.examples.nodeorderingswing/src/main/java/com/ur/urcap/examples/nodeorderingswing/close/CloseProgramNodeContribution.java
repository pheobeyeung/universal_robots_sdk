package com.ur.urcap.examples.nodeorderingswing.close;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class CloseProgramNodeContribution implements ProgramNodeContribution {

	private final CloseProgramNodeView nodeView;

	CloseProgramNodeContribution(CloseProgramNodeView view, double displayOrderId) {
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
		return "Close";
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
