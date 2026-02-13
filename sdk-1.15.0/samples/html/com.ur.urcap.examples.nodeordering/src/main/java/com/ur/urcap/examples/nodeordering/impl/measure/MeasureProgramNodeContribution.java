package com.ur.urcap.examples.nodeordering.impl.measure;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.LabelComponent;

public class MeasureProgramNodeContribution implements ProgramNodeContribution {

	@Label(id = "orderIdValue")
	private LabelComponent orderIdValue;

	private final double displayOrderId;

	public MeasureProgramNodeContribution(double displayOrderId) {
		this.displayOrderId = displayOrderId;
	}

	@Override
	public void openView() {
		orderIdValue.setText(Double.toString(displayOrderId));
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
