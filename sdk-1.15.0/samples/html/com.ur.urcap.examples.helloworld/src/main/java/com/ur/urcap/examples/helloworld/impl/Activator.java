package com.ur.urcap.examples.helloworld.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.contribution.ProgramNodeService;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) throws Exception {
		HelloWorldInstallationNodeService helloWorldInstallationNodeService = new HelloWorldInstallationNodeService();

		context.registerService(InstallationNodeService.class, helloWorldInstallationNodeService, null);
		context.registerService(ProgramNodeService.class, new HelloWorldProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
