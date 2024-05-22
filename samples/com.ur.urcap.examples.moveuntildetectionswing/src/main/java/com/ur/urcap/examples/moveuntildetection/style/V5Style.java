package com.ur.urcap.examples.moveuntildetection.style;

import java.awt.Dimension;


public class V5Style extends Style {
	private static final Dimension INPUTFIELD_SIZE = new Dimension(290, 30);
	private static final Dimension SMALL_INPUT_FIELD_SIZE = new Dimension(120, 30);
	private static final Dimension SMALL_INPUT_FIELD_LABEL_SIZE = new Dimension(30, 30);
	private static final int CONTENT_INDENT = 10;

	@Override
	public Dimension getInputFieldSize() {
		return INPUTFIELD_SIZE;
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
		return CONTENT_INDENT;
	}

}
