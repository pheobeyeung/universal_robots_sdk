package com.ur.urcap.examples.toolchanger;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.examples.toolchanger.installation.ToolChangerInstallationNodeService;
import com.ur.urcap.examples.toolchanger.program.ToolChangerProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) {
		context.registerService(SwingInstallationNodeService.class, new ToolChangerInstallationNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new ToolChangerProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) {
	}
}
