package com.ur.urcap.examples.localizationswing.i18n;

import com.ur.urcap.api.domain.system.localization.Localization;
import com.ur.urcap.api.domain.system.localization.UnitType;

import java.util.Locale;

public class EnglishLanguagePack extends LanguagePack {

	public EnglishLanguagePack() {
		super(new Localization() {
			@Override
			public Locale getLocale() {
				return Locale.ROOT;
			}

			@Override
			public Locale getLocaleForProgrammingLanguage() {
				return Locale.ROOT;
			}

			@Override
			public UnitType getUnitType() {
				return UnitType.METRIC;
			}
		});
	}
}
