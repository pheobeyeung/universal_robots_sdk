package com.ur.urcap.examples.pickorplace.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class GripperOpenProgramNodeContribution implements ProgramNodeContribution {

	private final URCapAPI urCapAPI;

	public GripperOpenProgramNodeContribution(URCapAPI urCapAPI) {
		this.urCapAPI = urCapAPI;
		createSubtree();
	}

	private void createSubtree(){
		ProgramModel programModel = urCapAPI.getProgramModel();
		ProgramNodeFactory nf = programModel.getProgramNodeFactory();
		TreeNode root = programModel.getRootTreeNode(this);

		try {
			root.addChild(nf.createCommentNode().setComment("Add your nodes here"));
			root.addChild(nf.createSetNode());
			root.addChild(nf.createWaitNode());
		} catch (TreeStructureException e) {
			// See e.getMessage() for explanation
		}
	}

	@Override
	public void openView() {
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return "Gripper Open";
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		//Place script for opening gripper here
		writer.writeChildren();
	}

}
