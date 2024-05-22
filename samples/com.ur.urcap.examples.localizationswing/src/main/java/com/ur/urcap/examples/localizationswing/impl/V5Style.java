package com.ur.urcap.examples.localizationswing.impl;

import java.awt.*;

public class V5Style extends Style {
	private static final Dimension INPUTFIELD_SIZE = new Dimension(130, 30);
	private static final Dimension COMBOBOX_SIZE = new Dimension(130, 30);

	@Override
	public Dimension getInputfieldSize() {
		return INPUTFIELD_SIZE;
	}

	@Override
	public Dimension getComboBoxSize() {
		return COMBOBOX_SIZE;
	}
}
