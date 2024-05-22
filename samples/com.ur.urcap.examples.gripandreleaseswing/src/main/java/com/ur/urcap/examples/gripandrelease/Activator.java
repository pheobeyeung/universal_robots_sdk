package com.ur.urcap.examples.gripandrelease;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.examples.gripandrelease.program.GripAndReleaseProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) {
		context.registerService(SwingProgramNodeService.class, new GripAndReleaseProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) {
	}
}
