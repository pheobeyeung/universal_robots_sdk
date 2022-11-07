package com.ur.urcap.examples.pickorplaceswing.close;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.examples.pickorplaceswing.pickorplace.Style;

public class GripperCloseProgramNodeView implements SwingProgramNodeView<GripperCloseProgramNodeContribution> {

	private final Style style;

	GripperCloseProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<GripperCloseProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(style.createVerticalSpacing());
		panel.add(style.createInfo("Closing of the gripper is specified in this subtree."));
	}

}
