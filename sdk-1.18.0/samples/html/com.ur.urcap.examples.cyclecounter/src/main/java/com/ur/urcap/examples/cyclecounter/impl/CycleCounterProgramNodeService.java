package com.ur.urcap.examples.cyclecounter.impl;

import com.ur.urcap.api.contribution.ProgramNodeConfiguration;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.contribution.ProgramNodeServiceConfigurable;
import com.ur.urcap.api.contribution.program.configuration.debugging.ProgramDebuggingSupport;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.io.InputStream;

public class CycleCounterProgramNodeService implements ProgramNodeService, ProgramNodeServiceConfigurable {
	@Override
	public String getId() {
		return "CycleCounter";
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
	public String getTitle() {
		return "Cycle Counter";
	}

	@Override
	public InputStream getHTML() {
		return this.getClass().getResourceAsStream("/com/ur/urcap/examples/cyclecounter/impl/CycleCounterProgramNode.html");
	}

	@Override
	public ProgramNodeContribution createNode(URCapAPI api, DataModel model) {
		return new CycleCounterProgramNodeContribution(api, model);
	}

	@Override
	public void configureContribution(ProgramNodeConfiguration programNodeConfiguration) {
		ProgramDebuggingSupport programDebuggingSupport = programNodeConfiguration.getProgramDebuggingSupport();
		programDebuggingSupport.setAllowBreakpointOnChildNodesInSubtree(true);
		programDebuggingSupport.setAllowStartFromChildNodesInSubtree(true);
	}
}
