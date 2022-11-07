package com.ur.urcap.examples.moveuntildetection;

import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.examples.moveuntildetection.style.Style;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
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

	public Box createInputSection(final String label, final JTextField inputField, final String units) {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);

		section.add(new JLabel(label));
		section.add(inputField);
		section.add(createSmallHorizontalSpacing());
		section.add(new JLabel(units));

		return section;
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

	public Component createVerticalSpacing(int n) {
		return Box.createRigidArea(new Dimension(0, style.getVerticalSpacing() * n));
	}

}
