package com.ur.urcap.examples.helloworldswing.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HelloWorldProgramNodeView implements SwingProgramNodeView<HelloWorldProgramNodeContribution>{

	private final Style style;
	private JTextField jTextField;
	private JLabel previewTitle;
	private JLabel previewMessage;

	public HelloWorldProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel jPanel, final ContributionProvider<HelloWorldProgramNodeContribution> provider) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		jPanel.add(createInfo());
		jPanel.add(createVerticalSpacing(style.getVerticalSpacing()));
		jPanel.add(createInput(provider));
		jPanel.add(createVerticalSpacing(style.getExtraLargeVerticalSpacing()));
		jPanel.add(createPreview());
	}

	private Box createInfo() {
		Box infoBox = Box.createHorizontalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		infoBox.add(new JLabel("This program node will open a popup on execution."));
		return infoBox;
	}

	private Box createInput(final ContributionProvider<HelloWorldProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		inputBox.add(new JLabel("Enter your name:"));
		inputBox.add(createHorizontalSpacing());

		jTextField = new JTextField();
		jTextField.setFocusable(false);
		jTextField.setPreferredSize(style.getInputfieldSize());
		jTextField.setMaximumSize(jTextField.getPreferredSize());
		jTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = provider.get().getKeyboardForTextField();
				keyboardInput.show(jTextField, provider.get().getCallbackForTextField());
			}
		});

		inputBox.add(jTextField);
		return inputBox;
	}

	private Box createPreview() {
		Box previewBox = Box.createVerticalBox();
		JLabel preview = new JLabel("Preview");
		preview.setFont(preview.getFont().deriveFont(Font.BOLD, style.getSmallHeaderFontSize()));

		Box titleBox = Box.createHorizontalBox();
		titleBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		titleBox.add(new JLabel("Title:"));
		titleBox.add(createHorizontalSpacing());
		previewTitle = new JLabel("my title");
		titleBox.add(previewTitle);

		Box messageBox = Box.createHorizontalBox();
		messageBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		messageBox.add(new JLabel("Message:"));
		messageBox.add(createHorizontalSpacing());
		previewMessage = new JLabel("my message");
		messageBox.add(previewMessage);

		previewBox.add(preview);
		previewBox.add(createVerticalSpacing(style.getLargeVerticalSpacing()));
		previewBox.add(titleBox);
		previewBox.add(createVerticalSpacing(style.getVerticalSpacing()));
		previewBox.add(messageBox);

		return previewBox;
	}

	private Component createVerticalSpacing(int height) {
		return Box.createRigidArea(new Dimension(0, height));
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	public void setPopupText(String popupText) {
		jTextField.setText(popupText);
	}

	public void setMessagePreview(String message) {
		previewMessage.setText(message);
	}

	public void setTitlePreview(String title) {
		previewTitle.setText(title);
	}
}
