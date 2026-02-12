package com.ur.urcap.examples.userinput.impl;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(SwingInstallationNodeService.class, new UserInputInstallationNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
