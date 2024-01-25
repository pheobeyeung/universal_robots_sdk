package com.ur.urcap.examples.toolchanger.common;

import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.examples.toolchanger.style.Style;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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

	private JTextPane createTextPane(String info) {
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

	public Box createInputSection(final String label, final JTextField inputField, final String units) {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);

		section.add(createSmallInputFieldLabel(label));
		section.add(inputField);
		section.add(createSmallHorizontalSpacing());
		section.add(new JLabel(units));

		return section;
	}

	private JLabel createSmallInputFieldLabel(String label) {
		JLabel jLabel = new JLabel(label);

		jLabel.setPreferredSize(style.getSmallInputFieldLabelSize());
		jLabel.setMinimumSize(style.getSmallInputFieldLabelSize());
		jLabel.setMaximumSize(style.getSmallInputFieldLabelSize());

		return jLabel;
	}

	public JComboBox createComboBox(final ActionListener actionListener) {
		JComboBox jComboBox = new JComboBox();

		jComboBox.setPreferredSize(style.getInputFieldSize());
		jComboBox.setMaximumSize(style.getInputFieldSize());
		jComboBox.setMinimumSize(style.getInputFieldSize());

		jComboBox.addActionListener(actionListener);

		return jComboBox;
	}

	public <T> JTextField createNumberInputField(final KeyboardNumberInput<T> keyboardNumberInput,
												 final KeyboardInputCallback<T> callback,
												 final ValueProvider<T> initialValueProvider) {
		final JTextField inputField = new JTextField();

		inputField.setFocusable(false);
		inputField.setHorizontalAlignment(JTextField.RIGHT);
		inputField.setPreferredSize(style.getSmallInputFieldSize());
		inputField.setMinimumSize(style.getSmallInputFieldSize());
		inputField.setMaximumSize(style.getSmallInputFieldSize());

		inputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				keyboardNumberInput.setInitialValue(initialValueProvider.get());
				keyboardNumberInput.show(inputField, callback);
			}
		});

		return inputField;
	}

	public Component createSmallHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getSmallHorizontalSpacing(), 0));
	}

	public Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	public Component createSmallVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, style.getSmallVerticalSpacing()));
	}

	public Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, style.getVerticalSpacing()));
	}

	public Component createVerticalSpacing(int n) {
		return Box.createRigidArea(new Dimension(0, style.getVerticalSpacing() * n));
	}

}
