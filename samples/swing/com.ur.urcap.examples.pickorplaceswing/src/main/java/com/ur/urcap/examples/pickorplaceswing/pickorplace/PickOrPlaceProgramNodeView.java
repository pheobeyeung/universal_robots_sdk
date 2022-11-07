package com.ur.urcap.examples.pickorplaceswing.pickorplace;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

public class PickOrPlaceProgramNodeView implements SwingProgramNodeView<PickOrPlaceProgramNodeContribution> {

	private final Style style;

	private JButton placeButton;
	private JButton pickButton;
	private JButton resetButton;

	public PickOrPlaceProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(final JPanel panel, final ContributionProvider<PickOrPlaceProgramNodeContribution> provider) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(createMakeSection(provider));
		panel.add(createResetSection(provider));
	}

	private Component createMakeSection(final ContributionProvider<PickOrPlaceProgramNodeContribution> provider) {
		Box section = style.createSection(BoxLayout.PAGE_AXIS);

		Box infoSection = style.createSection(BoxLayout.PAGE_AXIS);
		infoSection.add(style.createInfo("Choose Pick or Place:"));
		infoSection.add(style.createVerticalSpacing());
		section.add(infoSection);

		Box buttonSection = style.createSection(BoxLayout.LINE_AXIS);
		buttonSection.add(style.createHorizontalIndent());
		this.pickButton = style.createButton("Pick");
		this.pickButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().createPick();
			}
		});
		buttonSection.add(pickButton, FlowLayout.LEFT);

		buttonSection.add(style.createHorizontalSpacing());
		this.placeButton = style.createButton("Place");
		this.placeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().createPlace();
			}
		});
		buttonSection.add(placeButton);

		section.add(buttonSection);
		return section;
	}

	private Component createResetSection(final ContributionProvider<PickOrPlaceProgramNodeContribution> provider) {
		Box section = style.createSection(BoxLayout.PAGE_AXIS);

		section.add(style.createVerticalSpacing());

		Box infoSection = style.createSection(BoxLayout.PAGE_AXIS);
		infoSection.add(style.createVerticalSpacing());
		infoSection.add(style.createInfo("Tap the button to reset your selection."));
		infoSection.add(style.createVerticalSpacing());
		infoSection.add(style.createInfo("This removes the program tree and clears all configuration data."));
		infoSection.add(style.createVerticalSpacing());
		section.add(infoSection);

		Box buttonSection = style.createSection(BoxLayout.LINE_AXIS);
		buttonSection.add(style.createHorizontalIndent());
		this.resetButton = style.createButton("Reset");
		this.resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				provider.get().reset();
			}
		});
		buttonSection.add(this.resetButton, FlowLayout.LEFT);
		section.add(buttonSection);

		return section;
	}

	void update(PickOrPlaceProgramNodeContribution contribution) {
		TemplateType templateType = contribution.getTemplateType();

		if (templateType == TemplateType.EMPTY) {
			pickButton.setEnabled(true);
			placeButton.setEnabled(true);
			resetButton.setEnabled(false);
		} else {
			pickButton.setEnabled(false);
			placeButton.setEnabled(false);
			resetButton.setEnabled(true);
		}
	}

}
