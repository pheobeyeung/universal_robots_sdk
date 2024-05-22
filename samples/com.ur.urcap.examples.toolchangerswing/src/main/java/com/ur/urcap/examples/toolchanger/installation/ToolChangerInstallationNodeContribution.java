package com.ur.urcap.examples.toolchanger.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.tcp.TCP;
import com.ur.urcap.api.domain.tcp.TCPContributionModel;
import com.ur.urcap.api.domain.userinteraction.RobotPositionCallback2;
import com.ur.urcap.api.domain.userinteraction.UserInteraction;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.api.domain.userinteraction.robot.movement.MovementCompleteEvent;
import com.ur.urcap.api.domain.userinteraction.robot.movement.RobotMovement;
import com.ur.urcap.api.domain.userinteraction.robot.movement.RobotMovementCallback;
import com.ur.urcap.api.domain.value.Pose;
import com.ur.urcap.api.domain.value.PoseFactory;
import com.ur.urcap.api.domain.value.jointposition.JointPositions;
import com.ur.urcap.api.domain.value.robotposition.PositionParameters;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.examples.toolchanger.common.ValueProvider;


public class ToolChangerInstallationNodeContribution implements InstallationNodeContribution {

	private static final String TOOL_CHANGE_JOINT_POSITIONS = "joint-position";
	private static final String TOOL_FLANGE_OFFSET_X = "tool-flange-offset-x";
	private static final String TOOL_FLANGE_OFFSET_Y = "tool-flange-offset-y";
	private static final String TOOL_FLANGE_OFFSET_Z = "tool-flange-offset-z";
	private static final String INITIALIZED = "initialized";

	private static final double MIN_TCP_OFFSET = 0.0;
	private static final double MAX_TCP_OFFSET = 100.0;
	private static final double DEFAULT_VALUE_OFFSET = 0.0;
	private static final double COORDINATE_VALUE_EPSILON = 0.00001;

	private final InputValidator<Double> toolFlangeOffsetXValidator;
	private final InputValidator<Double> toolFlangeOffsetYValidator;
	private final InputValidator<Double> toolFlangeOffsetZValidator;
	private final KeyboardInputFactory keyboardFactory;

	private final ToolChangerInstallationNodeView view;
	private final DataModel model;

	private final TCPContributionModel tcpContributionModel;
	private final PoseFactory poseFactory;
	private final UserInteraction userInteraction;
	private final RobotMovement robotMovement;

