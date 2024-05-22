package com.ur.urcap.examples.pickorplaceswing.pickorplace;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.CreationContext.NodeCreationType;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.examples.pickorplaceswing.close.GripperCloseProgramNodeService;
import com.ur.urcap.examples.pickorplaceswing.open.GripperOpenProgramNodeService;

public class PickOrPlaceProgramNodeContribution implements ProgramNodeContribution {

	private static final String TEMPLATE_KEY = "templateType";

	private final ProgramAPIProvider apiProvider;
	private final PickOrPlaceProgramNodeView view;
	private final DataModel model;

	public PickOrPlaceProgramNodeContribution(ProgramAPIProvider apiProvider, PickOrPlaceProgramNodeView view,
			DataModel model, CreationContext context) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		lockChildSequence(apiProvider);
		if (context.getNodeCreationType() == NodeCreationType.NEW) {
			this.setModel(TemplateType.EMPTY.getName());
		}
	}

	@Override
	public String getTitle() {
		return getTemplateType().getName();
	}

	@Override
	public void openView() {
		this.view.update(this);
	}

	@Override
	public void closeView() {
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.writeChildren();
	}

	private void setModel(final String variable) {
		model.set(TEMPLATE_KEY, variable);
	}

	public void reset() {
		apiProvider.getProgramAPI().getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				setModel(TemplateType.EMPTY.getName());
				clearSubtree();
				view.update(PickOrPlaceProgramNodeContribution.this);
			}
		});
	}

	public void createPick() {
		apiProvider.getProgramAPI().getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				setModel(TemplateType.PICK.getName());
				createSubtree(TemplateType.PICK);
				view.update(PickOrPlaceProgramNodeContribution.this);
			}
		});
	}

	public void createPlace() {
		apiProvider.getProgramAPI().getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				setModel(TemplateType.PLACE.getName());
				createSubtree(TemplateType.PLACE);
				view.update(PickOrPlaceProgramNodeContribution.this);
			}
		});
	}

	public TemplateType getTemplateType() {
		String templateTypeName = model.get(TEMPLATE_KEY, TemplateType.EMPTY.getName());
		return TemplateType.valueOfByName(templateTypeName);
	}

	private void clearSubtree() {
		ProgramAPI programAPI = apiProvider.getProgramAPI();
		ProgramModel programModel = programAPI.getProgramModel();

		TreeNode subTree = programModel.getRootTreeNode(this);

		try {
			for (TreeNode child : subTree.getChildren()) {
				subTree.removeChild(child);
			}
		} catch (TreeStructureException e) {
			e.printStackTrace();
			// See e.getMessage() for explanation
		}
	}

	private void createSubtree(TemplateType template) {
		ProgramAPI programAPI = apiProvider.getProgramAPI();
		ProgramModel programModel = programAPI.getProgramModel();
		ProgramNodeFactory nf = programModel.getProgramNodeFactory();
		TreeNode root = programModel.getRootTreeNode(this);
		try {
			root.addChild(nf.createMoveNode());

			addGripper(template, root, nf);

			TreeNode folderRetract = root.addChild(nf.createFolderNode());
			folderRetract.addChild(nf.createCommentNode().setComment("Please customize your functionality here"));
		} catch (TreeStructureException e) {
			e.printStackTrace();
			// See e.getMessage() for explanation
		}
	}

	private void addGripper(TemplateType template, TreeNode root, ProgramNodeFactory nf) throws TreeStructureException {
		switch (template) {
		case PICK:
			root.addChild(nf.createURCapProgramNode(GripperCloseProgramNodeService.class));
			break;
		case PLACE:
			root.addChild(nf.createURCapProgramNode(GripperOpenProgramNodeService.class));
			break;
		default:
			break;
		}
	}

	private void lockChildSequence(ProgramAPIProvider apiProvider) {
		ProgramAPI programAPI = apiProvider.getProgramAPI();
		ProgramModel programModel = programAPI.getProgramModel();
		TreeNode root = programModel.getRootTreeNode(this);
		root.setChildSequenceLocked(true);
	}
}
