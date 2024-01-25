package com.ur.urcap.examples.localizationswing.impl;

import java.awt.*;

public abstract class Style {
	private static final int HORIZONTAL_SPACING = 10;
	private static final int VERTICAL_SPACING = 10;
	private static final int SMALL_HEADER_FONT_SIZE = 16;
	private static final int INPUT_WIDTH = 16;

	public int getHorizontalSpacing() {
		return HORIZONTAL_SPACING;
	}

	public int getVerticalSpacing() {
		return VERTICAL_SPACING;
	}

	public int getSmallHeaderFontSize() {
		return SMALL_HEADER_FONT_SIZE;
	}

	public abstract Dimension getInputfieldSize();

	public abstract Dimension getComboBoxSize();
}
