package com.ur.urcap.examples.tooliocontrol;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.examples.tooliocontrol.installation.ToolIOControlInstallationNodeService;
import com.ur.urcap.examples.tooliocontrol.program.ToolIOControlProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		bundleContext.registerService(SwingInstallationNodeService.class, new ToolIOControlInstallationNodeService(), null);
		bundleContext.registerService(SwingProgramNodeService.class, new ToolIOControlProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}
