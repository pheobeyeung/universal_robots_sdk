package com.ur.urcap.examples.createfeature.program;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UseFeatureProgramNodeView implements SwingProgramNodeView<UseFeatureProgramNodeContribution> {

	private static final String DESCRIPTION_TXT = "<html>The child Move nodes will make a square movement around the selected feature.<br/>" +
			"Moving the selected feature will adjust this movement accordingly.</html>";
	private static final String CREATE_FEATURE_WARNING_TXT = "<html><p>Go to Create Feature in the installation and create a feature that can be used for this movement</p></html>";
	private static final String MOVE_FEATURE_WARNING_TXT = "<html><p>Could not create Move node. Try moving the feature in the installation";
	private static final String CREATE_BUTTON_TXT = "Create";

	private JLabel createFeatureWarningLabel;
	private JLabel moveFeatureWarningLabel;
	private JButton createButton;

	@Override
	public void buildUI(JPanel jPanel, final ContributionProvider<UseFeatureProgramNodeContribution> contributionProvider) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		JLabel descriptionLabel = new JLabel(DESCRIPTION_TXT);
		jPanel.add(descriptionLabel);
		jPanel.add(createVerticalSpacing());

		ImageIcon warningIcon = new ImageIcon(getClass().getResource("/icons/warning.png"));

		createFeatureWarningLabel = new JLabel(CREATE_FEATURE_WARNING_TXT, warningIcon, SwingConstants.LEADING);
		jPanel.add(createFeatureWarningLabel);
		createFeatureWarningLabel.setVisible(false);

		moveFeatureWarningLabel = new JLabel(MOVE_FEATURE_WARNING_TXT, warningIcon, SwingConstants.LEADING);
		jPanel.add(moveFeatureWarningLabel);
		moveFeatureWarningLabel.setVisible(false);

		jPanel.add(createVerticalSpacing());
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

	void showCreateFeatureWarningMessage(boolean show) {
		createFeatureWarningLabel.setVisible(show);
	}

	void showMoveFeatureWarningMessage(boolean show) {
		moveFeatureWarningLabel.setVisible(show);
	}

	void enableCreateButton(boolean enable) {
		createButton.setEnabled(enable);
	}

	private Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, 10));
	}
}
