package com.ur.urcap.examples.nodeordering.impl;

import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.examples.nodeordering.impl.close.CloseProgramNodeService;
import com.ur.urcap.examples.nodeordering.impl.detect.DetectProgramNodeService;
import com.ur.urcap.examples.nodeordering.impl.measure.MeasureProgramNodeService;
import com.ur.urcap.examples.nodeordering.impl.open.OpenProgramNodeService;
import com.ur.urcap.examples.nodeordering.impl.search.SearchProgramNodeService;
import com.ur.urcap.examples.nodeordering.impl.zero.ZeroProgramNodeService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		context.registerService(ProgramNodeService.class, new ZeroProgramNodeService(), null);
		context.registerService(ProgramNodeService.class, new CloseProgramNodeService(), null);
		context.registerService(ProgramNodeService.class, new OpenProgramNodeService(), null);
		context.registerService(ProgramNodeService.class, new DetectProgramNodeService(), null);
		context.registerService(ProgramNodeService.class, new MeasureProgramNodeService(), null);
		context.registerService(ProgramNodeService.class, new SearchProgramNodeService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

