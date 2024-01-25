package com.ur.urcap.examples.gripandrelease.style;

import java.awt.Dimension;


public abstract class Style {

	private static final int HORIZONTAL_SPACING = 10;
	private static final int VERTICAL_SPACING = 10;

	public int getHorizontalSpacing() {
		return HORIZONTAL_SPACING;
	}

	public int getVerticalSpacing() {
		return VERTICAL_SPACING;
	}

	public abstract Dimension getInputFieldSize();
}
