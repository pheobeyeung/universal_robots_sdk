package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class MyDaemonInstallationNodeService implements SwingInstallationNodeService<MyDaemonInstallationNodeContribution, MyDaemonInstallationNodeView> {

	private final MyDaemonDaemonService daemonService;

	public MyDaemonInstallationNodeService(MyDaemonDaemonService daemonService) {
		this.daemonService = daemonService;
	}

	@Override
	public String getTitle(Locale locale) {
		return "My Daemon";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public MyDaemonInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new MyDaemonInstallationNodeView(style);
	}

	@Override
	public MyDaemonInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, MyDaemonInstallationNodeView view, DataModel model, CreationContext context) {
		return new MyDaemonInstallationNodeContribution(apiProvider, view, model, daemonService, context);
	}

}