	public ToolChangerInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, ToolChangerInstallationNodeView view) {
		this.model = model;
		this.view = view;

		tcpContributionModel = apiProvider.getInstallationAPI().getTCPContributionModel();
		robotMovement = apiProvider.getUserInterfaceAPI().getUserInteraction().getRobotMovement();
		poseFactory = apiProvider.getInstallationAPI().getValueFactoryProvider().getPoseFactory();

		keyboardFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		InputValidationFactory inputValidationFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();
		toolFlangeOffsetXValidator = inputValidationFactory.createDoubleRangeValidator(-MAX_TCP_OFFSET, MAX_TCP_OFFSET);
		toolFlangeOffsetYValidator = inputValidationFactory.createDoubleRangeValidator(-MAX_TCP_OFFSET, MAX_TCP_OFFSET);
		toolFlangeOffsetZValidator = inputValidationFactory.createDoubleRangeValidator(MIN_TCP_OFFSET, MAX_TCP_OFFSET);
		userInteraction = apiProvider.getUserInterfaceAPI().getUserInteraction();

		initializeDefaults();
	}

	private void initializeDefaults() {
		if (model.get(INITIALIZED, false)) {
			return;
		}
		model.set(INITIALIZED, true);

		setToolEnabled(Tool.GRIPPER, true);
		setToolEnabled(Tool.SCREWDRIVER, true);
		setToolEnabled(Tool.WELDING_TOOL, true);
	}

	@Override
	public void openView() {
		view.setGripperTCPEnabled(isToolEnabled(Tool.GRIPPER));
		view.setScrewdriverTCPEnabled(isToolEnabled(Tool.SCREWDRIVER));
		view.setWeldingToolTCPEnabled(isToolEnabled(Tool.WELDING_TOOL));

		view.setToolChangePositionDefined(getToolChangeJointPositions() != null);
		view.updateToolFlangeOffset(getToolFlangeOffsetX(), getToolFlangeOffsetY(), getToolFlangeOffsetZ());
	}

	@Override
	public void closeView() {
	}

	@Override
	public void generateScript(ScriptWriter writer) {
	}

	public void setToolEnabled(Tool tool, boolean enable) {
		if (isToolEnabled(tool) == enable) {
			return;
		}
		model.set(tool.getModelKey(), enable);

		if (enable) {
			updateTCPOffset(tool);
		} else {
			tcpContributionModel.removeTCP(tool.getId());
		}
	}

	public boolean isToolEnabled(Tool tool) {
		boolean toolEnabled = model.get(tool.getModelKey(), false);
		TCP toolTCP = tcpContributionModel.getTCP(tool.getId());

		return toolEnabled && toolTCP != null;
	}

	private void updateTCPOffset(Tool tool) {
		double toolFlangeOffsetX = getToolFlangeOffsetX();
		double toolFlangeOffsetY = getToolFlangeOffsetY();
		double toolFlangeOffsetZ = getToolFlangeOffsetZ();

		Pose pose = poseFactory.createPose(
				tool.getX() + toolFlangeOffsetX,
				tool.getY() + toolFlangeOffsetY,
				tool.getZ() + toolFlangeOffsetZ,
				tool.getRx(), tool.getRy(), tool.getRz(),
				Length.Unit.MM, Angle.Unit.RAD);

		TCP tcp = tcpContributionModel.getTCP(tool.getId());
		if (tcp == null) {
			tcpContributionModel.addTCP(tool.getId(), tool.getName(), pose);
		} else {
			tcpContributionModel.updateTCP(tool.getId(), pose);
		}
	}

	public double getToolFlangeOffsetX() {
		return model.get(TOOL_FLANGE_OFFSET_X, DEFAULT_VALUE_OFFSET);
	}

	public double getToolFlangeOffsetY() {
		return model.get(TOOL_FLANGE_OFFSET_Y, DEFAULT_VALUE_OFFSET);
	}

	public double getToolFlangeOffsetZ() {
		return model.get(TOOL_FLANGE_OFFSET_Z, DEFAULT_VALUE_OFFSET);
	}

	public void setToolFlangeOffsetX(double offset) {
		if (epsilonEquals(getToolFlangeOffsetX(), offset, COORDINATE_VALUE_EPSILON)) {
			return;
		}

		model.set(TOOL_FLANGE_OFFSET_X, offset);
		updateToolTCPOffsets();
	}


	public void setToolFlangeOffsetY(double offset) {
		if (epsilonEquals(getToolFlangeOffsetY(), offset, COORDINATE_VALUE_EPSILON)) {
			return;
		}

		model.set(TOOL_FLANGE_OFFSET_Y, offset);
		updateToolTCPOffsets();
	}

	public void setToolflangeOffsetZ(double offset) {
		if (epsilonEquals(getToolFlangeOffsetZ(), offset, COORDINATE_VALUE_EPSILON)) {
			return;
		}

		model.set(TOOL_FLANGE_OFFSET_Z, offset);
		updateToolTCPOffsets();
	}

	private void updateToolTCPOffsets() {
		//update tools offset
		if (isToolEnabled(Tool.GRIPPER)) {
			updateTCPOffset(Tool.GRIPPER);
		}

		if (isToolEnabled(Tool.SCREWDRIVER)) {
			updateTCPOffset(Tool.SCREWDRIVER);
		}

		if (isToolEnabled(Tool.WELDING_TOOL)) {
			updateTCPOffset(Tool.WELDING_TOOL);
		}
	}

	public void defineToolChangerPosition() {
		userInteraction.getUserDefinedRobotPosition(new RobotPositionCallback2() {
			@Override
			public void onOk(PositionParameters positionParameters) {
				model.set(TOOL_CHANGE_JOINT_POSITIONS, positionParameters.getJointPositions());
				view.setToolChangePositionDefined(true);
			}
		});
	}

	public JointPositions getToolChangeJointPositions() {
		return model.get(TOOL_CHANGE_JOINT_POSITIONS, (JointPositions) null);
	}

	public KeyboardNumberInput<Double> getKeyboardForToolFlangeOffsetX() {
		KeyboardNumberInput<Double> keyboard = keyboardFactory.createDoubleKeypadInput();
		keyboard.setErrorValidator(toolFlangeOffsetXValidator);

		return keyboard;
	}

	public KeyboardNumberInput<Double> getKeyboardForToolFlangeOffsetY() {
		KeyboardNumberInput<Double> keyboard = keyboardFactory.createDoubleKeypadInput();
		keyboard.setErrorValidator(toolFlangeOffsetYValidator);

		return keyboard;
	}

	public KeyboardNumberInput<Double> getKeyboardForToolFlangeOffsetZ() {
		KeyboardNumberInput<Double> keyboard = keyboardFactory.createDoubleKeypadInput();
		keyboard.setErrorValidator(toolFlangeOffsetZValidator);

		return keyboard;
	}


	public KeyboardInputCallback<Double> getCallbackForToolFlangeOffsetX() {
		return new KeyboardInputCallback<Double>() {
			@Override
			public void onOk(Double value) {
				setToolFlangeOffsetX(value);
				view.updateToolFlangeOffset(getToolFlangeOffsetX(), getToolFlangeOffsetY(), getToolFlangeOffsetZ());
			}
		};
	}

	public KeyboardInputCallback<Double> getCallbackForToolFlangeOffsetY() {
		return new KeyboardInputCallback<Double>() {
			@Override
			public void onOk(Double value) {
				setToolFlangeOffsetY(value);
				view.updateToolFlangeOffset(getToolFlangeOffsetX(), getToolFlangeOffsetY(), getToolFlangeOffsetZ());
			}
		};
	}

	public KeyboardInputCallback<Double> getCallbackForToolFlangeOffsetZ() {
		return new KeyboardInputCallback<Double>() {
			@Override
			public void onOk(Double value) {
				setToolflangeOffsetZ(value);
				view.updateToolFlangeOffset(getToolFlangeOffsetX(), getToolFlangeOffsetY(), getToolFlangeOffsetZ());
			}
		};
	}

	public ValueProvider<Double> getValueProviderForToolFlangeOffsetX() {
		return new ValueProvider<Double>() {
			@Override
			public Double get() {
				return getToolFlangeOffsetX();
			}
		};
	}

	public ValueProvider<Double> getValueProviderForToolFlangeOffsetY() {
		return new ValueProvider<Double>() {
			@Override
			public Double get() {
				return getToolFlangeOffsetY();
			}
		};
	}

	public ValueProvider<Double> getValueProviderForToolFlangeOffsetZ() {
		return new ValueProvider<Double>() {
			@Override
			public Double get() {
				return getToolFlangeOffsetZ();
			}
		};
	}


	public void moveToPosition() {
		robotMovement.requestUserToMoveRobot(getToolChangeJointPositions(), new RobotMovementCallback() {

			@Override
			public void onComplete(MovementCompleteEvent movementCompleteEvent) {

			}
		});
	}

	private static boolean epsilonEquals(double a, double b, double epsilon) {
		if (a == b) {
			return true;
		}

		return Math.abs(a - b) < epsilon;
	}
}
