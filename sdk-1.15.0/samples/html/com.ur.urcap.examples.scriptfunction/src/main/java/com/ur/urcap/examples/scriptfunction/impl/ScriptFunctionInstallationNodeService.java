package com.ur.urcap.examples.scriptfunction.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.io.InputStream;

public class ScriptFunctionInstallationNodeService implements InstallationNodeService {

	public ScriptFunctionInstallationNodeService() { }

	@Override
	public InstallationNodeContribution createInstallationNode(URCapAPI api, DataModel model) {
		return new ScriptFunctionInstallationNodeContribution(api, model);
	}

	@Override
	public String getTitle() {
		return "Script Function";
	}

	@Override
	public InputStream getHTML() {
		InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/examples/scriptfunction/impl/installation.html");
		return is;
	}
}
