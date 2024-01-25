package com.ur.urcap.examples.moveuntildetection;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) {
		bundleContext.registerService(SwingProgramNodeService.class, new MoveUntilDetectionService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) {
	}
}

