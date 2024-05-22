package com.ur.urcap.examples.localizationswing.impl;

import java.awt.*;

public class V3Style extends Style {
	private static final Dimension INPUTFIELD_SIZE = new Dimension(120, 24);
	private static final Dimension COMBOBOX_SIZE = new Dimension(120, 24);

	@Override
	public Dimension getInputfieldSize() {
		return INPUTFIELD_SIZE;
	}

	@Override
	public Dimension getComboBoxSize() {
		return COMBOBOX_SIZE;
	}
}
