package com.ur.urcap.examples.style;

import java.awt.*;

public class V3Style extends Style {
	private static final Dimension INPUTFIELD_SIZE = new Dimension(200, 24);
	private static final Dimension COMBOBOX_SIZE = new Dimension(200, 24);

	@Override
	public Dimension getInputfieldDimension() {
		return INPUTFIELD_SIZE;
	}

	@Override
	public Dimension getComboBoxDimension() {
		return COMBOBOX_SIZE;
	}
}
