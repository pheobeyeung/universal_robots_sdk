package com.ur.urcap.examples.localization.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.system.localization.UnitType;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.api.domain.value.simple.SimpleValueFactory;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.annotation.Select;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.InputTextField;
import com.ur.urcap.api.ui.component.LabelComponent;
import com.ur.urcap.api.ui.component.SelectDropDownList;
import com.ur.urcap.api.ui.component.SelectEvent;
import com.ur.urcap.examples.localization.i18n.CommandNamesResource;
import com.ur.urcap.examples.localization.i18n.LanguagePack;
import com.ur.urcap.examples.localization.i18n.TextResource;
import com.ur.urcap.examples.localization.i18n.UnitsResource;

import java.util.Arrays;
import java.util.List;

public class LocalizationProgramNodeContribution implements ProgramNodeContribution {
	private static final String HEIGHT = "height";
	private static final String UNIT = "unit";

	private final UnitType systemOfMeasurement;
	private final SimpleValueFactory valueFactory;
	private final DataModel model;

	public LocalizationProgramNodeContribution(URCapAPI api, DataModel model) {
		this.model = model;
		valueFactory = api.getValueFactoryProvider().getSimpleValueFactory();
		systemOfMeasurement = LanguagePack.getInstance().getUnitType();
	}

	private TextResource getTextResource() {
		return LanguagePack.getInstance().getTextResource();
	}

	private CommandNamesResource getCommandNamesResource() {
		return LanguagePack.getInstance().getCommandNamesResource();
	}

	private UnitsResource getUnitsResource() {
		return LanguagePack.getInstance().getUnitsResource();
	}

	@Label(id = "nodeDescription")
	private LabelComponent nodeDescriptionLabel;

	@Label(id = "enterValue")
	private LabelComponent enterValueLabel;

	@Input(id = "yourHeight")
	private InputTextField numberValueTextField;

	@Label(id = "enterValueUnit")
	private LabelComponent valueUnitLabel;

	@Label(id = "selectConversion")
	private LabelComponent selectConversionLabel;

	@Select(id = "conversion")
	private SelectDropDownList conversionUnitDropDown;

	@Label(id = "preview")
	private LabelComponent previewLabel;

	@Label(id = "language")
	private LabelComponent languageLabel;

	@Label(id = "programmingLanguage")
	private LabelComponent programmingLanguageLabel;

	@Label(id = "systemOfMeasurement")
	private LabelComponent systemOfMeasurementLabel;

	@Label(id = "unitExample")
	private LabelComponent unitExampleLabel;

	@Input(id = "yourHeight")
	public void onInput(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_CHANGE) {
			final double inputValue = Double.parseDouble(numberValueTextField.getText().replace(',','.'));
			model.set(HEIGHT, inputValue);
			setUnitExampleLabel();
		}
	}

	@Select(id = "conversion")
	public void encoderTypeSelected(SelectEvent event) {
		if (event.getEvent() == SelectEvent.EventType.ON_SELECT) {
			final Length.Unit targetUnit = ((TranslatableLengthUnit) conversionUnitDropDown.getSelectedItem()).getUnit();
			model.set(UNIT, targetUnit.name());
			setUnitExampleLabel();
		}
	}

	@Override
	public void openView() {
		numberValueTextField.setText(getHeight() + "");
		nodeDescriptionLabel.setText(getTextResource().nodeDescription());
		enterValueLabel.setText(getTextResource().enterValue());
		valueUnitLabel.setText(getUnitsResource().lengthUnit(getSystemLengthUnit()));

		selectConversionLabel.setText(getTextResource().units());
		initializeDropDownList();

		previewLabel.setText(getTextResource().preview());
		languageLabel.setText(createLanguageString());
		programmingLanguageLabel.setText(createProgrammingLanguageString());
		systemOfMeasurementLabel.setText(createSystemOfMeasurementString());
		setUnitExampleLabel();
	}

	private void initializeDropDownList() {
		List<TranslatableLengthUnit> unitNames = Arrays.asList(
				new TranslatableLengthUnit(Length.Unit.MM),
				new TranslatableLengthUnit(Length.Unit.IN));
		conversionUnitDropDown.setItems(unitNames);

		final Length.Unit targetUnit = getTargetUnit();
		for (TranslatableLengthUnit unit : unitNames) {
			if (unit.getUnit() == targetUnit) {
				conversionUnitDropDown.selectItem(unit);
				return;
			}
		}
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return getCommandNamesResource().nodeName() + ": " + LanguagePack.getInstance().getLanguageResource().localeProgrammingLanguage();
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine("popup(\"" + createMessage() + "\")");
	}

	private Length.Unit getTargetUnit() {
		String unitName = model.get(UNIT, Length.Unit.MM.name());
		return Length.Unit.valueOf(unitName);
	}

	private double getHeight() {
		return model.get(HEIGHT,0d);
	}

	private void setUnitExampleLabel() {
		unitExampleLabel.setText(createConvertedValueString());
	}

	private String createConvertedValueString() {
		double inputValue = getHeight();
		Length inputLength = valueFactory.createLength(inputValue, getSystemLengthUnit());
		final double convertedValue = inputLength.getAs(getTargetUnit());
		String formattedValue = String.format("%.2f", convertedValue);
		return getTextResource().example() + ": "+ formattedValue + " " + getUnitsResource().lengthUnit(getTargetUnit());
	}

	private String createSystemOfMeasurementString() {
		return getTextResource().units() + ": " + getTextResource().systemOfMeasurement(systemOfMeasurement);
	}

	private String createLanguageString() {
		return getTextResource().language() + ": " + LanguagePack.getInstance().getLanguageResource().localeLanguage();
	}

	private String createProgrammingLanguageString() {
		return getTextResource().programmingLanguage() + ": " + LanguagePack.getInstance().getLanguageResource().localeProgrammingLanguage();
	}

	private Length.Unit getSystemLengthUnit() {
		return systemOfMeasurement == UnitType.METRIC ? Length.Unit.MM : Length.Unit.IN;
	}

	private String createMessage() {
		return createLanguageString() + "<br/>" +
				createProgrammingLanguageString() + "<br/>" +
				createSystemOfMeasurementString() + "<br/>" +
				createConvertedValueString();
	}
}
