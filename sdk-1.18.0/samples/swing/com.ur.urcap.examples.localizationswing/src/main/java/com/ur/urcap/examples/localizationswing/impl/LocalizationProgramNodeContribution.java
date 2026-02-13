package com.ur.urcap.examples.localizationswing.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.system.localization.UnitType;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.api.domain.value.simple.SimpleValueFactory;
import com.ur.urcap.examples.localizationswing.i18n.CommandNamesResource;
import com.ur.urcap.examples.localizationswing.i18n.LanguagePack;
import com.ur.urcap.examples.localizationswing.i18n.TextResource;
import com.ur.urcap.examples.localizationswing.i18n.UnitsResource;

import java.util.Arrays;
import java.util.List;

public class LocalizationProgramNodeContribution implements ProgramNodeContribution {
	private static final String HEIGHT = "height";
	private static final String UNIT = "unit";

	private final UnitType systemOfMeasurement;
	private final SimpleValueFactory valueFactory;
	private final ProgramAPIProvider apiProvider;
	private final DataModel model;
	private LocalizationProgramNodeView view;
	private final LanguagePack languagePack;

	public LocalizationProgramNodeContribution(ProgramAPIProvider api, LocalizationProgramNodeView view, DataModel model) {
		this.apiProvider = api;
		this.view = view;
		this.model = model;
		languagePack = new LanguagePack(api.getSystemAPI().getSystemSettings().getLocalization());
		valueFactory = api.getProgramAPI().getValueFactoryProvider().getSimpleValueFactory();
		systemOfMeasurement = languagePack.getUnitType();
	}

	private TextResource getTextResource() {
		return languagePack.getTextResource();
	}

	private CommandNamesResource getCommandNamesResource() {
		return languagePack.getCommandNamesResource();
	}

	private UnitsResource getUnitsResource() {
		return languagePack.getUnitsResource();
	}


	@Override
	public void openView() {
		view.setNodeDescriptionText(getTextResource().nodeDescription());
		view.setEnterValueText(getUnitsResource().lengthUnit(getSystemLengthUnit()));
		setHeightTextField(Double.toString(getHeight()));
		view.setHeightLabel(getTextResource().enterValue());
		view.setUnitsText(getTextResource().units());
		initializeDropDownList();
		view.setPreviewText(getTextResource().preview());
		view.setLanguageLabel(createLanguageString());
		view.setProgrammingLanguageLabel(createProgrammingLanguageString());
		view.setSystemOfMeasurementLabel(createSystemOfMeasurementString());
		setUnitExampleLabel();
	}

	private void initializeDropDownList() {
		List<TranslatableLengthUnit> unitNames = Arrays.asList(
			new TranslatableLengthUnit(getTextResource(), Length.Unit.MM),
			new TranslatableLengthUnit(getTextResource(), Length.Unit.IN));
		view.setUnits(unitNames);

		Length.Unit targetUnit = getTargetUnit();
		for (TranslatableLengthUnit unit : unitNames) {
			if (unit.getUnit() == targetUnit) {
				view.setSelectedUnit(unit);
				return;
			}
		}
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return getCommandNamesResource().nodeName() + ": " + languagePack.getLanguageResource().localeProgrammingLanguage();
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine("popup(\"" + createMessage() + "\")");
	}

	public KeyboardInputCallback<Double> getCallbackForSetHeight() {
		return new KeyboardInputCallback<Double>() {
			@Override
			public void onOk(Double value) {
				storeHeight(value);
			}
		};
	}

	private void storeHeight(final Double value) {
		apiProvider.getProgramAPI().getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(HEIGHT, value);
				setUnitExampleLabel();
				setHeightTextField(value.toString());
			}
		});
	}

	private void setHeightTextField(String height) {
		view.setHeightTextField(height);
	}

	public KeyboardNumberInput<Double> getKeyboardForHeight() {
		KeyboardNumberInput<Double> keyboard = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory().createDoubleKeypadInput();
		keyboard.setInitialValue(getHeight());
		return keyboard;
	}

	public void onUnitSelection() {
		apiProvider.getProgramAPI().getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				final Length.Unit targetUnit = view.getSelectedUnit().getUnit();
				model.set(UNIT, targetUnit.name());
				setUnitExampleLabel();
			}
		});
	}

	private Length.Unit getTargetUnit() {
		String unitName = model.get(UNIT, Length.Unit.MM.name());
		return Length.Unit.valueOf(unitName);
	}

	private double getHeight() {
		return model.get(HEIGHT,0d);
	}

	private void setUnitExampleLabel() {
		view.setUnitExampleLabel(createConvertedValueString());
	}

	private String createConvertedValueString() {
		double inputValue = getHeight();
		Length inputLength = valueFactory.createLength(inputValue, getSystemLengthUnit());
		final double convertedValue = inputLength.getAs(getTargetUnit());
		String formattedValue = String.format("%.2f", convertedValue);
		return getTextResource().example() + ": "+ formattedValue + " " + getUnitsResource().lengthUnit(getTargetUnit());
	}

	private String createSystemOfMeasurementString() {
		return createSystemOfMeasurementString(languagePack);
	}

	private String createSystemOfMeasurementString(LanguagePack outputLanguage) {
		return outputLanguage.getTextResource().units() + ": " + outputLanguage.getTextResource().systemOfMeasurement(systemOfMeasurement);
	}

	private String createLanguageString() {
		return getTextResource().language() + ": " + languagePack.getLanguageResource().localeLanguage();
	}

	private String createProgrammingLanguageString() {
		return getTextResource().programmingLanguage() + ": " + languagePack.getLanguageResource().localeProgrammingLanguage();
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
