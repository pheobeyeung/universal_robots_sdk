package com.ur.urcap.examples.moveuntildetection.style;

import java.awt.Color;
import java.awt.Dimension;


public abstract class Style {

	private static final Color UNDEFINED_WARNING_COLOR = new Color(230, 206, 76);

	private static final int SMALL_HORIZONTAL_SPACING = 5;
	private static final int HORIZONTAL_SPACING = 10;
	private static final int VERTICAL_SPACING = 10;
	private static final int SMALL_VERTICAL_SPACING = 5;

	public int getHorizontalSpacing() {
		return HORIZONTAL_SPACING;
	}

	public int getSmallHorizontalSpacing() {
		return SMALL_HORIZONTAL_SPACING;
	}

	public int getVerticalSpacing() {
		return VERTICAL_SPACING;
	}

	public int getSmallVerticalSpacing() {
		return SMALL_VERTICAL_SPACING;
	}

	public abstract Dimension getInputFieldSize();

	public abstract Dimension getSmallInputFieldSize();

	public abstract Dimension getSmallInputFieldLabelSize();

	public Color getUndefinedWarningColor() {
		return UNDEFINED_WARNING_COLOR;
	}


	public abstract int getContentIndent();
}
