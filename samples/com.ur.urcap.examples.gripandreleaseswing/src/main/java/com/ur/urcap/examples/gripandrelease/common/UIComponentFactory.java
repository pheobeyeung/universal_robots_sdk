package com.ur.urcap.examples.gripandrelease.common;

import com.ur.urcap.examples.gripandrelease.style.Style;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Component;
import java.awt.Dimension;


public final class UIComponentFactory {

	private final Style style;

	public UIComponentFactory(Style style) {
		this.style = style;
	}

	public Box createInfoSection(String infoText) {
		Box infoBox = Box.createHorizontalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel infoLabel = new JLabel(infoText);
		infoBox.add(infoLabel);

		return infoBox;
	}

	public Box createHeaderSection(String headerText) {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);

		JTextPane pane = createTextPane(headerText);
		pane.setBackground(section.getBackground());
		section.add(pane);

		return section;
	}

	public JTextPane createTextPane(String info) {
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attributeSet, 0.5f);
		StyleConstants.setLeftIndent(attributeSet, 0f);

		JTextPane pane = new JTextPane();
		pane.setBorder(BorderFactory.createEmptyBorder());
		pane.setParagraphAttributes(attributeSet, false);
		pane.setText(info);
		pane.setEditable(false);
		pane.setMaximumSize(pane.getPreferredSize());

		return pane;
	}

	public Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	public Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, style.getVerticalSpacing()));
	}

}
