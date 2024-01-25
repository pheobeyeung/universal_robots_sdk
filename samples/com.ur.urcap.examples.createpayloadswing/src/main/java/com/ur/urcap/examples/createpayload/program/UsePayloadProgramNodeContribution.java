package com.ur.urcap.examples.createpayload.program;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.builtin.SetPayloadNode;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.setpayloadnode.SelectionPayloadNodeConfig;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.examples.createpayload.installation.CreatePayloadInstallationNodeContribution;

import java.util.List;


public class UsePayloadProgramNodeContribution implements ProgramNodeContribution {
	private final UsePayloadProgramNodeView view;
	private final ProgramAPI programAPI;

	UsePayloadProgramNodeContribution(ProgramAPIProvider apiProvider, UsePayloadProgramNodeView view) {
		programAPI = apiProvider.getProgramAPI();
		this.view = view;

		ProgramModel programModel = apiProvider.getProgramAPI().getProgramModel();
		TreeNode rootNode = programModel.getRootTreeNode(this);
		rootNode.setChildSequenceLocked(true);
	}

	@Override
	public void openView() {
		view.showWarningLabel(!getInstallationNode().isPayloadCreated());
		view.enableCreateButton(getInstallationNode().isPayloadCreated() && !isPayloadNodeInTree());
	}

	@Override
	public void closeView() {
		// Intentionally left empty
	}

	@Override
	public String getTitle() {
		return "Use Payload";
	}

	@Override
	public boolean isDefined() {
		return getInstallationNode().isPayloadCreated();
	}

	@Override
	public void generateScript(ScriptWriter scriptWriter) {
		scriptWriter.writeChildren();
	}

	private boolean isPayloadNodeInTree() {
		List<TreeNode> children = programAPI.getProgramModel().getRootTreeNode(this).getChildren();
		return children.size() > 0;
	}

	void create() {
		programAPI.getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				createAndInsertPayloadNode();
			}
		});
		openView();
	}

	private void createAndInsertPayloadNode() {
		ProgramModel programModel = programAPI.getProgramModel();

		TreeNode rootNode = programModel.getRootTreeNode(this);
		SetPayloadNode setPayloadNode = programModel.getProgramNodeFactory().createSetPayloadNode();
		SelectionPayloadNodeConfig config = setPayloadNode.getConfigFactory().createSelectionConfig(getInstallationNode().getPayload());
		setPayloadNode.setConfig(config);

		try {
			rootNode.addChild(setPayloadNode);
		} catch (TreeStructureException e) {
			e.printStackTrace();
		}
	}

	private CreatePayloadInstallationNodeContribution getInstallationNode() {
		return programAPI.getInstallationNode(CreatePayloadInstallationNodeContribution.class);
	}
}
