package com.ur.urcap.examples.driver.gripper.customgrippersetup;

import com.ur.urcap.api.contribution.driver.gripper.GripperContribution;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	@Override
	public void start(final BundleContext context) {
		context.registerService(GripperContribution.class, new CustomGripperSetup(), null);
	}

	@Override
	public void stop(BundleContext context) {
	}
}

