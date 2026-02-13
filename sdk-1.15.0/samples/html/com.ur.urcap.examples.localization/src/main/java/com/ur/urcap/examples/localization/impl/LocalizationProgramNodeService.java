package com.ur.urcap.examples.localization.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.URCapAPIFacade;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.system.localization.Localization;
import com.ur.urcap.examples.localization.i18n.LanguagePack;

import java.io.InputStream;

public class LocalizationProgramNodeService implements ProgramNodeService {

	@Override
	public String getId() {
		return "LocalizationNode";
	}

	@Override
	public String getTitle() {
		//This is the first time Localization is needed, setup the Language
		final Localization localization = URCapAPIFacade.getURCapAPI().getSystemSettings().getLocalization();
		LanguagePack.getInstance().setLocalization(localization);
		return LanguagePack.getInstance().getCommandNamesResource().nodeName();
	}

	@Override
	public InputStream getHTML() {
		InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/examples/localization/impl/programnode.html");
		return is;
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}

	@Override
	public boolean isChildrenAllowed() {
		return false;
	}

	@Override
	public ProgramNodeContribution createNode(URCapAPI api, DataModel model) {
		return new LocalizationProgramNodeContribution(api, model);
	}
}
