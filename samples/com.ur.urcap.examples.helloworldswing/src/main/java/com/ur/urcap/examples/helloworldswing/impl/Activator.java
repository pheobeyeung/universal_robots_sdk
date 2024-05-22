package com.ur.urcap.examples.helloworldswing.impl;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) throws Exception {
		context.registerService(SwingInstallationNodeService.class, new HelloWorldInstallationNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new HelloWorldProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
