package com.ur.urcap.examples.helloworld.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.io.InputStream;

public class HelloWorldProgramNodeService implements ProgramNodeService {

	public HelloWorldProgramNodeService() {
	}

	@Override
	public String getId() {
		return "HelloWorldNode";
	}

	@Override
	public String getTitle() {
		return "Hello World";
	}

	@Override
	public InputStream getHTML() {
		InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/examples/helloworld/impl/programnode.html");
		return is;
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
	public ProgramNodeContribution createNode(URCapAPI api, DataModel model) {
		return new HelloWorldProgramNodeContribution(api, model);
	}
}
