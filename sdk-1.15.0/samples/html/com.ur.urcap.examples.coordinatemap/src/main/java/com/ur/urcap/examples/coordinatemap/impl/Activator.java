package com.ur.urcap.examples.coordinatemap.impl;

import com.ur.urcap.api.contribution.InstallationNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Coordinate Map activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		CoordinateMapInstallationNodeService coordinateMapInstallationNodeService = new CoordinateMapInstallationNodeService();
		bundleContext.registerService(InstallationNodeService.class, coordinateMapInstallationNodeService, null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

