package com.ur.urcap.examples.localizationswing.impl;

import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.examples.localizationswing.i18n.TextResource;

public class TranslatableLengthUnit {
	private TextResource textResource;
	private Length.Unit unit;

	public TranslatableLengthUnit(TextResource textResource, Length.Unit unit) {
		this.textResource = textResource;
		this.unit = unit;
	}

	public Length.Unit getUnit() {
		return unit;
	}

	@Override
	public String toString() {
		return textResource.lengthUnitName(unit);
	}
}