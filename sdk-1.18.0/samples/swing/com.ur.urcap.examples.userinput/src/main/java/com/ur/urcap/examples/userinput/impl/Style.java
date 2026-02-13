package com.ur.urcap.examples.userinput.impl;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class Style {
	private static final int VERTICAL_SPACING = 10;
	private static final int HORIZONTAL_SPACING = 10;
	private static final Color SUCCESS_BACKGROUND = new Color(194, 246, 214);
	private static final Border SUCCESS_BORDER = BorderFactory.createLineBorder(new Color(134, 222, 136));
	private static final Color ERROR_BACKGROUND = new Color(237, 206, 206);
	private static final Border ERROR_BORDER = BorderFactory.createLineBorder(new Color(236, 165, 165));
	private static final Color WARNING_BACKGROUND = new Color(251, 238, 198);
	private static final Border WARNING_BORDER = BorderFactory.createLineBorder(new Color(235, 202, 132));

	public int getVerticalSpacing() {
		return VERTICAL_SPACING;
	}

	public int getHorizontalSpacing() {
		return HORIZONTAL_SPACING;
	}

	public Color getSuccessBackground() {
		return SUCCESS_BACKGROUND;
	}

	public Border getSuccessBorder() {
		return SUCCESS_BORDER;
	}

	public Color getErrorBackground() {
		return ERROR_BACKGROUND;
	}

	public Border getErrorBorder() {
		return ERROR_BORDER;
	}

	public Color getWarningBackground() {
		return WARNING_BACKGROUND;
	}

	public Border getWarningBorder() {
		return WARNING_BORDER;
	}

	public abstract Dimension getInputfieldDimension();
}
