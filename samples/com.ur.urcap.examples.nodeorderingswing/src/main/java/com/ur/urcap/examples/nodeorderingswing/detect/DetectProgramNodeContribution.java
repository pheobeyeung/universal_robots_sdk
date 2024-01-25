package com.ur.urcap.examples.nodeorderingswing.detect;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class DetectProgramNodeContribution implements ProgramNodeContribution {

	private final DetectProgramNodeView nodeView;

	DetectProgramNodeContribution(DetectProgramNodeView view, double displayOrderId) {
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
		return "Detect";
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
