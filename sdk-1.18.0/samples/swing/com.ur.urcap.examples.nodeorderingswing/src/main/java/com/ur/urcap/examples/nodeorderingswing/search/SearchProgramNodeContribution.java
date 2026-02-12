package com.ur.urcap.examples.nodeorderingswing.search;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class SearchProgramNodeContribution implements ProgramNodeContribution {

	private final SearchProgramNodeView nodeView;

	SearchProgramNodeContribution(SearchProgramNodeView view, double displayOrderId) {
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
		return "Search";
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
