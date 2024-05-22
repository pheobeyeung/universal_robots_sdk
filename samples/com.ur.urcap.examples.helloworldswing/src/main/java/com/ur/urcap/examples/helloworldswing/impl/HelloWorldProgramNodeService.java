package com.ur.urcap.examples.helloworldswing.impl;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class HelloWorldProgramNodeService implements SwingProgramNodeService<HelloWorldProgramNodeContribution, HelloWorldProgramNodeView> {

	@Override
	public String getId() {
		return "HelloWorldSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public String getTitle(Locale locale) {
		String title = "Hello World";
		if ("ru".equals(locale.getLanguage())) {
			title = "Привет мир";
		} else if ("de".equals(locale.getLanguage())) {
			title = "Hallo Welt";
		}
		return title;
	}

	@Override
	public HelloWorldProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new HelloWorldProgramNodeView(style);
	}

	@Override
	public HelloWorldProgramNodeContribution createNode(
			ProgramAPIProvider apiProvider,
			HelloWorldProgramNodeView view,
			DataModel model,
			CreationContext context) {
		return new HelloWorldProgramNodeContribution(apiProvider, view, model);
	}
}
