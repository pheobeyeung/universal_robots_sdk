package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyDaemonInstallationNodeView implements SwingInstallationNodeView<MyDaemonInstallationNodeContribution> {

	private final Style style;
	private JTextField popupInputField;
	private JButton startButton;
	private JButton stopButton;
	private JLabel statusLabel;

	public MyDaemonInstallationNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, MyDaemonInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createInfo());
		panel.add(createVerticalSpacing());

		panel.add(createInput(contribution));
		panel.add(createVerticalSpacing(style.getLargeVerticalSpacing()));

		panel.add(createStartStopButtons(contribution));
		panel.add(createVerticalSpacing());

		panel.add(createStatusInfo());
	}

	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JTextPane pane = new JTextPane();
		pane.setBorder(BorderFactory.createEmptyBorder());
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attributeSet, 0.5f);
		StyleConstants.setLeftIndent(attributeSet, 0f);
		pane.setParagraphAttributes(attributeSet, false);
		pane.setText("The popup title below is shared between all My Daemon program nodes.\nThe title cannot be empty.");
		pane.setEditable(false);
		pane.setMaximumSize(pane.getPreferredSize());
		pane.setBackground(infoBox.getBackground());
		infoBox.add(pane);
		return infoBox;
	}

	private Box createInput(final MyDaemonInstallationNodeContribution contribution) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		inputBox.add(new JLabel("Popup title:"));
		inputBox.add(createHorizontalSpacing());

		popupInputField = new JTextField();
		popupInputField.setFocusable(false);
		popupInputField.setPreferredSize(style.getInputfieldSize());
		popupInputField.setMaximumSize(popupInputField.getPreferredSize());
		popupInputField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = contribution.getInputForTextField();
				keyboardInput.show(popupInputField, contribution.getCallbackForTextField());
			}
		});
		inputBox.add(popupInputField);

		return inputBox;
	}

	private Box createStartStopButtons(final MyDaemonInstallationNodeContribution contribution) {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		startButton = new JButton("Start Daemon");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.onStartClick();
			}
		});
		box.add(startButton);

		box.add(createHorizontalSpacing());

		stopButton = new JButton("Stop Daemon");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.onStopClick();
			}
		});
		box.add(stopButton);

		return box;
	}

	private Box createStatusInfo() {
		Box box = Box.createHorizontalBox();
		box.setAlignmentX(Component.LEFT_ALIGNMENT);

		statusLabel = new JLabel("My Daemon status");
		box.add(statusLabel);
		return box;
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	private Component createVerticalSpacing(int space) {
		return Box.createRigidArea(new Dimension(0, space));
	}

	private Component createVerticalSpacing() {
		return createVerticalSpacing(style.getVerticalSpacing());
	}

	public void setPopupText(String t) {
		popupInputField.setText(t);
	}

	public void setStartButtonEnabled(boolean enabled) {
		startButton.setEnabled(enabled);
	}

	public void setStopButtonEnabled(boolean enabled) {
		stopButton.setEnabled(enabled);
	}

	public void setStatusLabel(String text) {
		statusLabel.setText(text);
	}
}
