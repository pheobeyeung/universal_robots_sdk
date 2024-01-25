package com.ur.urcap.examples.gripandrelease.program;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.device.gripper.GripperDevice;
import com.ur.urcap.examples.gripandrelease.common.UIComponentFactory;
import com.ur.urcap.examples.gripandrelease.style.Style;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class GripAndReleaseProgramNodeView implements SwingProgramNodeView<GripAndReleaseProgramNodeContribution> {

	private static final String INFO_TEXT = "<html>Select a gripper and click the Apply button.<html>";
	private static final String SELECT_GRIPPER_TEXT = "Gripper";
	private static final String GRIPPER_UNDEFINED_SELECTION = "<Select gripper>";

	private final UIComponentFactory uiFactory;
	private final Style style;
	private JComboBox gripperComboBox;
	private JButton applyButton;

	private ContributionProvider<GripAndReleaseProgramNodeContribution> contributionProvider;


	public GripAndReleaseProgramNodeView(Style style) {
		this.style = style;
		this.uiFactory = new UIComponentFactory(style);
	}

	@Override
	public void buildUI(JPanel jPanel, final ContributionProvider<GripAndReleaseProgramNodeContribution> provider) {
		this.contributionProvider = provider;

		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.add(uiFactory.createInfoSection(INFO_TEXT));
		jPanel.add(uiFactory.createVerticalSpacing());
		jPanel.add(uiFactory.createVerticalSpacing());
		jPanel.add(uiFactory.createVerticalSpacing());

		jPanel.add(uiFactory.createHeaderSection(SELECT_GRIPPER_TEXT));
		jPanel.add(createInput());
	}

	private Box createInput() {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);

		gripperComboBox = new JComboBox();
		gripperComboBox.setPreferredSize(style.getInputFieldSize());
		gripperComboBox.setMaximumSize(style.getInputFieldSize());
		gripperComboBox.setMinimumSize(style.getInputFieldSize());
		gripperComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				setApplyButtonEnablement(contributionProvider.get().getSelectedGripperDevice());
			}
		});
		section.add(gripperComboBox);

		section.add(uiFactory.createHorizontalSpacing());
		section.add(createButton());

		return section;
	}

	private void setApplyButtonEnablement(GripperDevice gripperDevice) {
		Object selected = gripperComboBox.getSelectedItem();

		if (selected instanceof GripperDevice) {
			// Enable button if the selected gripper has not already been applied
			applyButton.setEnabled(!selected.equals(gripperDevice));
		} else{
			applyButton.setEnabled(false);
		}
	}

	private Box createButton() {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);

		applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				Object selected = gripperComboBox.getSelectedItem();
				GripperDevice gripperDevice = (GripperDevice) selected;
				GripAndReleaseProgramNodeContribution gripAndReleaseProgramNodeContribution = contributionProvider.get();
				gripAndReleaseProgramNodeContribution.createGripperNodeWithGripAndReleaseConfig(gripperDevice);

				// Enable button if the selected gripper has not already been applied
				setApplyButtonEnablement(gripperDevice);
			}
		});
		section.add(applyButton);

		return section;
	}

	public void updateView() {
		updateComboBox();
		setApplyButtonEnablement(contributionProvider.get().getSelectedGripperDevice());
	}

	private void updateComboBox() {
		GripAndReleaseProgramNodeContribution contribution = contributionProvider.get();

		List<GripperDevice> grippers = contribution.getGrippers();
		DefaultComboBoxModel model = new DefaultComboBoxModel(grippers.toArray());
		model.insertElementAt(GRIPPER_UNDEFINED_SELECTION, 0);

		GripperDevice selectedGripper = contribution.getSelectedGripperDevice();

		if (selectedGripper != null) {
			model.setSelectedItem(selectedGripper);
		} else if (hasOnlyOneGripper())
			// If there is only one gripper in PolyScope, automatically preselect it
			model.setSelectedItem(grippers.get(0));
		else {
			model.setSelectedItem(GRIPPER_UNDEFINED_SELECTION);
		}

		gripperComboBox.setModel(model);

		// If there is only one gripper available in PolyScope, disable the combo box
		gripperComboBox.setEnabled(!hasOnlyOneGripper() || hasUnresolvedGripperSelection(selectedGripper));
	}

	private boolean hasUnresolvedGripperSelection(GripperDevice selectedGripper) {
		return selectedGripper != null && !selectedGripper.isResolvable();
	}

	private boolean hasOnlyOneGripper() {
		return contributionProvider.get().getGrippers().size() == 1;
	}
}
