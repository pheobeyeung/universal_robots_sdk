package com.ur.urcap.examples.style;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class Style {
	private static final int VERTICAL_SPACING = 10;
	private static final int HORIZONTAL_SPACING = 10;

	public int getVerticalSpacing() {
		return VERTICAL_SPACING;
	}

	public int getHorizontalSpacing() {
		return HORIZONTAL_SPACING;
	}

	public abstract Dimension getInputfieldDimension();

	public abstract Dimension getComboBoxDimension();
}
