package com.ur.urcap.examples.pickorplace.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.io.InputStream;

public class PickOrPlaceProgramNodeService implements ProgramNodeService {

	@Override
	public String getId() {
		return "PickOrPlaceProgramNode";
	}

	@Override
	public String getTitle() {
		return "Pick or Place";
	}

	@Override
	public InputStream getHTML() {
		return this.getClass().getResourceAsStream("/com/ur/urcap/examples/pickorplace/impl/PickOrPlaceProgramNode.html");
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
		return new PickOrPlaceProgramNodeContribution(urCapAPI, dataModel);
	}
}
