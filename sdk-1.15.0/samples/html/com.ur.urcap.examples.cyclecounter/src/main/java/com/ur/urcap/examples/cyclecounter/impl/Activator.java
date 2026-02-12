package com.ur.urcap.examples.cyclecounter.impl;

import com.ur.urcap.api.contribution.ProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(ProgramNodeService.class, new CycleCounterProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

