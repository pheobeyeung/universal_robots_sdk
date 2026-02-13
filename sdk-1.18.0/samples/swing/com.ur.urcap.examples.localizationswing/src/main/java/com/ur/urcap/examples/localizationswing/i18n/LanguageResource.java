package com.ur.urcap.examples.localizationswing.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageResource {
	private final static String fileName = "com/ur/urcap/examples/localizationswing/impl/i18n/languages/languages";
	private final Locale locale;
	private final Locale programmingLanguageLocale;
	private ResourceBundle resource;

	public LanguageResource(Locale locale, Locale programmingLanguageLocale) {
		this.locale = locale;
		Locale resourceLocale = locale.equals(LanguagePack.rootLanguageLocale) ? Locale.ROOT : locale;
		resource = ResourceBundle.getBundle(fileName, resourceLocale, new UTF8Control());
		this.programmingLanguageLocale = programmingLanguageLocale;
	}

	public String localeLanguage() {
		return getLanguage(this.locale);
	}

	public String localeProgrammingLanguage() {
		return getLanguage(this.programmingLanguageLocale);
	}

	public String language(String language) {
		String lang =  getStringByKey(language);
		if(lang == null ) {
			lang =  "!" + language;
		}
		return lang;
	}

	private String getStringByKey(String key) {
		try {
			return resource.getString(key);
		} catch (Exception e) {
			return null;
		}
	}

	private String getLanguage(Locale languageLocale) {
		String lanaguge = getStringByKey(languageLocale.getLanguage() + "_" + languageLocale.getCountry());
		if (lanaguge == null) {
			lanaguge = getStringByKey(languageLocale.getLanguage());
			if (lanaguge == null) {
				lanaguge = "!" + languageLocale.getLanguage();
			}
		}
		return lanaguge;
	}
}
