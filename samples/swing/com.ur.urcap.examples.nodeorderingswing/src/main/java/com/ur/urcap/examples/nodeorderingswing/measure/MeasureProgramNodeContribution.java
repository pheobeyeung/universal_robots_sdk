package com.ur.urcap.examples.nodeorderingswing.measure;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class MeasureProgramNodeContribution implements ProgramNodeContribution {

	private final MeasureProgramNodeView nodeView;

	MeasureProgramNodeContribution(MeasureProgramNodeView view, double displayOrderId) {
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
		return "Measure";
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
