package com.ur.urcap.examples.createpayload;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.examples.createpayload.installation.CreatePayloadInstallationNodeService;
import com.ur.urcap.examples.createpayload.program.UsePayloadProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) {
		context.registerService(SwingInstallationNodeService.class, new CreatePayloadInstallationNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new UsePayloadProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) {
	}
}
