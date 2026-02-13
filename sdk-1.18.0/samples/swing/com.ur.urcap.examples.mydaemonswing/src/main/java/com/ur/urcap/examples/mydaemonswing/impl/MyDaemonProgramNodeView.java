package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyDaemonProgramNodeView implements SwingProgramNodeView<MyDaemonProgramNodeContribution> {

	private final Style style;
	private JTextField nameInputField;
	private JLabel titlePreviewLabel;
	private JLabel messagePreviewLabel;

	public MyDaemonProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<MyDaemonProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createInfo());
		panel.add(createVerticalSpacing());

		panel.add(createInput(provider));
		panel.add(createVerticalSpacing(style.getExtraLargeVerticalSpacing()));

		panel.add(createPreviewInfo());
	}

	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		infoBox.add(new JLabel("This program node will open a popup on execution."));
		return infoBox;
	}

	private Box createInput(final ContributionProvider<MyDaemonProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputBox.add(new JLabel("Enter your name:"));
		inputBox.add(createHorizontalSpacing());

		nameInputField = new JTextField();
		nameInputField.setFocusable(false);
		nameInputField.setPreferredSize(style.getInputfieldSize());
		nameInputField.setMaximumSize(nameInputField.getPreferredSize());
		nameInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getInputForTextField();
				keyboardInput.show(nameInputField, provider.get().getCallbackForTextField());
			}
		});
		inputBox.add(nameInputField);

		return inputBox;
	}


	private Box createPreviewInfo() {
		Box box = Box.createVerticalBox();

		JLabel preview = new JLabel("Preview");
		preview.setFont(preview.getFont().deriveFont((float)style.getSmallHeaderFontSize()).deriveFont(Font.BOLD));
		box.add(preview);

		box.add(createVerticalSpacing(style.getLargeVerticalSpacing()));

		Box titleBox = Box.createHorizontalBox();
		titleBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		titleBox.add(new JLabel("Title:"));
		titleBox.add(createHorizontalSpacing());
		titlePreviewLabel = new JLabel();
		titleBox.add(titlePreviewLabel);
		box.add(titleBox);

		box.add(createVerticalSpacing());

		Box messageBox = Box.createHorizontalBox();
		messageBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		messageBox.add(new JLabel("Message:"));
		messageBox.add(createHorizontalSpacing());
		messagePreviewLabel = new JLabel();
		messageBox.add(messagePreviewLabel);
		box.add(messageBox);

		return box;
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	private Component createVerticalSpacing() {
		return createVerticalSpacing(style.getVerticalSpacing());
	}

	private Component createVerticalSpacing(int space) {
		return Box.createRigidArea(new Dimension(0, space));
	}

	public void setNameText(String t) {
		nameInputField.setText(t);
	}

	public void setTitlePreview(String title) {
		titlePreviewLabel.setText(title);
	}

	public void setMessagePreview(String message) {
		messagePreviewLabel.setText(message);
	}
}
