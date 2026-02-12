package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class MyDaemonProgramNodeService implements SwingProgramNodeService<MyDaemonProgramNodeContribution, MyDaemonProgramNodeView> {

	public MyDaemonProgramNodeService() {
	}

	@Override
	public String getId() {
		return "MyDaemonSwingNode";
	}

	@Override
	public String getTitle(Locale locale) {
		return "My Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public MyDaemonProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new MyDaemonProgramNodeView(style);
	}

	@Override
	public MyDaemonProgramNodeContribution createNode(ProgramAPIProvider apiProvider, MyDaemonProgramNodeView view, DataModel model, CreationContext context) {
		return new MyDaemonProgramNodeContribution(apiProvider, view, model);
	}

}
