package com.ur.urcap.examples.createpayload.program;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsePayloadProgramNodeView implements SwingProgramNodeView<UsePayloadProgramNodeContribution> {

	private static final String DESCRIPTION_TXT = "<html>Change the payload value in installation</html>";
	private final String WARNING_TXT = "<html>URCap Payload not defined!<br/>Go to installation page to define the payload.</html>";
	private final String CREATE_BUTTON_TXT = "Create";
	private JLabel warningLabel;
	private ContributionProvider<UsePayloadProgramNodeContribution> contributionProvider;
	private JButton createButton;

	@Override
	public void buildUI(JPanel jPanel, final ContributionProvider<UsePayloadProgramNodeContribution> contributionProvider) {
		this.contributionProvider = contributionProvider;

		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.add(new JLabel(DESCRIPTION_TXT));
		jPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		ImageIcon warningIcon = new ImageIcon(getClass().getResource("/icons/warning.png"));

		warningLabel = new JLabel(WARNING_TXT, warningIcon, SwingConstants.LEADING);
		jPanel.add(warningLabel);
		jPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		createButton = new JButton(CREATE_BUTTON_TXT);
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contributionProvider.get().create();
			}
		});
		createButton.setEnabled(false);
		jPanel.add(createButton);
	}

	void showWarningLabel(boolean show) {
		warningLabel.setVisible(show);
	}

	void enableCreateButton(boolean enable) {
		createButton.setEnabled(enable);
	}
}
