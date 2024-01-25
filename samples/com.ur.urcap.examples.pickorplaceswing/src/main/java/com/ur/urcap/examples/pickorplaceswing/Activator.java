package com.ur.urcap.examples.pickorplaceswing;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.examples.pickorplaceswing.close.GripperCloseProgramNodeService;
import com.ur.urcap.examples.pickorplaceswing.open.GripperOpenProgramNodeService;
import com.ur.urcap.examples.pickorplaceswing.pickorplace.PickOrPlaceProgramNodeService;

public class Activator implements BundleActivator {

	@Override
	public void start(final BundleContext context) throws Exception {
		context.registerService(SwingProgramNodeService.class, new PickOrPlaceProgramNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new GripperOpenProgramNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new GripperCloseProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {

	}

}
