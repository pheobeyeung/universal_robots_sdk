package com.ur.urcap.examples.userinput.impl;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class UserInputInstallationNodeView implements SwingInstallationNodeView<UserInputInstallationNodeContribution> {

	private static final int MAXIMUM_FRACTION_DIGITS = 8;

	private final Style style;
	private JTextField ipAddress = new JTextField();
	private JTextField positiveDouble = new JTextField();
	private JTextField text = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JLabel passwordInfoLabel;
	private Box passwordInfoBox;

	public UserInputInstallationNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel jPanel, final UserInputInstallationNodeContribution installationNode) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		ipAddress.setHorizontalAlignment(JTextField.RIGHT);
		jPanel.add(createLabelInputField("IP Address: ", ipAddress, new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = installationNode.getKeyboardForIpAddress();
				keyboardInput.show(ipAddress, installationNode.getCallbackForIpAddress());
			}
		}));

		jPanel.add(createVerticalSpacing());
		positiveDouble.setHorizontalAlignment(JTextField.RIGHT);
		jPanel.add(createLabelInputField("Positive number: ", positiveDouble, new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardNumberInput<Double> keyboardInput = installationNode.getKeyboardForPositiveNumber();
				keyboardInput.show(positiveDouble, installationNode.getCallbackForPositiveNumber());
			}
		}));

		jPanel.add(createVerticalSpacing());
		jPanel.add(createLabelInputField("Text (min. 1 character): ", text, new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboard = installationNode.getKeyboardForTextField();
				keyboard.show(text, installationNode.getCallbackForTextField());
			}
		}));

		jPanel.add(createVerticalSpacing());
		Box passwordBox = createPasswordBox(installationNode);
		jPanel.add(passwordBox);

		jPanel.add(createVerticalSpacing());
		passwordInfoBox = createPasswordInfoBox();
		jPanel.add(passwordInfoBox);
		passwordInfoBox.setMaximumSize(passwordBox.getPreferredSize());
	}

	private Box createPasswordBox(final UserInputInstallationNodeContribution installationNode) {
		Box passwordBox = createLabelInputField("Set password: ", passwordField, new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = installationNode.getKeyboardForPassword();
				keyboardInput.show(passwordField, installationNode.getCallbackForPassword());
			}
		});

		JButton clearButton = new JButton("Clear");
		clearButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				installationNode.onClearPasswordClicked();
			}
		});

		JButton testPassword = new JButton("Test password");
		testPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = installationNode.getKeyboardForTestPassword();
				keyboardInput.show(passwordField, installationNode.getCallbackForTestPassword());
			}
		});

		passwordBox.add(createHorizontalSpacing());
		passwordBox.add(clearButton);
		passwordBox.add(createHorizontalSpacing());
		passwordBox.add(testPassword);
		return passwordBox;
	}

	private Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, style.getVerticalSpacing()));
	}

	private Box createLabelInputField(String label, final JTextField inputField, MouseAdapter mouseAdapter) {
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel jLabel = new JLabel(label);
		inputField.setFocusable(false);
		inputField.setPreferredSize(style.getInputfieldDimension());
		inputField.setMaximumSize(inputField.getPreferredSize());
		inputField.addMouseListener(mouseAdapter);

		horizontalBox.add(jLabel);
		horizontalBox.add(inputField);

		return horizontalBox;
	}

	private Box createPasswordInfoBox() {
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		horizontalBox.setOpaque(true);

		passwordInfoLabel = new JLabel();

		horizontalBox.add(Box.createHorizontalGlue());
		horizontalBox.add(passwordInfoLabel);
		horizontalBox.add(Box.createHorizontalGlue());

		return horizontalBox;
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	public void setIpAddress(String value) {
		ipAddress.setText(value);
	}

	public void setPositiveDouble(Double value) {
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(MAXIMUM_FRACTION_DIGITS);
		String stringValue = df.format((double) value);
		positiveDouble.setText(stringValue);
	}

	public void setText(String value) {
		text.setText(value);
	}

	public void setPasswordField(String value) {
		passwordField.setText(value);
	}

	public void setPasswordIsCorrect() {
		passwordInfoLabel.setText("Password is correct");
		passwordInfoBox.setBackground(style.getSuccessBackground());
		passwordInfoBox.setBorder(style.getSuccessBorder());
		passwordInfoBox.setVisible(true);
	}

	public void setPasswordIsNotCorrect() {
		passwordInfoLabel.setText("Password is NOT correct");
		passwordInfoBox.setBackground(style.getErrorBackground());
		passwordInfoBox.setBorder(style.getErrorBorder());
		passwordInfoBox.setVisible(true);
	}

	public void setPasswordNotSet() {
		passwordInfoLabel.setText("Password is not set");
		passwordInfoBox.setBackground(style.getWarningBackground());
		passwordInfoBox.setBorder(style.getWarningBorder());
		passwordInfoBox.setVisible(true);
	}

	public void hidePasswordInfo() {
		passwordInfoBox.setVisible(false);
	}
}
