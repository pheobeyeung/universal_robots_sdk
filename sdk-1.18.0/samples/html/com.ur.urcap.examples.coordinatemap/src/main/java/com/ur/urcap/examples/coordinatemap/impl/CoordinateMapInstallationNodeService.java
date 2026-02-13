package com.ur.urcap.examples.coordinatemap.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.io.InputStream;

public class CoordinateMapInstallationNodeService implements InstallationNodeService {

	public CoordinateMapInstallationNodeService() { }

	@Override
	public InstallationNodeContribution createInstallationNode(URCapAPI api, DataModel model) {
		return new CoordinateMapInstallationNodeContribution(model);
	}

	@Override
	public String getTitle() {
		return "Coordinate Map";
	}

	@Override
	public InputStream getHTML() {
		InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/examples/coordinatemap/impl/installation.html");
		return is;
	}
}
