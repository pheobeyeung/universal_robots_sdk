package com.ur.urcap.examples.localization.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class CommandNamesResource {
	private final static String fileName = "com/ur/urcap/examples/localization/impl/i18n/commandnames/command_names";
	private ResourceBundle resource;

	public CommandNamesResource(Locale locale) {
		locale = locale.equals(LanguagePack.rootLanguageLocale) ? Locale.ROOT : locale;
		resource = ResourceBundle.getBundle(fileName, locale, new UTF8Control());
	}

	public String nodeName() {
		return getStringByKey("NodeName");
	}

	private String getStringByKey(String key) {
		try {
			return resource.getString(key);
		} catch (Exception e) {
			return "!"+key;
		}
	}
}
