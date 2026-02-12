package com.ur.urcap.examples.localization.impl;

import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.api.ui.component.Translatable;
import com.ur.urcap.examples.localization.i18n.LanguagePack;
import java.util.Locale;

public class TranslatableLengthUnit implements Translatable {
	private Length.Unit unit;

	public TranslatableLengthUnit(Length.Unit unit) {
		this.unit = unit;
	}

	public Length.Unit getUnit() {
		return unit;
	}

	@Override
	public String getTranslatedText(Locale locale) {
		return LanguagePack.getInstance().getTextResource().lengthUnitName(unit);
	}
}
