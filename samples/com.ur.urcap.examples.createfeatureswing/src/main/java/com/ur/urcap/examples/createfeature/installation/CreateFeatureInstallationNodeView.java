package com.ur.urcap.examples.createfeature.installation;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CreateFeatureInstallationNodeView implements SwingInstallationNodeView<CreateFeatureInstallationNodeContribution> {
	private static final String DESCRIPTION_TXT = "<html>Create and modify an installation feature, that will be used in the Use Feature program node.<br/> " +
			"The created feature can be inspected in PolyScopes feature screen under the installation tab</html>";
	private static final String CREATE_FEATURE_TXT = "Create feature";
	private static final String UPDATE_FEATURE_TXT = "Update feature";
	private static final String DELETE_FEATURE_TXT = "Delete feature";

	private JButton createFeatureButton;
	private JButton updateFeatureButton;
	private JButton	deleteFeatureButton;

	@Override
	public void buildUI(JPanel panel, final CreateFeatureInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		Box content = Box.createVerticalBox();
		content.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.setAlignmentY(Component.TOP_ALIGNMENT);
		content.add(createHeaderSection());
		content.add(createVerticalSpacing());
		content.add(createFeatureSection(contribution));
		panel.add(content);
	}

	void featureIsCreated(boolean featureCreated) {
		createFeatureButton.setEnabled(!featureCreated);
		updateFeatureButton.setEnabled(featureCreated);
		deleteFeatureButton.setEnabled(featureCreated);
	}

	private Component createFeatureSection(CreateFeatureInstallationNodeContribution contribution) {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);
		section.setAlignmentY(Component.TOP_ALIGNMENT);

		createFeatureButton = createCreateFeatureButton(contribution);
		section.add(createFeatureButton);
		section.add(createHorizontalSpacing());

		updateFeatureButton = createUpdateFeatureButton(contribution);
		section.add(updateFeatureButton);
		section.add(createHorizontalSpacing());

		deleteFeatureButton = createDeleteFeatureButton(contribution);
		section.add(deleteFeatureButton);

		return section;
	}

	private JButton createCreateFeatureButton(final CreateFeatureInstallationNodeContribution contribution) {
		JButton btn = new JButton(CREATE_FEATURE_TXT);
		btn.setFocusPainted(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.createFeature();
			}
		});
		return btn;
	}

	private JButton createUpdateFeatureButton(final CreateFeatureInstallationNodeContribution contribution) {
		JButton btn = new JButton(UPDATE_FEATURE_TXT);
		btn.setFocusPainted(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.updateFeature();
			}
		});
		return btn;
	}

	private JButton createDeleteFeatureButton(final CreateFeatureInstallationNodeContribution contribution) {
		JButton btn = new JButton(DELETE_FEATURE_TXT);
		btn.setFocusPainted(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.deleteFeature();
			}
		});
		return btn;
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(10, 0));
	}

	private Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, 15));
	}

	private Box createHeaderSection() {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel descriptionLabel = new JLabel(DESCRIPTION_TXT);
		section.add(descriptionLabel);
		return section;
	}
}
