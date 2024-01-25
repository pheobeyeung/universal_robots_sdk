package com.ur.urcap.examples.createpayload.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.payload.Payload;
import com.ur.urcap.api.domain.payload.PayloadContributionModel;
import com.ur.urcap.api.domain.robot.RobotLimits;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.api.domain.value.Position;
import com.ur.urcap.api.domain.value.PositionFactory;
import com.ur.urcap.api.domain.value.Range;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.api.domain.value.simple.Mass;
import com.ur.urcap.api.domain.value.simple.SimpleValueFactory;


public class CreatePayloadInstallationNodeContribution implements InstallationNodeContribution {
	private static final String PAYLOAD_KEY = "PayloadKey";
	private static final String PAYLOAD_ID = "mypayload";
	private static final String SUGGESTED_PAYLOAD_NAME = "URCapPayload";

	private static final int DEFAULT_MASS_VALUE = 3;
	private static final int DEFAULT_COORDINATE_VALUE = 0;

	private final DataModel model;
	private final CreatePayloadInstallationNodeView view;
	private final PayloadContributionModel payloadContributionModel;
	private final PositionFactory positionFactory;
	private final SimpleValueFactory valueFactory;
	private final RobotLimits robotLimits;
	private final KeyboardInputFactory keyboardInputFactory;
	private final InputValidationFactory validationFactory;

	CreatePayloadInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, CreatePayloadInstallationNodeView view) {
		this.model = model;
		this.view = view;

		payloadContributionModel = apiProvider.getInstallationAPI().getPayloadContributionModel();
		positionFactory = apiProvider.getInstallationAPI().getValueFactoryProvider().getPositionFactory();
		valueFactory = apiProvider.getInstallationAPI().getValueFactoryProvider().getSimpleValueFactory();
		keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		robotLimits = apiProvider.getSystemAPI().getRobotModel().getRobotLimits();
		validationFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();
	}

	@Override
	public void openView() {
		view.update();
	}

	@Override
	public void closeView() {
		// Intentionally left empty
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Intentionally left empty
	}

	public boolean isPayloadCreated() {
		return model.isSet(PAYLOAD_KEY);
	}

	public Payload getPayload() {
		return model.get(PAYLOAD_KEY, (Payload) null);
	}

	public void createPayload() {
		Mass mass = valueFactory.createMass(DEFAULT_MASS_VALUE, Mass.Unit.KG);
		Position position = positionFactory.createPosition(DEFAULT_COORDINATE_VALUE,
														   DEFAULT_COORDINATE_VALUE,
														   DEFAULT_COORDINATE_VALUE,
														   Length.Unit.MM);

		Payload payload = payloadContributionModel.addPayload(PAYLOAD_ID, SUGGESTED_PAYLOAD_NAME, mass, position);
		model.set(PAYLOAD_KEY, payload);
		view.update();
	}

	public void updatePayloadMass(double mass) {
		Position centerOfGravity = payloadContributionModel.getPayload(PAYLOAD_ID).getCenterOfGravity();
		Mass payloadMass = valueFactory.createMass(mass, Mass.Unit.KG);
		payloadContributionModel.updatePayload(PAYLOAD_ID, payloadMass, centerOfGravity);
	}

	public void updateCenterOfGravity(CenterOfGravityCoordinate coordinateToUpdate, double newCenterOfGravityCoordinate) {
		Payload payload = payloadContributionModel.getPayload(PAYLOAD_ID);
		Position currentCenterOfGravity = payload.getCenterOfGravity();

		Position centerOfGravity = null;
		switch (coordinateToUpdate) {
			case CX:
				centerOfGravity = positionFactory.createPosition(newCenterOfGravityCoordinate,
																currentCenterOfGravity.getY(Length.Unit.MM),
																currentCenterOfGravity.getZ(Length.Unit.MM),
																Length.Unit.MM);
				break;
			case CY:
				centerOfGravity = positionFactory.createPosition(currentCenterOfGravity.getX(Length.Unit.MM),
																newCenterOfGravityCoordinate,
																currentCenterOfGravity.getZ(Length.Unit.MM),
																Length.Unit.MM);
				break;
			case CZ:
				centerOfGravity = positionFactory.createPosition(currentCenterOfGravity.getX(Length.Unit.MM),
																currentCenterOfGravity.getY(Length.Unit.MM),
																newCenterOfGravityCoordinate,
																Length.Unit.MM);
				break;
		}

		payloadContributionModel.updatePayload(PAYLOAD_ID, payload.getMass(), centerOfGravity);
	}

	public void deletePayload() {
		model.remove(PAYLOAD_KEY);
		payloadContributionModel.removePayload(PAYLOAD_ID);
		view.update();
	}

	public KeyboardNumberInput<Double> getKeyboardInputForMass() {
		KeyboardNumberInput<Double> keyboardInput = keyboardInputFactory.createPositiveDoubleKeypadInput();
		keyboardInput.setErrorValidator(getMassValidator());
		keyboardInput.setInitialValue(getPayload().getMass().getAs(Mass.Unit.KG));

		return keyboardInput;
	}

	private InputValidator<Double> getMassValidator() {
		Range<Mass> massRange = robotLimits.getPayloadMassRange();
		return validationFactory.createDoubleRangeValidator(
						massRange.getLowerBound().getAs(Mass.Unit.KG),
						massRange.getUpperBound().getAs(Mass.Unit.KG)
				);
	}

	public KeyboardInputCallback<Double> getKeyBoardCallbackForMass() {
		return new KeyboardInputCallback<Double>() {
			@Override
			public void onOk(Double value) {
				updatePayloadMass(value);
				view.updateInputFields(getPayload());
			}
		};
	}

	public KeyboardNumberInput<Double> getKeyboardInputForCenterOfGravity(CenterOfGravityCoordinate centerOfGravityCoordinate) {
		KeyboardNumberInput<Double> keyboardInput = keyboardInputFactory.createDoubleKeypadInput();
		keyboardInput.setErrorValidator(getCenterOfGravityInputValidator(centerOfGravityCoordinate));

		double value = 0;
		switch (centerOfGravityCoordinate) {
			case CX:
				value = getPayload().getCenterOfGravity().getX(Length.Unit.MM);
				break;
			case CY:
				value = getPayload().getCenterOfGravity().getY(Length.Unit.MM);
				break;
			case CZ:
				value = getPayload().getCenterOfGravity().getZ(Length.Unit.MM);
				break;
		}
		keyboardInput.setInitialValue(value);

		return keyboardInput;
	}

	public KeyboardInputCallback<Double> getKeyBoardCallbackForCenterOfGravity(final CenterOfGravityCoordinate centerOfGravityCoordinate) {
		return new KeyboardInputCallback<Double>() {
			@Override
			public void onOk(Double value) {
				updateCenterOfGravity(centerOfGravityCoordinate, value);
				view.updateInputFields(getPayload());
			}
		};
	}

	private InputValidator<Double> getCenterOfGravityInputValidator(CenterOfGravityCoordinate centerOfGravityCoordinate) {
		Range<Length> centerOfGravityCoordinateRange = null;
		switch (centerOfGravityCoordinate) {
			case CX:
				centerOfGravityCoordinateRange = robotLimits.getCenterOfGravityXRange();
				break;
			case CY:
				centerOfGravityCoordinateRange = robotLimits.getCenterOfGravityYRange();
				break;
			case CZ:
				centerOfGravityCoordinateRange = robotLimits.getCenterOfGravityZRange();
				break;
		}

		return validationFactory.createDoubleRangeValidator(
						centerOfGravityCoordinateRange.getLowerBound().getAs(Length.Unit.MM),
						centerOfGravityCoordinateRange.getUpperBound().getAs(Length.Unit.MM)
				);
	}
}
