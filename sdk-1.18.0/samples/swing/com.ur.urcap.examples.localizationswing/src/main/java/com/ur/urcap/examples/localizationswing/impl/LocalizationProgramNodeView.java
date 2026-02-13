package com.ur.urcap.examples.localizationswing.impl;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LocalizationProgramNodeView implements SwingProgramNodeView<LocalizationProgramNodeContribution> {
	private JLabel nodeDescriptionLabel;
	private JLabel enterValueLabel;
	private JLabel setHeightLabel;
	private JLabel setUnitsLabel;
	private JLabel previewLabel;
	private JComboBox unitsCombo;
	private JTextField heightTextField;
	private JLabel languageLabel;
	private JLabel programmingLanguageLabel;
	private JLabel systemOfMeasurementLabel;
	private JLabel unitExampleLabel;
	private final Style style;

	public LocalizationProgramNodeView(ViewAPIProvider apiProvider, Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel panel, final ContributionProvider<LocalizationProgramNodeContribution> provider) {

		panel.setLayout(new GridBagLayout());
		GridBagConstraints baseConstraints = new GridBagConstraints();
		baseConstraints.fill = GridBagConstraints.HORIZONTAL;
		baseConstraints.anchor = GridBagConstraints.NORTH;
		baseConstraints.insets = new Insets(3,1,3,1);
		baseConstraints.weightx = 0.5;

		// Inputs
		GridBagConstraints nodeDescriptionConstraints = (GridBagConstraints)baseConstraints.clone();
		nodeDescriptionConstraints.gridx = 0;
		nodeDescriptionConstraints.gridy = 0;
		nodeDescriptionConstraints.gridwidth = 2;
		nodeDescriptionConstraints.insets = new Insets(10,0,10,0);
		panel.add(createNodeDescriptionLabel(), nodeDescriptionConstraints);

		GridBagConstraints heightLabelConstraints = (GridBagConstraints)baseConstraints.clone();
		heightLabelConstraints.gridx = 0;
		heightLabelConstraints.gridy = 1;
		panel.add(createHeightLabel(), heightLabelConstraints);

		JPanel inputPanel = new JPanel(new BorderLayout());
		inputPanel.add(createHeightTextField(provider), BorderLayout.WEST);
		inputPanel.add(new JLabel(" "), BorderLayout.CENTER);
		inputPanel.add(createEnterValueLabel(), BorderLayout.EAST);

		GridBagConstraints heightTextFieldConstraints = (GridBagConstraints)baseConstraints.clone();
		heightTextFieldConstraints.gridx = 0;
		heightTextFieldConstraints.gridy = 2;
		heightTextFieldConstraints.anchor = GridBagConstraints.WEST;
		heightTextFieldConstraints.fill = GridBagConstraints.NONE;
		panel.add(inputPanel, heightTextFieldConstraints);

		GridBagConstraints setInputConstraints = (GridBagConstraints)baseConstraints.clone();
		setInputConstraints.gridx = 0;
		setInputConstraints.gridy = 3;
		setInputConstraints.insets = new Insets(20, 0, 5, 0);
		panel.add(createSetInputsLabel(), setInputConstraints);

		GridBagConstraints unitsComboConstraints = (GridBagConstraints)baseConstraints.clone();
		unitsComboConstraints.gridx = 0;
		unitsComboConstraints.gridy = 4;
		unitsComboConstraints.anchor = GridBagConstraints.WEST;
		unitsComboConstraints.fill = GridBagConstraints.NONE;
		panel.add(createUnitsCombo(provider), unitsComboConstraints);

		// Outputs
		GridBagConstraints previewLabelConstraints = (GridBagConstraints)baseConstraints.clone();
		previewLabelConstraints.gridx = 0;
		previewLabelConstraints.gridy = 5;
		previewLabelConstraints.insets = new Insets(50, 0, 25, 0);
		panel.add(createPreviewLabel(), previewLabelConstraints);

		GridBagConstraints languageLabelConstraints = (GridBagConstraints)baseConstraints.clone();
		languageLabelConstraints.gridx = 0;
		languageLabelConstraints.gridy = 6;
		panel.add(createLanguageLabel(), languageLabelConstraints);

		GridBagConstraints programmingLanguageLabelConstraints = (GridBagConstraints)baseConstraints.clone();
		programmingLanguageLabelConstraints.gridx = 0;
		programmingLanguageLabelConstraints.gridy = 7;
		panel.add(createProgrammingLanguageLabel(), programmingLanguageLabelConstraints);

		GridBagConstraints systemOfMeasurementConstraints = (GridBagConstraints)baseConstraints.clone();
		systemOfMeasurementConstraints.gridx = 0;
		systemOfMeasurementConstraints.gridy = 8;
		panel.add(createSystemOfMeasurementLabel(), systemOfMeasurementConstraints);

		GridBagConstraints unitExampleConstraints = (GridBagConstraints)baseConstraints.clone();
		unitExampleConstraints.gridx = 0;
		unitExampleConstraints.gridy = 9;
		unitExampleConstraints.gridwidth = 5;
		panel.add(createUnitExampleLabel(), unitExampleConstraints);

		GridBagConstraints bufferConstraints = (GridBagConstraints)baseConstraints.clone();
		bufferConstraints.weighty = 1.0;
		bufferConstraints.gridx = 0;
		bufferConstraints.gridy = 10;
		panel.add(new JPanel(), bufferConstraints);
	}

	public void setNodeDescriptionText (String description) {
		nodeDescriptionLabel.setText(description);
	}

	public void setEnterValueText (String valueText) {
		enterValueLabel.setText(valueText);
	}

	public void setHeightLabel(String height) {
		setHeightLabel.setText(height);
	}

	public void setUnitsText(String units) {
		setUnitsLabel.setText(units);
	}

	public void setUnits(List<TranslatableLengthUnit> units) {
		unitsCombo.removeAll();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(TranslatableLengthUnit unit : units){
			model.addElement(unit);
		}
		unitsCombo.setModel(model);
		unitsCombo.updateUI();
	}

	public void setSelectedUnit(TranslatableLengthUnit unit) {
		unitsCombo.setSelectedItem(unit);
	}

	public void setLanguageLabel(String lang) {
		languageLabel.setText(lang);
	}

	public void setProgrammingLanguageLabel(String programmingLang) {
		programmingLanguageLabel.setText(programmingLang);
	}

	public void setPreviewText(String preview) {
		previewLabel.setText(preview);
	}

	public void setSystemOfMeasurementLabel(String system) {
		systemOfMeasurementLabel.setText(system);
	}

	public void setUnitExampleLabel(String result) {
		unitExampleLabel.setText(result);
	}

	public void setHeightTextField(String height) {
		heightTextField.setText(height);
	}

	public TranslatableLengthUnit getSelectedUnit() {
		return (TranslatableLengthUnit)unitsCombo.getSelectedItem();
	}

	private JLabel createNodeDescriptionLabel() {
		nodeDescriptionLabel = new JLabel();
		return nodeDescriptionLabel;
	}

	private JLabel createSetInputsLabel() {
		setUnitsLabel = new JLabel();
		setUnitsLabel.setMaximumSize(setUnitsLabel.getPreferredSize());
		return setUnitsLabel;
	}

	private JLabel createEnterValueLabel() {
		enterValueLabel = new JLabel();
		return enterValueLabel;
	}

	private JComboBox createUnitsCombo(final ContributionProvider<LocalizationProgramNodeContribution> provider) {
		unitsCombo = new JComboBox();
		unitsCombo.setPreferredSize(style.getComboBoxSize());
		unitsCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				provider.get().onUnitSelection();
			}
		});
		return unitsCombo;
	}

	private JLabel createHeightLabel() {
		setHeightLabel = new JLabel();
		setHeightLabel.setMaximumSize(setHeightLabel.getPreferredSize());
		return setHeightLabel;
	}

	private JTextField createHeightTextField(final ContributionProvider<LocalizationProgramNodeContribution> provider) {
		heightTextField = new JTextField();
		heightTextField.setFocusable(false);
		heightTextField.setHorizontalAlignment(JTextField.RIGHT);
		heightTextField.setPreferredSize(style.getInputfieldSize());
		heightTextField.setMaximumSize(heightTextField.getPreferredSize());
		heightTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				KeyboardNumberInput<Double> keyboard = provider.get().getKeyboardForHeight();
				keyboard.show(heightTextField, provider.get().getCallbackForSetHeight());
			}
		});
		return heightTextField;
	}

	private JLabel createPreviewLabel() {
		previewLabel = new JLabel("Preview");
		previewLabel.setFont(previewLabel.getFont().deriveFont(Font.BOLD, style.getSmallHeaderFontSize()));
		return previewLabel;
	}

	private JLabel createLanguageLabel() {
		languageLabel = new JLabel("Language");
		return languageLabel;
	}

	private JLabel createProgrammingLanguageLabel() {
		programmingLanguageLabel = new JLabel("Programming Language");
		return programmingLanguageLabel;
	}

	private JLabel createSystemOfMeasurementLabel() {
		systemOfMeasurementLabel = new JLabel("System of Measurement");
		return systemOfMeasurementLabel;
	}

	private JLabel createUnitExampleLabel() {
		unitExampleLabel = new JLabel("Unit Example");
		return unitExampleLabel;
	}
}
