package com.ur.urcap.examples.nodeorderingswing;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.examples.nodeorderingswing.close.CloseProgramNodeService;
import com.ur.urcap.examples.nodeorderingswing.detect.DetectProgramNodeService;
import com.ur.urcap.examples.nodeorderingswing.measure.MeasureProgramNodeService;
import com.ur.urcap.examples.nodeorderingswing.open.OpenProgramNodeService;
import com.ur.urcap.examples.nodeorderingswing.search.SearchProgramNodeService;
import com.ur.urcap.examples.nodeorderingswing.zero.ZeroProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(SwingProgramNodeService.class, new CloseProgramNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new DetectProgramNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new SearchProgramNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new ZeroProgramNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new OpenProgramNodeService(), null);
		context.registerService(SwingProgramNodeService.class, new MeasureProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

