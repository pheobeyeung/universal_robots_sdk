package com.ur.urcap.examples.ellipse.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.feature.Feature;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.nodes.builtin.MoveNode;
import com.ur.urcap.api.domain.program.nodes.builtin.WaypointNode;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.movenode.TCPSelection;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.movenode.builder.MovePConfigBuilder;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.BlendParameters;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.WaypointMotionParameters;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.WaypointNodeConfig;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.WaypointNodeConfigFactory;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.RobotPositionCallback2;
import com.ur.urcap.api.domain.userinteraction.UserInteraction;
import com.ur.urcap.api.domain.userinteraction.robot.movement.MovementCompleteEvent;
import com.ur.urcap.api.domain.userinteraction.robot.movement.MovementErrorEvent;
import com.ur.urcap.api.domain.userinteraction.robot.movement.RobotMovement;
import com.ur.urcap.api.domain.userinteraction.robot.movement.RobotMovementCallback;
import com.ur.urcap.api.domain.validation.ErrorHandler;
import com.ur.urcap.api.domain.value.Pose;
import com.ur.urcap.api.domain.value.ValueFactoryProvider;
import com.ur.urcap.api.domain.value.blend.Blend;
import com.ur.urcap.api.domain.value.robotposition.PositionParameters;
import com.ur.urcap.api.domain.value.simple.Acceleration;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.api.domain.value.simple.SimpleValueFactory;
import com.ur.urcap.api.domain.value.simple.Speed;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.LabelComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EllipseProgramNodeContribution implements ProgramNodeContribution {

	private static final String ERROR_LABEL = "errorLabel";
	private static final String DEFINED_KEY = "is_defined";
	private static final String BUTTON_SELECT_POSE = "buttonSelectPose";
	private static final String BUTTON_MOVE_HERE = "buttonMoveHere";
	private static final String CENTER_POSITION = "center_pose";

	private static final double SHARED_TOOL_SPEED = 250;
	private static final double SHARED_TOOL_ACCELERATION = 1200;
	private static final double SHARED_BLEND_RADIUS_IN_MM = 23;
	private static final double HORIZONTAL_RADIUS_IN_MM = 200.0;
	private static final double VERTICAL_RADIUS_IN_MM = 120.0;

	private static final int NUMBER_OF_WAYPOINTS = 16;

	private final ProgramAPI programAPI;
	private final ProgramModel programModel;
	private final UserInteraction userInteraction;
	private final ValueFactoryProvider valueFactoryProvider;

	private MoveNode moveNode;
	private final List<WaypointNode> waypointNodes = new ArrayList<WaypointNode>();

	private DataModel dataModel;
	private TreeNode moveTreeNode;

	private WaypointNodeConfigFactory waypointNodeConfigFactory;
	private ProgramNodeFactory programNodeFactory;
	private final RobotMovement robotMovement;

	private final BufferedImage errorIcon;

	private EllipseState ellipseState = new EllipseState();

	@Label(id = ERROR_LABEL)
	public LabelComponent errorLabel;

	@Input(id = BUTTON_MOVE_HERE)
	private InputButton buttonMoveHere;

	public EllipseProgramNodeContribution(URCapAPI urCapAPI, DataModel dataModel) {
		this.dataModel = dataModel;
		this.errorIcon = getErrorImage();

		programAPI = urCapAPI.getProgramAPIProvider().getProgramAPI();
		programModel = programAPI.getProgramModel();
		valueFactoryProvider = programAPI.getValueFactoryProvider();
		programNodeFactory = programModel.getProgramNodeFactory();
		waypointNodeConfigFactory = programNodeFactory.createWaypointNode().getConfigFactory();
		userInteraction = urCapAPI.getProgramAPIProvider().getUserInterfaceAPI().getUserInteraction();
		robotMovement = userInteraction.getRobotMovement();
	}

	@Input(id = BUTTON_SELECT_POSE)
	public void onSelectPoseButtonPressed(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_PRESSED) {
			clearErrors();
			selectCenterPoint();
		}
	}

	private void selectCenterPoint() {
		userInteraction.getUserDefinedRobotPosition(new RobotPositionCallback2() {
			@Override
			public void onOk(PositionParameters positionParameters) {
				removeNodes();
				createNodes();
				configureMoveNode();
				adjustWaypointsToCenterPoint(positionParameters);
			}
		});
	}

	@Input(id = BUTTON_MOVE_HERE)
	public void onMoveToCenterPose(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_PRESSED) {
			clearErrors();
			moveToCenterPoint();
		}
	}

	private void moveToCenterPoint() {
		Pose centerPose = dataModel.get(CENTER_POSITION, (Pose) null);
		if (centerPose != null) {
			robotMovement.requestUserToMoveRobot(centerPose, new RobotMovementCallback() {

				@Override
				public void onComplete(MovementCompleteEvent event) {

				}

				@Override
				public void onError(MovementErrorEvent event) {
					updateError(new EllipseState(getErrorMessage(event.getErrorType())));
				}
			});
		}
	}

	private String getErrorMessage(MovementErrorEvent.ErrorType errorType) {
		switch (errorType) {
			case UNREACHABLE_POSE:
				return "Could not move to center point.";

			default:
				return "Error in move here";
		}
	}

	private void removeNodes() {
		TreeNode rootTreeNode = programModel.getRootTreeNode(this);
		try {
			for (TreeNode child : rootTreeNode.getChildren()) {
				rootTreeNode.removeChild(child);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void adjustWaypointsToCenterPoint(PositionParameters positionParameters) {
		dataModel.set(CENTER_POSITION, positionParameters.getPose());
		try {
			configureWaypointNodes(positionParameters);
		} catch (IllegalArgumentException e) {
			updateError(new EllipseState("Could not create ellipse movement<br>Try a different center point."));
			resetWaypointNodes();
		}
		lockTreeNodes();
		if (buttonMoveHere != null) {
			buttonMoveHere.setEnabled(true);
		}
	}

	private void resetWaypointNodes() {
		BlendParameters blendParameters = waypointNodeConfigFactory.createSharedBlendParameters();
		WaypointMotionParameters motionParameters = waypointNodeConfigFactory.createSharedMotionParameters();
		for (WaypointNode waypointNode : waypointNodes) {
			WaypointNodeConfig waypointNodeConfig = waypointNodeConfigFactory.
					createFixedPositionConfig(blendParameters, motionParameters);
			waypointNode.setConfig(waypointNodeConfig);
		}
	}

	private void configureWaypointNodes(PositionParameters positionParameters) {
		// adjust orientation according to base
		double baseAngle = positionParameters.getJointPositions().getAllJointPositions()[0].getAngle(Angle.Unit.RAD) + (Math.PI / 2);
		double xContribution = Math.cos(baseAngle);
		double yContribution = Math.sin(baseAngle);

		double angle = -Math.PI;
		double angularStepDistance = (2 * Math.PI) / (double) NUMBER_OF_WAYPOINTS;

		for (WaypointNode waypointNode : waypointNodes) {
			angle += angularStepDistance;
			double offsetX = Math.cos(angle) * HORIZONTAL_RADIUS_IN_MM * xContribution;
			double offsetY = Math.cos(angle) * HORIZONTAL_RADIUS_IN_MM * yContribution;
			double offsetZ = Math.sin(-angle) * VERTICAL_RADIUS_IN_MM;

			WaypointNodeConfig newWaypointNodeConfig = createWaypointConfig(positionParameters, offsetX, offsetY, offsetZ);
			waypointNode.setConfig(newWaypointNodeConfig);
		}
	}

	private WaypointNodeConfig createWaypointConfig(PositionParameters positionParameters,
													double xOffsetInMM, double yOffsetInMM, double zOffsetInMM) {
		BlendParameters blendParameters = waypointNodeConfigFactory.createSharedBlendParameters();
		WaypointMotionParameters motionParameters = waypointNodeConfigFactory.createSharedMotionParameters();
		Pose pose = createPoseUsingCenterPoseAndOffset(positionParameters.getPose(), xOffsetInMM, yOffsetInMM, zOffsetInMM, Length.Unit.MM);

		return waypointNodeConfigFactory.createFixedPositionConfig(pose, positionParameters.getJointPositions(), positionParameters.getTCPOffset(),
				blendParameters, motionParameters);
	}

	private Pose createPoseUsingCenterPoseAndOffset(Pose pose, double xOffset, double yOffset,
													double zOffset, Length.Unit unit) {
		double x = pose.getPosition().getX(unit) + xOffset;
		double y = pose.getPosition().getY(unit) + yOffset;
		double z = pose.getPosition().getZ(unit) + zOffset;
		double rx = pose.getRotation().getRX(Angle.Unit.RAD);
		double ry = pose.getRotation().getRY(Angle.Unit.RAD);
		double rz = pose.getRotation().getRZ(Angle.Unit.RAD);
		return valueFactoryProvider.getPoseFactory().
				createPose(x, y, z, rx, ry, rz, unit, Angle.Unit.RAD);
	}

	private void createNodes() {
		try {
			moveNode = programNodeFactory.createMoveNodeNoTemplate();
			TreeNode rootTreeNode = programModel.getRootTreeNode(this);
			moveTreeNode = rootTreeNode.addChild(moveNode);

			waypointNodes.clear();
			for (int i = 1; i <= NUMBER_OF_WAYPOINTS; i++) {
				createAndAddWaypointNode(i);
			}
		} catch (TreeStructureException e) {
			e.printStackTrace();
		}
	}

	private void configureMoveNode() {
		SimpleValueFactory valueFactory = valueFactoryProvider.getSimpleValueFactory();

		Speed speed = valueFactory.createSpeed(SHARED_TOOL_SPEED, Speed.Unit.MM_S);
		Acceleration acceleration = valueFactory.createAcceleration(SHARED_TOOL_ACCELERATION, Acceleration.Unit.MM_S2);
		Length length = valueFactory.createLength(SHARED_BLEND_RADIUS_IN_MM, Length.Unit.MM);
		Blend blend = valueFactoryProvider.getBlendFactory().createBlend(length);
		Feature feature = programAPI.getFeatureModel().getBaseFeature();
		TCPSelection tcpSelection = moveNode.getTCPSelectionFactory().createActiveTCPSelection();

		MovePConfigBuilder movePConfigBuilder = moveNode.getConfigBuilders().createMovePConfigBuilder()
				.setToolSpeed(speed, ErrorHandler.AUTO_CORRECT)
				.setToolAcceleration(acceleration, ErrorHandler.AUTO_CORRECT)
				.setBlend(blend, ErrorHandler.AUTO_CORRECT)
				.setFeature(feature)
				.setTCPSelection(tcpSelection);

		moveNode.setConfig(movePConfigBuilder.build());
	}

	private void createAndAddWaypointNode(int waypointNumber) throws TreeStructureException {
		String waypointName = createWaypointName(waypointNumber);
		WaypointNode waypointNode = programNodeFactory.createWaypointNode(waypointName);
		moveTreeNode.addChild(waypointNode);
		waypointNodes.add(waypointNode);
	}

	private static String createWaypointName(int waypointNumber) {
		return "EllipsePos" + waypointNumber;
	}

	@Override
	public void openView() {
		if (buttonMoveHere != null) {
			buttonMoveHere.setEnabled(dataModel.get(CENTER_POSITION, (Pose) null) != null);
		}
		updateError(this.ellipseState);
	}

	@Override
	public void closeView() {
		// nothing needs to happen here in this example
	}

	@Override
	public String getTitle() {
		return "Ellipse";
	}

	@Override
	public boolean isDefined() {
		return dataModel.get(DEFINED_KEY, false);
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.writeChildren();
	}

	private void lockTreeNodes() {
		TreeNode thisTreeNode = programModel.getRootTreeNode(this);
		thisTreeNode.setChildSequenceLocked(true);
		moveTreeNode.setChildSequenceLocked(true);
	}

	private BufferedImage getErrorImage() {
		BufferedImage image;
		try {
			image = ImageIO.read(getClass().getResource("/com/ur/urcap/examples/ellipse/impl/warning-bigger.png"));
		} catch (IOException e) {
			// Should not happen.
			throw new RuntimeException("Unexpected exception while loading icon.", e);
		}
		return image;
	}

	private void clearErrors() {
		updateError(new EllipseState());
	}

	private void updateError(EllipseState ellipseState) {
		this.ellipseState = ellipseState;
		if (errorLabel != null) {
			errorLabel.setVisible(ellipseState.isError());
			errorLabel.setText("<html>Error: " + ellipseState.getMessage() + "</html>");
			errorLabel.setImage(errorIcon);
		}
		setDefined(!ellipseState.isError());
	}

	private void setDefined(boolean defined) {
		dataModel.set(DEFINED_KEY, defined);
	}

	private static class EllipseState {
		private final String message;
		private final boolean isError;

		EllipseState() {
			this.isError = false;
			this.message = "";
		}

		EllipseState(String message) {
			this.isError = true;
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public boolean isError() {
			return isError;
		}
	}
}
