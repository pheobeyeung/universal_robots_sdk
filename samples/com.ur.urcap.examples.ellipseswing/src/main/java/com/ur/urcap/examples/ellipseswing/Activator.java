package com.ur.urcap.examples.ellipseswing;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;

public class Activator implements BundleActivator {

	@Override
	public void start(final BundleContext context) throws Exception {
		context.registerService(SwingProgramNodeService.class, new EllipseProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}
