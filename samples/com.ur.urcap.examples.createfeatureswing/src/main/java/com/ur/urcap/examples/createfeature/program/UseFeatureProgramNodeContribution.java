package com.ur.urcap.examples.createfeature.program;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.feature.Feature;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.nodes.builtin.MoveNode;
import com.ur.urcap.api.domain.program.nodes.builtin.WaypointNode;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.movenode.MoveLMoveNodeConfig;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.movenode.builder.MoveLConfigBuilder;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.movenode.builder.MoveNodeConfigBuilders;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.BlendParameters;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.FixedPositionDefinedWaypointNodeConfig;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.WaypointMotionParameters;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waypointnode.WaypointNodeConfigFactory;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.value.Pose;
import com.ur.urcap.api.domain.value.PoseFactory;
import com.ur.urcap.api.domain.value.Position;
import com.ur.urcap.api.domain.value.Rotation;
import com.ur.urcap.api.domain.value.jointposition.JointPositions;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.examples.createfeature.installation.CreateFeatureInstallationNodeContribution;

import java.util.List;

public class UseFeatureProgramNodeContribution implements ProgramNodeContribution {
	private final ProgramAPI programAPI;
	private final ProgramModel programModel;
	private final UseFeatureProgramNodeView view;
	private final ProgramNodeFactory programNodeFactory;
	private final PoseFactory poseFactory;

	private boolean moveCreationPossible = true;
	private final Pose toolFlangeOffset;

	UseFeatureProgramNodeContribution(ProgramAPIProvider apiProvider, UseFeatureProgramNodeView view) {
		poseFactory = apiProvider.getProgramAPI().getValueFactoryProvider().getPoseFactory();
		programAPI = apiProvider.getProgramAPI();
		programModel = apiProvider.getProgramAPI().getProgramModel();
		this.view = view;
		programNodeFactory = programModel.getProgramNodeFactory();
		final TreeNode rootNode = programModel.getRootTreeNode(this);
		rootNode.setChildSequenceLocked(true);

		toolFlangeOffset = poseFactory.createPose(0, 0, 0, 0, 0, 0, Length.Unit.M, Angle.Unit.RAD);
	}

	@Override
	public void openView() {
		boolean featureCreated = getInstallationNode().isFeatureCreated();
		view.showCreateFeatureWarningMessage(!featureCreated);
		view.showMoveFeatureWarningMessage(!moveCreationPossible);
		view.enableCreateButton(featureCreated && !isMoveNodeAdded());
	}

	@Override
	public void closeView() {
		// Intentionally left empty
	}

	@Override
	public String getTitle() {
		return "Use feature";
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter scriptWriter) {
		scriptWriter.writeChildren();
	}

	void create() {
		programAPI.getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				createMovement();
				view.enableCreateButton(false);
			}
		});
	}

	private void createMovement() {
		final TreeNode rootNode = programModel.getRootTreeNode(this);
		TreeNode moveTreeNode = null;
		try {
			MoveNode moveNode = createMoveNode();
			moveTreeNode = rootNode.addChild(moveNode);
			addWaypoints(moveTreeNode);
			moveTreeNode.setChildSequenceLocked(true);
			moveCreationPossible = true;
		} catch (Exception e) {
			moveCreationPossible = false;
			if (moveTreeNode != null) {
				try {
					// Remove the partially added Move node
					rootNode.removeChild(moveTreeNode);
				} catch (TreeStructureException ex) {
					// Do nothing
				}
			}
		} finally {
			view.showMoveFeatureWarningMessage(!moveCreationPossible);
		}

	}

	private MoveNode createMoveNode() {
		MoveNode moveNode = programNodeFactory.createMoveNodeNoTemplate();
		MoveNodeConfigBuilders builders = moveNode.getConfigBuilders();
		MoveLConfigBuilder builder = builders.createMoveLConfigBuilder();
		builder.setFeature(getInstallationNode().getFeature());
		MoveLMoveNodeConfig moveLMoveNodeConfig = builder.build();
		moveNode.setConfig(moveLMoveNodeConfig);
		return moveNode;
	}

	private void addWaypoints(TreeNode moveTreeNode) {
		Feature feature = getInstallationNode().getFeature();
		Pose pose = feature.getPose();
		Position position = pose.getPosition();
		double x = position.getX(Length.Unit.M);
		double y = position.getY(Length.Unit.M);
		double z = position.getZ(Length.Unit.M);
		Rotation rotation = pose.getRotation();
		double rx = rotation.getRX(Angle.Unit.RAD);
		double ry = rotation.getRY(Angle.Unit.RAD);
		double rz = rotation.getRZ(Angle.Unit.RAD);
		Pose lowLeft = poseFactory.createPose(x, y-0.1,z-0.1,rx,ry,rz, Length.Unit.M, Angle.Unit.RAD);
		Pose lowRight = poseFactory.createPose(x, y+0.1,z-0.1,rx,ry,rz, Length.Unit.M, Angle.Unit.RAD);
		Pose topLeft = poseFactory.createPose(x, y-0.1,z+0.1,rx,ry,rz, Length.Unit.M, Angle.Unit.RAD);
		Pose topRight = poseFactory.createPose(x, y+0.1,z+0.1,rx,ry,rz, Length.Unit.M, Angle.Unit.RAD);

		addWayPoint(moveTreeNode, "Corner_1", lowLeft);
		addWayPoint(moveTreeNode, "Corner_2", lowRight);
		addWayPoint(moveTreeNode, "Corner_3", topRight);
		addWayPoint(moveTreeNode, "Corner_4", topLeft);
	}

	private void addWayPoint(TreeNode moveTreeNode, String waypointName, Pose waypointPose) {
		WaypointNode waypoint = programNodeFactory.createWaypointNode(waypointName);
		try {
			moveTreeNode.addChild(waypoint);
		} catch (TreeStructureException e) {
			throw new IllegalStateException("Could not add waypoint node to move node", e);
		}
		WaypointNodeConfigFactory configFactory = waypoint.getConfigFactory();
		BlendParameters noBlendParameters = configFactory.createNoBlendParameters();
		WaypointMotionParameters sharedMotionParameters = configFactory.createSharedMotionParameters();
		JointPositions featureJointPositions = getInstallationNode().getFeatureJointPositions();
		FixedPositionDefinedWaypointNodeConfig fixedPositionConfig =
				configFactory.createFixedPositionConfig(waypointPose, featureJointPositions, toolFlangeOffset, noBlendParameters, sharedMotionParameters);
		waypoint.setConfig(fixedPositionConfig);
	}

	private CreateFeatureInstallationNodeContribution getInstallationNode() {
		return programAPI.getInstallationNode(CreateFeatureInstallationNodeContribution.class);
	}

	private boolean isMoveNodeAdded() {
		List<TreeNode> children = programModel.getRootTreeNode(this).getChildren();
		return children.size() == 1 && children.get(0).getProgramNode() instanceof MoveNode;
	}
}
