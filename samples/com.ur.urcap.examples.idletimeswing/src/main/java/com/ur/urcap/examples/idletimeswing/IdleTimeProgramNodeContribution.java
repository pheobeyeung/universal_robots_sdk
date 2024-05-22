package com.ur.urcap.examples.idletimeswing;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.nodes.builtin.InvalidDomainException;
import com.ur.urcap.api.domain.program.nodes.builtin.WaitNode;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waitnode.TimeWaitNodeConfig;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waitnode.WaitNodeConfig;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waitnode.WaitNodeConfigFactory;
import com.ur.urcap.api.domain.program.structure.ProgramNodeVisitor;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
import com.ur.urcap.api.domain.util.Filter;
import com.ur.urcap.api.domain.validation.ErrorHandler;
import com.ur.urcap.api.domain.value.expression.InvalidExpressionException;
import com.ur.urcap.api.domain.value.simple.SimpleValueFactory;
import com.ur.urcap.api.domain.value.simple.Time;
import com.ur.urcap.api.domain.variable.GlobalVariable;
import com.ur.urcap.api.domain.variable.Variable;
import com.ur.urcap.api.domain.variable.VariableException;
import com.ur.urcap.api.domain.variable.VariableFactory;

import java.util.Collection;


public class IdleTimeProgramNodeContribution implements ProgramNodeContribution {
	public static final String SELECTED_VAR = "selectedVar";
	private static final String TEXT = "Text";

	private final DataModel model;
	private final ProgramAPI programAPI;
	private final IdleTimeProgramNodeView view;
	private final VariableFactory variableFactory;
	private final KeyboardInputFactory keyboardInputFactory;
	private final UndoRedoManager undoRedoManager;

	public IdleTimeProgramNodeContribution(ProgramAPIProvider apiProvider, IdleTimeProgramNodeView view, DataModel model) {

		this.programAPI = apiProvider.getProgramAPI();
		this.view = view;
		this.model = model;
		this.variableFactory = apiProvider.getProgramAPI().getVariableModel().getVariableFactory();
		this.keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();

		createSubTreeTemplate(apiProvider);
	}

	private void createSubTreeTemplate(ProgramAPIProvider apiProvider) {
		ProgramNodeFactory programNodeFactory = apiProvider.getProgramAPI().getProgramModel().getProgramNodeFactory();
		WaitNode waitNode = programNodeFactory.createWaitNode();
		SimpleValueFactory valueFactory = apiProvider.getProgramAPI().getValueFactoryProvider().getSimpleValueFactory();
		WaitNodeConfigFactory configFactory = waitNode.getConfigFactory();

		TimeWaitNodeConfig configA = configFactory.createTimeConfig(valueFactory.createTime(1, Time.Unit.S), ErrorHandler.AUTO_CORRECT);
		TimeWaitNodeConfig configB = configFactory.createTimeConfig(valueFactory.createTime(2.5, Time.Unit.S), ErrorHandler.AUTO_CORRECT);

		TreeNode root = apiProvider.getProgramAPI().getProgramModel().getRootTreeNode(this);

		try {
			root.addChild(programNodeFactory.createWaitNode().setConfig(configA));
			root.addChild(programNodeFactory.createFolderNode()).addChild(programNodeFactory.createWaitNode().setConfig(configB));
			root.addChild(programNodeFactory.createWaitNode().setConfig(configA));
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

	}

	@Override
	public String getTitle() {
		return "Idle Time";
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
			writer.appendLine(resolvedVariableName + "=" + resolvedVariableName + "+" + String.valueOf(getTotalWaitTime()));
		}
	}

	private double getTotalWaitTime() {
		TreeNode rootTreeNode = programAPI.getProgramModel().getRootTreeNode(this);
		final double[] waitTime = {0};

		rootTreeNode.traverse(new ProgramNodeVisitor() {
			@Override
			public void visit(WaitNode programNode, int index, int depth) {
				WaitNodeConfig config = programNode.getConfig();
				if (WaitNodeConfig.ConfigType.TIME.equals(config.getConfigType())) {
					waitTime[0] += ((TimeWaitNodeConfig) config).getTime().getAs(Time.Unit.S);
				}
			}
		});
		return waitTime[0];
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
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(SELECTED_VAR, variable);
			}
		});
	}

	public void removeVariable() {
		undoRedoManager.recordChanges(new UndoableChanges() {
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
