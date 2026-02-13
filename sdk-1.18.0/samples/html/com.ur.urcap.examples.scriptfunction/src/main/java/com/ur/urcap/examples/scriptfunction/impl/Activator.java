package com.ur.urcap.examples.scriptfunction.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import com.ur.urcap.api.contribution.InstallationNodeService;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		ScriptFunctionInstallationNodeService scriptFunctionInstallationNodeService = new ScriptFunctionInstallationNodeService();
		context.registerService(InstallationNodeService.class, scriptFunctionInstallationNodeService, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}

