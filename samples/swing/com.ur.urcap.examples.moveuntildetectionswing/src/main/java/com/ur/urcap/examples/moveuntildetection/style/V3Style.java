package com.ur.urcap.examples.moveuntildetection.style;

import java.awt.Dimension;


public class V3Style extends Style {
	private static final Dimension INPUT_FIELD_SIZE = new Dimension(200, 24);
	private static final Dimension SMALL_INPUT_FIELD_SIZE = new Dimension(120, 24);
	private static final Dimension SMALL_INPUT_FIELD_LABEL_SIZE = new Dimension(30, 24);

	@Override
	public Dimension getInputFieldSize() {
		return INPUT_FIELD_SIZE;
	}

	@Override
	public Dimension getSmallInputFieldSize() {
		return SMALL_INPUT_FIELD_SIZE;
	}

	@Override
	public Dimension getSmallInputFieldLabelSize() {
		return SMALL_INPUT_FIELD_LABEL_SIZE;
	}

	@Override
	public int getContentIndent() {
		return 0;
	}

}
