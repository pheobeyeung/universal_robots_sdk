package com.ur.urcap.examples;

import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		bundleContext.registerService(SwingToolbarService.class, new MyToolbarService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

