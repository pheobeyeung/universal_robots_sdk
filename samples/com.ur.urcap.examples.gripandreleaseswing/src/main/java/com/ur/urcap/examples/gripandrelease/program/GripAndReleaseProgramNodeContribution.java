package com.ur.urcap.examples.gripandrelease.program;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.device.gripper.GripperDevice;
import com.ur.urcap.api.domain.device.gripper.GripperManager;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.program.nodes.contributable.device.gripper.GripperNode;
import com.ur.urcap.api.domain.program.nodes.contributable.device.gripper.configuration.GripActionConfig;
import com.ur.urcap.api.domain.program.nodes.contributable.device.gripper.configuration.GripActionConfigBuilder;
import com.ur.urcap.api.domain.program.nodes.contributable.device.gripper.configuration.ReleaseActionConfig;
import com.ur.urcap.api.domain.program.nodes.contributable.device.gripper.configuration.ReleaseActionConfigBuilder;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;

import java.util.List;


public class GripAndReleaseProgramNodeContribution implements ProgramNodeContribution {
	private static final String SELECTED_DEVICE = "selected-device";
	private static final String PROGRAM_TREE_TITLE_BASE = "Grip And Release";

	private final ProgramAPI programAPI;
	private final GripAndReleaseProgramNodeView view;
	private final DataModel model;
	private final GripperManager gripperManager;
	private final UndoRedoManager undoRedoManager;

	public GripAndReleaseProgramNodeContribution(ProgramAPIProvider apiProvider, GripAndReleaseProgramNodeView view, DataModel model) {
		this.programAPI = apiProvider.getProgramAPI();
		this.gripperManager = programAPI.getDeviceManager(GripperManager.class);
		this.undoRedoManager = programAPI.getUndoRedoManager();
		this.view = view;
		this.model = model;
	}

	@Override
	public void openView() {
		view.updateView();
	}

	@Override
	public void closeView() {
		
	}

	@Override
	public String getTitle() {
		return isGripperSelected() ? PROGRAM_TREE_TITLE_BASE + ": " + getSelectedGripperDevice() : PROGRAM_TREE_TITLE_BASE;
	}

	private boolean isGripperSelected() {
		return getSelectedGripperDevice() != null;
	}

	public GripperDevice getSelectedGripperDevice() {
		return model.get(SELECTED_DEVICE, null, GripperDevice.class);
	}

	@Override
	public boolean isDefined() {
		return isGripperSelected();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// intentionally left empty
	}

	public void createGripperNodeWithGripAndReleaseConfig(final GripperDevice gripperDevice) {
		undoRedoManager.recordChanges(
				new UndoableChanges() {
					@Override
					public void executeChanges() {
						try {
							clearSubtree();

							TreeNode root = programAPI.getProgramModel().getRootTreeNode(GripAndReleaseProgramNodeContribution.this);

							GripperNode gripConfiguredGripperProgramNode = gripperManager.getGripperProgramNodeFactory().createGripperNode(gripperDevice);
							GripActionConfigBuilder gripConfigBuilder = gripConfiguredGripperProgramNode.createGripActionConfigBuilder();
							GripActionConfig gripConfig = gripConfigBuilder.build();
							gripConfiguredGripperProgramNode.setConfig(gripConfig);
							root.addChild(gripConfiguredGripperProgramNode);

							GripperNode releaseConfiguredGripperProgramNode = gripperManager.getGripperProgramNodeFactory().createGripperNode(gripperDevice);
							ReleaseActionConfigBuilder releaseConfigBuilder = releaseConfiguredGripperProgramNode.createReleaseActionConfigBuilder();
							ReleaseActionConfig releaseConfig = releaseConfigBuilder.build();
							releaseConfiguredGripperProgramNode.setConfig(releaseConfig);
							root.addChild(releaseConfiguredGripperProgramNode);

							setSelectedGripperDevice(gripperDevice);

						} catch (TreeStructureException e) {
							e.printStackTrace();
						}
					}
				}
		);
	}

	private void clearSubtree() {
		TreeNode subTree = programAPI.getProgramModel().getRootTreeNode(this);
		try {
			for (TreeNode child : subTree.getChildren()) {
				subTree.removeChild(child);
			}
		} catch (TreeStructureException e) {
			e.printStackTrace();
		}
	}

	private void setSelectedGripperDevice(GripperDevice gripperDevice) {
		model.set(SELECTED_DEVICE, gripperDevice);
	}

	public List<GripperDevice> getGrippers() {
		return gripperManager.getGrippers();
	}
}
