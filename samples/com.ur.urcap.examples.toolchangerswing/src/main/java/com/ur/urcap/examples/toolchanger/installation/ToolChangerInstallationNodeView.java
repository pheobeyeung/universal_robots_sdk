package com.ur.urcap.examples.toolchanger.installation;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.examples.toolchanger.common.UIComponentFactory;
import com.ur.urcap.examples.toolchanger.style.Style;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class ToolChangerInstallationNodeView implements SwingInstallationNodeView<ToolChangerInstallationNodeContribution> {

	private static final String SET_POSITION_TXT = "Set position";
	private static final String CHANGE_POSITION_TXT = "Change position";
	private static final String MOVE_HERE_TXT = "Move here";
	private static final String TOOL_TCPS_TXT = "Tool TCPs";
	private static final String MM_TXT = "mm";
	private static final String TOOL_FLANGE_OFFSET_TXT = "Tool flange offset";
	private static final String TOOL_CHANGE_POSITION_TXT = "Tool change position";

	private static final String LENGTH_FORMAT = "%1$,.2f";

	private final UIComponentFactory uiFactory;
	private final Style style;

	private JCheckBox gripperCheckBox;
	private JCheckBox screwdriverCheckBox;
	private JCheckBox weldingToolCheckBox;

	private JButton setPositionButton;
	private JButton moveToButton;

	private JTextField toolFlangeOffsetXInputField;
	private JTextField toolFlangeOffsetYInputField;
	private JTextField toolFlangeOffsetZInputField;

	public ToolChangerInstallationNodeView(Style style) {
		this.style = style;
		this.uiFactory = new UIComponentFactory(style);
	}

	@Override
	public void buildUI(JPanel panel, final ToolChangerInstallationNodeContribution contribution) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		Box content = Box.createVerticalBox();
		content.setBorder(BorderFactory.createEmptyBorder(0, style.getContentIndent(), 0, 0));

		content.setAlignmentX(Component.LEFT_ALIGNMENT);
		content.setAlignmentY(Component.TOP_ALIGNMENT);
		content.add(uiFactory.createHeaderSection(TOOL_CHANGE_POSITION_TXT));
		content.add(createPoseSection(contribution));
		content.add(uiFactory.createVerticalSpacing(6));
		content.add(createToolsSection(contribution));
		content.add(uiFactory.createVerticalSpacing(3));
		content.add(uiFactory.createHeaderSection(TOOL_FLANGE_OFFSET_TXT));
		content.add(createInputSection(contribution));

		panel.add(content);
	}


	private Component createPoseSection(ToolChangerInstallationNodeContribution contribution) {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);
		section.setAlignmentY(Component.TOP_ALIGNMENT);

		setPositionButton = createDefinePositionButton(contribution);
		section.add(setPositionButton);
		section.add(uiFactory.createHorizontalSpacing());

		moveToButton = createMoveToButton(contribution);
		section.add(moveToButton);

		return section;
	}

	private Component createToolsSection(final ToolChangerInstallationNodeContribution installationNode) {
		Box section = Box.createVerticalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);
		section.setAlignmentY(Component.TOP_ALIGNMENT);

		section.add(new JLabel(TOOL_TCPS_TXT));

		gripperCheckBox = createTCPCheckBox(installationNode, Tool.GRIPPER);
		section.add(gripperCheckBox);

		screwdriverCheckBox = createTCPCheckBox(installationNode, Tool.SCREWDRIVER);
		section.add(screwdriverCheckBox);

		weldingToolCheckBox = createTCPCheckBox(installationNode, Tool.WELDING_TOOL);
		section.add(weldingToolCheckBox);

		return section;
	}

	private Component createInputSection(final ToolChangerInstallationNodeContribution contribution) {
		Box section = Box.createVerticalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);
		section.setAlignmentY(Component.TOP_ALIGNMENT);

		toolFlangeOffsetXInputField = uiFactory.createNumberInputField(
				contribution.getKeyboardForToolFlangeOffsetX(),
				contribution.getCallbackForToolFlangeOffsetX(),
				contribution.getValueProviderForToolFlangeOffsetX());

		toolFlangeOffsetYInputField = uiFactory.createNumberInputField(
				contribution.getKeyboardForToolFlangeOffsetY(),
				contribution.getCallbackForToolFlangeOffsetY(),
				contribution.getValueProviderForToolFlangeOffsetY());

		toolFlangeOffsetZInputField = uiFactory.createNumberInputField(
				contribution.getKeyboardForToolFlangeOffsetZ(),
				contribution.getCallbackForToolFlangeOffsetZ(),
				contribution.getValueProviderForToolFlangeOffsetZ());

		section.add(uiFactory.createInputSection("X", toolFlangeOffsetXInputField, MM_TXT));
		section.add(uiFactory.createSmallVerticalSpacing());

		section.add(uiFactory.createInputSection("Y", toolFlangeOffsetYInputField, MM_TXT));
		section.add(uiFactory.createSmallVerticalSpacing());

		section.add(uiFactory.createInputSection("Z", toolFlangeOffsetZInputField, MM_TXT));

		return section;
	}

	private JButton createDefinePositionButton(final ToolChangerInstallationNodeContribution contribution) {
		JButton btn = new JButton(SET_POSITION_TXT);
		btn.setFocusPainted(false);

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.defineToolChangerPosition();
			}
		});

		return btn;
	}

	private JButton createMoveToButton(final ToolChangerInstallationNodeContribution contribution) {
		JButton btn = new JButton(MOVE_HERE_TXT);
		btn.setFocusPainted(false);

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				contribution.moveToPosition();
			}
		});

		return btn;
	}


	private JCheckBox createTCPCheckBox(final ToolChangerInstallationNodeContribution contribution, final Tool tool) {
		JCheckBox checkBox = new JCheckBox(tool.getTitle());
		checkBox.setFocusPainted(false);

		checkBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					contribution.setToolEnabled(tool, true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					contribution.setToolEnabled(tool, false);
				}
			}
		});

		return checkBox;
	}

	public void setGripperTCPEnabled(boolean enabled) {
		gripperCheckBox.setSelected(enabled);
	}

	public void setScrewdriverTCPEnabled(boolean enabled) {
		screwdriverCheckBox.setSelected(enabled);
	}

	public void setWeldingToolTCPEnabled(boolean enabled) {
		weldingToolCheckBox.setSelected(enabled);
	}

	public void updateToolFlangeOffset(double toolFlangeOffsetX, double toolFlangeOffsetY, double toolFlangeOffsetZ) {
		toolFlangeOffsetXInputField.setText(String.format(LENGTH_FORMAT, toolFlangeOffsetX));
		toolFlangeOffsetYInputField.setText(String.format(LENGTH_FORMAT, toolFlangeOffsetY));
		toolFlangeOffsetZInputField.setText(String.format(LENGTH_FORMAT, toolFlangeOffsetZ));
	}

	public void setToolChangePositionDefined(boolean positionDefined) {
		if (positionDefined) {
			setPositionButton.setText(CHANGE_POSITION_TXT);
			moveToButton.setEnabled(true);
		} else {
			setPositionButton.setText(SET_POSITION_TXT);
			moveToButton.setEnabled(false);
		}
	}

}
