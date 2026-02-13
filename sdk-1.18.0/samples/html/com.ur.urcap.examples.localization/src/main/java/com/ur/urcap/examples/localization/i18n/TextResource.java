package com.ur.urcap.examples.localization.i18n;

import com.ur.urcap.api.domain.system.localization.UnitType;
import com.ur.urcap.api.domain.value.simple.Length;
import java.util.Locale;
import java.util.ResourceBundle;

public class TextResource {
	private final static String fileName = "com/ur/urcap/examples/localization/impl/i18n/text/text";
	private ResourceBundle resource;

	public TextResource(Locale locale) {
		locale = locale.equals(LanguagePack.rootLanguageLocale) ? Locale.ROOT : locale;
		resource = ResourceBundle.getBundle(fileName, locale, new UTF8Control());
	}

	public String language() {
		return getStringByKey("Language");
	}

	public String programmingLanguage() {
		return getStringByKey("ProgrammingLanguage");
	}

	public String units() {
		return getStringByKey("setupUnitsTitle");
	}

	public String example() {
		return getStringByKey("UnitsExample");
	}

	public String nodeDescription() {
		return getStringByKey("NodeDescription");
	}

	public String enterValue() {
		return getStringByKey("EnterValue");
	}

	public String preview() {
		return getStringByKey("Preview");
	}

	public String notSupported() {
		return getStringByKey("NotSupported");
	}

	public String systemOfMeasurement(UnitType unitType) {
		if (unitType == UnitType.METRIC) {
			return getStringByKey("Metric_Units");
		} else {
			return getStringByKey("US_Units");
		}
	}

	public String lengthUnitName(Length.Unit unit) {
		 if (unit == Length.Unit.MM) {
			return getStringByKey("Millimeter");
		} else {
			return getStringByKey("Inch");
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
