package com.ur.urcap.examples.pickorplaceswing.close;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class GripperCloseProgramNodeContribution implements ProgramNodeContribution {

	private final ProgramAPIProvider apiProvider;

	GripperCloseProgramNodeContribution(ProgramAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
		createSubtree();
	}

	@Override
	public String getTitle() {
		return "Gripper Close";
	}

	@Override
	public void openView() {
	}

	@Override
	public void closeView() {
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Place script for closing gripper here
		writer.writeChildren();
	}

	private void createSubtree() {
		ProgramAPI programAPI = apiProvider.getProgramAPI();
		ProgramModel programModel = programAPI.getProgramModel();
		ProgramNodeFactory nf = programModel.getProgramNodeFactory();
		TreeNode root = programModel.getRootTreeNode(this);

		try {
			root.addChild(nf.createCommentNode().setComment("Add your nodes here"));
			root.addChild(nf.createSetNode());
			root.addChild(nf.createWaitNode());
		} catch (TreeStructureException e) {
			e.printStackTrace();
			// See e.getMessage() for explanation
		}
	}

}
