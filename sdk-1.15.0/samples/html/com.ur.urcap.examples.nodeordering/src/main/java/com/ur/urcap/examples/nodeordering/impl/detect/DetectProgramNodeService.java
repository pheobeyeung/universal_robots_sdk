package com.ur.urcap.examples.nodeordering.impl.detect;

import com.ur.urcap.api.contribution.ProgramNodeConfiguration;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.contribution.ProgramNodeServiceConfigurable;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.io.InputStream;

public class DetectProgramNodeService implements ProgramNodeService, ProgramNodeServiceConfigurable {

	private final double DISPLAY_ORDER_ID = 5;

	@Override
	public String getId() {
		return "Detect";
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public boolean isChildrenAllowed() {
		return false;
	}

	@Override
	public String getTitle() {
		return "Detect";
	}

	@Override
	public InputStream getHTML() {
		return this.getClass().getResourceAsStream("/com/ur/urcap/examples/nodeordering/impl/NodeOrderingProgramNode.html");
	}

	@Override
	public ProgramNodeContribution createNode(URCapAPI api, DataModel model) {
		return new DetectProgramNodeContribution(DISPLAY_ORDER_ID);
	}

	@Override
	public void configureContribution(ProgramNodeConfiguration programNodeConfiguration) {
		programNodeConfiguration.setDisplayOrderId(DISPLAY_ORDER_ID);
	}
}
