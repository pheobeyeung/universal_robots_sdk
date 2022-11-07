package com.ur.urcap.examples.cyclecounterswing;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(SwingProgramNodeService.class, new CycleCounterProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

