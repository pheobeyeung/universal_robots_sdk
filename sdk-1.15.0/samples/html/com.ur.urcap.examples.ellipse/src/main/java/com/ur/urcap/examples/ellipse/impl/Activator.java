package com.ur.urcap.examples.ellipse.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import com.ur.urcap.api.contribution.ProgramNodeService;

public class Activator implements BundleActivator {

	@Override
	public void start(final BundleContext context) throws Exception {
		context.registerService(ProgramNodeService.class, new EllipseProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
