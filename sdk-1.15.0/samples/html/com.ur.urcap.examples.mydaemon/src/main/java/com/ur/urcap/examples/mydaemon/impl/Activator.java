package com.ur.urcap.examples.mydaemon.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.contribution.DaemonService;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) throws Exception {
		MyDaemonDaemonService daemonService = new MyDaemonDaemonService();
		MyDaemonInstallationNodeService installationNodeService = new MyDaemonInstallationNodeService(daemonService);

		context.registerService(InstallationNodeService.class, installationNodeService, null);
		context.registerService(ProgramNodeService.class, new MyDaemonProgramNodeService(), null);
		context.registerService(DaemonService.class, daemonService, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
