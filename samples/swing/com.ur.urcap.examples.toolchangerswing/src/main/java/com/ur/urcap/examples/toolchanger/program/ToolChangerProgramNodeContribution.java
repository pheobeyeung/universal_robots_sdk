package com.ur.urcap.examples.toolchanger.program;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.tcp.TCP;
import com.ur.urcap.api.domain.tcp.TCPModel;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.value.jointposition.JointPosition;
import com.ur.urcap.api.domain.value.jointposition.JointPositions;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.examples.toolchanger.installation.ToolChangerInstallationNodeContribution;

import java.util.Collection;


public class ToolChangerProgramNodeContribution implements ProgramNodeContribution {
	private static final String SELECTED_TCP = "selected-tcp";
	private static final String PROGRAM_TREE_TITLE_BASE = "Change Tool";

	private final ProgramAPI programAPI;
	private final ToolChangerProgramNodeView view;
	private final DataModel model;
	private final TCPModel tcpModel;

	public ToolChangerProgramNodeContribution(ProgramAPIProvider apiProvider, ToolChangerProgramNodeView view, DataModel model) {
		this.programAPI = apiProvider.getProgramAPI();
		this.tcpModel = apiProvider.getProgramAPI().getTCPModel();
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
		TCP tcp = getSelectedTCP();

		return tcp != null ? PROGRAM_TREE_TITLE_BASE + ": " + tcp.getDisplayName() : PROGRAM_TREE_TITLE_BASE;
	}

	public Collection<TCP> getAllTCP() {
		return tcpModel.getTCPs();
	}

	public TCP getSelectedTCP() {
		return model.get(SELECTED_TCP, (TCP) null);
	}

	public void setSelectedTCP(final TCP tcp) {
		programAPI.getUndoRedoManager().recordChanges(
				new UndoableChanges() {
					@Override
					public void executeChanges() {
						model.set(SELECTED_TCP, tcp);
					}
				}
		);
	}

	@Override
	public boolean isDefined() {
		TCP tcp = getSelectedTCP();
		if (tcp == null) {
			return false;
		}

		if (getInstallationNode().getToolChangeJointPositions() == null) {
			return false;
		}
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		JointPositions q = getInstallationNode().getToolChangeJointPositions();
		if (q == null) {
			return;
		}

		JointPosition[] jpos = q.getAllJointPositions();
		writer.appendLine(String.format("movej([%f,%f,%f,%f,%f,%f])",
				jpos[0].getPosition(Angle.Unit.RAD),
				jpos[1].getPosition(Angle.Unit.RAD),
				jpos[2].getPosition(Angle.Unit.RAD),
				jpos[3].getPosition(Angle.Unit.RAD),
				jpos[4].getPosition(Angle.Unit.RAD),
				jpos[5].getPosition(Angle.Unit.RAD)
		));

		// "Simulate" the tool change operation
		writer.sleep(2.0);

		writer.set_tcp(getSelectedTCP());
	}

	private ToolChangerInstallationNodeContribution getInstallationNode() {
		return programAPI.getInstallationNode(ToolChangerInstallationNodeContribution.class);
	}

}
