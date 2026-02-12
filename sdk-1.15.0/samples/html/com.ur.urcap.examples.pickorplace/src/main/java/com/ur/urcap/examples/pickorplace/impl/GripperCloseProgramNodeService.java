package com.ur.urcap.examples.pickorplace.impl;

import com.ur.urcap.api.contribution.NonUserInsertable;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.io.InputStream;

public class GripperCloseProgramNodeService implements ProgramNodeService, NonUserInsertable {

	public GripperCloseProgramNodeService() {
	}

	@Override
	public String getId() {
		return "GripperProgramNode";
	}

	@Override
	public String getTitle() {
		return "Gripper Close";
	}

	@Override
	public InputStream getHTML() {
		return this.getClass().getResourceAsStream("/com/ur/urcap/examples/pickorplace/impl/GripperCloseProgramNode.html");
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public boolean isChildrenAllowed() {
		return true;
	}

	@Override
	public ProgramNodeContribution createNode(URCapAPI urCapAPI, DataModel dataModel) {
		return new GripperCloseProgramNodeContribution(urCapAPI);
	}
}
