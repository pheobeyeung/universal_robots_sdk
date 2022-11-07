package com.ur.urcap.examples.createfeature;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.examples.createfeature.installation.CreateFeatureInstallationNodeService;
import com.ur.urcap.examples.createfeature.program.UseFeatureProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(final BundleContext context) {
		context.registerService(SwingInstallationNodeService.class, new CreateFeatureInstallationNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new UseFeatureProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) {
	}
}
