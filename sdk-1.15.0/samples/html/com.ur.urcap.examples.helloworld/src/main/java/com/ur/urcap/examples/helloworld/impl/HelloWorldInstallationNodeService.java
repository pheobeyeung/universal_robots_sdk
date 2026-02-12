package com.ur.urcap.examples.helloworld.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.domain.URCapAPI;

import java.io.InputStream;

import com.ur.urcap.api.domain.data.DataModel;

public class HelloWorldInstallationNodeService implements InstallationNodeService {

	public HelloWorldInstallationNodeService() { }

	@Override
	public InstallationNodeContribution createInstallationNode(URCapAPI api, DataModel model) {
		return new HelloWorldInstallationNodeContribution(model);
	}

	@Override
	public String getTitle() {
		return "Hello World";
	}

	@Override
	public InputStream getHTML() {
		InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/examples/helloworld/impl/installation.html");
		return is;
	}
}
