package com.ur.urcap.examples.pickorplaceswing.open;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.examples.pickorplaceswing.pickorplace.Style;

public class GripperOpenProgramNodeView implements SwingProgramNodeView<GripperOpenProgramNodeContribution> {

	private final Style style;

	GripperOpenProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<GripperOpenProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(style.createVerticalSpacing());
		panel.add(style.createInfo("Opening of the gripper is specified in this subtree."));
	}
}
