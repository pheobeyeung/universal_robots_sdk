package com.ur.urcap.examples.style;

import java.awt.*;

public class V5Style extends Style {
	private static final Dimension INPUTFIELD_SIZE = new Dimension(200, 30);
	private static final Dimension COMBOBOX_SIZE = new Dimension(250, 30);

	@Override
	public Dimension getInputfieldDimension() {
		return INPUTFIELD_SIZE;
	}

	@Override
	public Dimension getComboBoxDimension() {
		return COMBOBOX_SIZE;
	}
}
