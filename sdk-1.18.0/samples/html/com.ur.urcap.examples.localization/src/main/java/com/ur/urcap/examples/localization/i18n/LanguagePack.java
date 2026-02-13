package com.ur.urcap.examples.localization.i18n;

import com.ur.urcap.api.domain.system.localization.Localization;
import com.ur.urcap.api.domain.system.localization.UnitType;

import java.util.Locale;

public class LanguagePack {
	public final static Locale rootLanguageLocale = Locale.ENGLISH;
	private static LanguagePack instance;
	private Localization localization;
	private TextResource textResource;
	private CommandNamesResource commandNamesResource;
	private UnitsResource unitsResource;
	private LanguageResource languageResource;
	private UnitType unitType;

	public static LanguagePack getInstance() {
		return instance;
	}

	static {
		instance = new LanguagePack();
	}

	private LanguagePack() {
		createDefaultLocalization();
	}

	private void createDefaultLocalization() {
		createTextResource(Locale.ROOT);
		createCommandNamesResource(Locale.ROOT);
		createUnitsResource(Locale.ROOT);
		createLanguageResource(Locale.ROOT, Locale.ROOT);
		unitType = UnitType.METRIC;
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setEnglishLocalization() {
		createDefaultLocalization();
	}

	public void setLocalization(Localization localization) {
		this.localization = localization;
		createTextResource(localization.getLocale());
		createLanguageResource(localization.getLocale(), localization.getLocaleForProgrammingLanguage());
		createCommandNamesResource(localization.getLocaleForProgrammingLanguage());
		createUnitsResource(localization.getLocale());
		unitType = localization.getUnitType();
	}

	public Localization getLocalization() {
		return localization;
	}

	public LanguageResource getLanguageResource() {
		return languageResource;
	}

	public TextResource getTextResource() {
		return textResource;
	}

	public CommandNamesResource getCommandNamesResource() {
		return commandNamesResource;
	}

	public UnitsResource getUnitsResource() {
		return unitsResource;
	}

	private void createTextResource(Locale locale) {
		textResource = new TextResource(locale);
	}

	private void createCommandNamesResource(Locale locale) {
		commandNamesResource = new CommandNamesResource(locale);
	}

	private void createUnitsResource(Locale locale) {
		unitsResource = new UnitsResource(locale);
	}

	private void createLanguageResource(Locale locale, Locale programmingLanguageLocale) {
		languageResource = new LanguageResource(locale, programmingLanguageLocale);
	}
}
