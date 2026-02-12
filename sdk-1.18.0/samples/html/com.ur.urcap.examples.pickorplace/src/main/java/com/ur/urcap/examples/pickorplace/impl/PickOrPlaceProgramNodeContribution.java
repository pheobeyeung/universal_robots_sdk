package com.ur.urcap.examples.pickorplace.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.ProgramNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.component.InputButton;
import com.ur.urcap.api.ui.component.InputEvent;

public class PickOrPlaceProgramNodeContribution implements ProgramNodeContribution {

	private final URCapAPI urCapAPI;
	private final DataModel dataModel;

	private final String BUTTON_RESET_TEMPLATE = "buttonResetTemplate";
	private final String BUTTON_PICK_TEMPLATE_ID = "buttonPickTemplate";
	private final String BUTTON_PLACE_TEMPLATE_ID = "buttonPlaceTemplate";

	private final String TEMPLATE_NONE = "Pick or Place";
	private final String TEMPLATE_PICK = "Pick";
	private final String TEMPLATE_PLACE = "Place";
	private final String TEMPLATE_KEY = "templateType";

	@Input(id = BUTTON_RESET_TEMPLATE)
	public InputButton buttonResetTemplate;

	@Input(id = BUTTON_PICK_TEMPLATE_ID)
	public InputButton buttonPickTemplate;

	@Input(id = BUTTON_PLACE_TEMPLATE_ID)
	public InputButton buttonPlaceTemplate;

	public PickOrPlaceProgramNodeContribution(URCapAPI urCapAPI, DataModel dataModel) {
		this.urCapAPI = urCapAPI;
		this.dataModel = dataModel;
	}

	@Input(id = BUTTON_RESET_TEMPLATE)
	public void onButtonResetTemplateTap(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_PRESSED) {
			dataModel.set(TEMPLATE_KEY, TEMPLATE_NONE);
			clearSubtree();
			updateView();
		}
	}

	@Input(id = BUTTON_PICK_TEMPLATE_ID)
	public void onButtonPickTemplateTap(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_PRESSED) {
			dataModel.set(TEMPLATE_KEY, TEMPLATE_PICK);
			createSubtree(TEMPLATE_PICK);
			updateView();
		}
	}

	@Input(id = BUTTON_PLACE_TEMPLATE_ID)
	public void onButtonPlaceTemplateTap(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_PRESSED) {
			dataModel.set(TEMPLATE_KEY, TEMPLATE_PLACE);
			createSubtree(TEMPLATE_PLACE);
			updateView();
		}
	}

	private void addGripper(String template, TreeNode root, ProgramNodeFactory nf) throws TreeStructureException {
		Class<? extends ProgramNodeService> clazz =
				template.equals(TEMPLATE_PICK)
						? GripperCloseProgramNodeService.class
						: GripperOpenProgramNodeService.class;
		root.addChild(nf.createURCapProgramNode(clazz));
	}

	private boolean isTreeEmpty(){
		return urCapAPI.getProgramModel().getRootTreeNode(this).getChildren().size() == 0;
	}

	private void clearSubtree(){
		TreeNode subTree = urCapAPI.getProgramModel().getRootTreeNode(this);
		try {
			for (TreeNode child : subTree.getChildren()) {
				subTree.removeChild(child);
			}
		} catch (TreeStructureException e) {
			// See e.getMessage() for explanation
		}
	}

	private void createSubtree(String template) {
		ProgramModel programModel = urCapAPI.getProgramModel();
		ProgramNodeFactory nf = programModel.getProgramNodeFactory();
		TreeNode root = programModel.getRootTreeNode(this);
		root.setChildSequenceLocked(true);

		try {
			root.addChild(nf.createMoveNode());

			addGripper(template, root, nf);

			TreeNode folderRetract = root.addChild(nf.createFolderNode());
			folderRetract.addChild(nf.createCommentNode().setComment("Please customize your functionality here"));
		} catch (TreeStructureException e) {
			// See e.getMessage() for explanation
		}
	}

	private void updateView(){
		boolean emptyTree = isTreeEmpty();
		buttonResetTemplate.setEnabled(!emptyTree);
		buttonPickTemplate.setEnabled(emptyTree);
		buttonPlaceTemplate.setEnabled(emptyTree);
	}

	@Override
	public void openView() {
		updateView();
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return dataModel.get(TEMPLATE_KEY, TEMPLATE_NONE);
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.writeChildren();
	}

}
