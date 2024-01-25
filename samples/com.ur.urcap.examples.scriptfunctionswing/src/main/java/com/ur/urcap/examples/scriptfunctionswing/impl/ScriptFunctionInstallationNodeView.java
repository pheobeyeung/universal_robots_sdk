package com.ur.urcap.examples.scriptfunctionswing.impl;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

public class ScriptFunctionInstallationNodeView implements SwingInstallationNodeView<ScriptFunctionInstallationNodeContribution> {

	private JCheckBox mulCheckBox;

	public ScriptFunctionInstallationNodeView() {

	}

	@Override
	public void buildUI(JPanel panel, ScriptFunctionInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createDescription());
		panel.add(Box.createRigidArea(new Dimension(0, 25)));
		panel.add(createShowMulChecked(contribution));
		panel.add(Box.createVerticalGlue());
	}

	public void setMulChecked(boolean checked) {
		mulCheckBox.setSelected(checked);
	}

	private JTextPane createDescription() {
		JTextPane descriptionPane = new JTextPane();
		descriptionPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		descriptionPane.setBorder(BorderFactory.createEmptyBorder());
		String description =
				"<html>This URCap example demonstrates how function signatures can be added/removed " +
						"to the list of functions shown in the expression editor for easy access." +
						"<p>Two script functions, add() and mul(), are added to the program preamble, " +
						"so they can be invoked during program execution.</p></br>" +
						"<p>1. <i>add():</i> permanently available in the expression editor (e.g. if commonly used) </p>" +
						"<p>2. <i>mul():</i> can be set to appear in the expression editor (e.g. if only relevant in specific cases)</p></html>";
		descriptionPane.setContentType("text/html");
		descriptionPane.setText(description);
		descriptionPane.setEditable(false);
		descriptionPane.setBackground(new Color(0, 0, 0, 0));
		descriptionPane.setMaximumSize(descriptionPane.getPreferredSize());
		return descriptionPane;
	}

	private Box createShowMulChecked(ScriptFunctionInstallationNodeContribution contribution) {
		Box showMulBox = Box.createHorizontalBox();
		mulCheckBox = new JCheckBox("Show mul() function");
		mulCheckBox.setHorizontalTextPosition(SwingConstants.RIGHT);
		mulCheckBox.addItemListener(contribution.getListenerForShowMul());
		showMulBox.add(mulCheckBox);
		showMulBox.add(Box.createHorizontalGlue());
		return showMulBox;
	}

}
