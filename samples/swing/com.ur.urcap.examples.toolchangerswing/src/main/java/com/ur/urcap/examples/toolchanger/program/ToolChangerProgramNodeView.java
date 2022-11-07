package com.ur.urcap.examples.toolchanger.program;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.tcp.TCP;
import com.ur.urcap.examples.toolchanger.common.UIComponentFactory;
import com.ur.urcap.examples.toolchanger.style.Style;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;


public class ToolChangerProgramNodeView implements SwingProgramNodeView<ToolChangerProgramNodeContribution> {

	private static final String INFO_TEXT = "<html>Change the tool of the robot.<br/><br/>" +
			"  The robot will move to the tool change position defined in the installation. " +
			"When the tool change operation has finished, the tool TCP will be applied.<html>";
	private static final String SET_TOOL_TCP_TEXT = "Set Tool TCP";
	private static final String TCP_PLACEHOLDER = "<TCP>";

	private final UIComponentFactory uiFactory;
	private JComboBox tcpsComboBox;

	private ContributionProvider<ToolChangerProgramNodeContribution> contributionProvider;

	public ToolChangerProgramNodeView(Style style) {
		this.uiFactory = new UIComponentFactory(style);
	}

	@Override
	public void buildUI(JPanel jPanel, final ContributionProvider<ToolChangerProgramNodeContribution> provider) {
		this.contributionProvider = provider;

		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		jPanel.add(uiFactory.createInfoSection(INFO_TEXT));
		jPanel.add(uiFactory.createVerticalSpacing(3));

		jPanel.add(uiFactory.createHeaderSection(SET_TOOL_TCP_TEXT));
		jPanel.add(createTCPSection());
	}

	public void updateView() {
		updateTCPCombobox();
	}

	private Box createTCPSection() {
		Box section = Box.createHorizontalBox();
		section.setAlignmentX(Component.LEFT_ALIGNMENT);

		tcpsComboBox = uiFactory.createComboBox(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox comboBox = (JComboBox) e.getSource();

				Object selected = comboBox.getSelectedItem();
				if (selected instanceof TCP) {
					TCP tcp = (TCP) comboBox.getSelectedItem();
					contributionProvider.get().setSelectedTCP(tcp);
				} else {
					contributionProvider.get().setSelectedTCP(null);
				}

				updateTCPCombobox();
			}
		});

		section.add(tcpsComboBox);

		return section;
	}

	private void updateTCPCombobox() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		ToolChangerProgramNodeContribution contribution = contributionProvider.get();

		TCP selected = contribution.getSelectedTCP();
		if (selected != null) {
			model.setSelectedItem(selected);
			model.addElement(TCP_PLACEHOLDER);
		} else {
			model.setSelectedItem(TCP_PLACEHOLDER);
		}

		Collection<TCP> tcps = contribution.getAllTCP();
		for (TCP tcp : tcps) {
			model.addElement(tcp);
		}

		tcpsComboBox.setModel(model);
	}
}
