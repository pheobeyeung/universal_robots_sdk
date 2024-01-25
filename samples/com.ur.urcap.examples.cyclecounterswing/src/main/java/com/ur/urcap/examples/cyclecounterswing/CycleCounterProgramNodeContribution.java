package com.ur.urcap.examples.cyclecounterswing;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.nodes.builtin.InvalidDomainException;
import com.ur.urcap.api.domain.program.nodes.builtin.WaitNode;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waitnode.TimeWaitNodeConfig;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
import com.ur.urcap.api.domain.util.Filter;
import com.ur.urcap.api.domain.validation.ErrorHandler;
import com.ur.urcap.api.domain.value.expression.InvalidExpressionException;
import com.ur.urcap.api.domain.value.simple.Time;
import com.ur.urcap.api.domain.variable.GlobalVariable;
import com.ur.urcap.api.domain.variable.Variable;
import com.ur.urcap.api.domain.variable.VariableException;
import com.ur.urcap.api.domain.variable.VariableFactory;

import java.util.Collection;


public class CycleCounterProgramNodeContribution implements ProgramNodeContribution {
	public static final String SELECTED_VAR = "selectedVar";
	private static final String TEXT = "Text";

	private final DataModel model;
	private final ProgramAPI programAPI;
	private final CycleCounterProgramNodeView view;
	private final VariableFactory variableFactory;
	private final KeyboardInputFactory keyboardInputFactory;

	public CycleCounterProgramNodeContribution(ProgramAPIProvider apiProvider, CycleCounterProgramNodeView view, DataModel model) {

		this.programAPI = apiProvider.getProgramAPI();
		this.view = view;
		this.model = model;
		this.variableFactory = apiProvider.getProgramAPI().getVariableModel().getVariableFactory();
		this.keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();

		insertNode();
	}

	private void insertNode() {
		try {
			ProgramNodeFactory programNodeFactory = programAPI.getProgramModel().getProgramNodeFactory();
			WaitNode waitNode = programNodeFactory.createWaitNode();

			Time oneSecondWait = programAPI.getValueFactoryProvider().getSimpleValueFactory().createTime(1, Time.Unit.S);
			TimeWaitNodeConfig config = waitNode.getConfigFactory().createTimeConfig(oneSecondWait, ErrorHandler.AUTO_CORRECT);

			programAPI.getProgramModel().getRootTreeNode(this).addChild(waitNode.setConfig(config));
		} catch (TreeStructureException e) {
			e.printStackTrace();
		} catch (InvalidDomainException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void openView() {
		view.updateView(this);
	}

	@Override
	public void closeView() {
		// no action needed when closing view
	}

	@Override
	public String getTitle() {
		return "Cycle Counter";
	}

	@Override
	public boolean isDefined() {
		return getSelectedVariable() != null;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.writeChildren();

		Variable variable = getSelectedVariable();
		if (variable != null) {
			//Use writer to resolve name, as the variable can be renamed at any time by the end user.
			String resolvedVariableName = writer.getResolvedVariableName(variable);
			writer.appendLine(resolvedVariableName + "=" + resolvedVariableName + "+1");

			//Note that the two code lines above can be replaced by the code line below which will resolve the variable
			//and increment it:
			//
			//writer.incrementVariable(variable)
			//
			//but to demonstrate renaming concerns the above lines are used.
		}
	}

	public Variable getSelectedVariable() {
		return model.get(SELECTED_VAR, (Variable) null);
	}

	public GlobalVariable createGlobalVariable(String variableName) {
		GlobalVariable variable = null;
		try {
			variable = variableFactory.createGlobalVariable(variableName, programAPI.getValueFactoryProvider().createExpressionBuilder().append("0").build());
		} catch (VariableException e) {
			view.setError(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (InvalidExpressionException e) {
			view.setError(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return variable;
	}

	public void setVariable(final Variable variable) {
		programAPI.getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(SELECTED_VAR, variable);
			}
		});
	}

	public void removeVariable() {
		programAPI.getUndoRedoManager().recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.remove(SELECTED_VAR);
			}
		});
	}

	public Collection<Variable> getGlobalVariables() {
		return programAPI.getVariableModel().get(new Filter<Variable>() {
			@Override
			public boolean accept(Variable element) {
				return element.getType().equals(Variable.Type.GLOBAL) || element.getType().equals(Variable.Type.VALUE_PERSISTED);
			}
		});
	}

	public KeyboardTextInput getKeyboardForInput() {
		KeyboardTextInput keyboard = keyboardInputFactory.createStringKeyboardInput();
		keyboard.setInitialValue(model.get(TEXT, ""));
		return keyboard;
	}

	public KeyboardInputCallback<String> getCallbackForInput() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				view.setNewVariable(value);
			}
		};
	}
}
