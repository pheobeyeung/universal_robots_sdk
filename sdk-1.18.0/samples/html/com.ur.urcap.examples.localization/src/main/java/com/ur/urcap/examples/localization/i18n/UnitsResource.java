package com.ur.urcap.examples.localization.i18n;

import com.ur.urcap.api.domain.value.simple.Length;
import java.util.Locale;
import java.util.ResourceBundle;

public class UnitsResource {
	private final static String fileName = "com/ur/urcap/examples/localization/impl/i18n/units/units";
	private ResourceBundle resource;

	public UnitsResource(Locale locale) {
		locale = locale.equals(LanguagePack.rootLanguageLocale) ? Locale.ROOT : locale;
		resource = ResourceBundle.getBundle(fileName, locale, new UTF8Control());
	}

	public String UNIT_in() {
		return getStringByKey("UNIT_in");
	}

	public String UNIT_MM() {
		return getStringByKey("UNIT_mm");
	}

	public String lengthUnit(Length.Unit unit) {
		if (unit == Length.Unit.MM) {
			return UNIT_MM();
		} else {
			return UNIT_in();
		}
	}

	private String getStringByKey(String key) {
		try {
			return resource.getString(key);
		} catch (Exception e) {
			return "!"+key;
		}
	}
}
