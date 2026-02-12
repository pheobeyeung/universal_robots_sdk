package com.ur.urcap.examples.idletime.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.URCapAPI;
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
import com.ur.urcap.api.domain.util.Filter;
import com.ur.urcap.api.domain.validation.ErrorHandler;
import com.ur.urcap.api.domain.value.expression.InvalidExpressionException;
import com.ur.urcap.api.domain.value.simple.SimpleValueFactory;
import com.ur.urcap.api.domain.value.simple.Time;
import com.ur.urcap.api.domain.variable.GlobalVariable;
import com.ur.urcap.api.domain.variable.Variable;
import com.ur.urcap.api.domain.variable.VariableException;
import com.ur.urcap.api.domain.variable.VariableFactory;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.annotation.Select;
import com.ur.urcap.api.ui.component.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.ur.urcap.api.ui.component.InputEvent.EventType.ON_PRESSED;
import static com.ur.urcap.api.ui.component.SelectEvent.EventType.ON_SELECT;

public class IdleTimeProgramNodeContribution implements ProgramNodeContribution {
	public static final String SELECTED_VAR = "selectedVar";
	private final URCapAPI api;
	private final DataModel model;
	private final VariableFactory variableFactory;

	@Input(id = "btnCreateNew")
	private InputButton btnCreateNew;

	@Input(id = "inputVariableName")
	private InputTextField inputVariableName;

	@Select(id = "comboVariables")
	private SelectDropDownList comboVariables;

	@Label(id = "errorLabel")
	public LabelComponent errorLabel;
	private final BufferedImage errorIcon;

	public IdleTimeProgramNodeContribution(URCapAPI api, DataModel model) {
		this.api = api;
		this.model = model;
		variableFactory = api.getVariableModel().getVariableFactory();
		this.errorIcon = getErrorImage();
		createSubTreeTemplate(api);
	}

	private void createSubTreeTemplate(URCapAPI api) {
		ProgramNodeFactory programNodeFactory = api.getProgramModel().getProgramNodeFactory();
		WaitNode waitNode = programNodeFactory.createWaitNode();
		SimpleValueFactory valueFactory = api.getValueFactoryProvider().getSimpleValueFactory();
		WaitNodeConfigFactory configFactory = waitNode.getConfigFactory();

		TimeWaitNodeConfig configA = configFactory.createTimeConfig(valueFactory.createTime(1, Time.Unit.S), ErrorHandler.AUTO_CORRECT);
		TimeWaitNodeConfig configB = configFactory.createTimeConfig(valueFactory.createTime(2.5, Time.Unit.S), ErrorHandler.AUTO_CORRECT);

		TreeNode root = api.getProgramModel().getRootTreeNode(this);

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
		inputVariableName.setText("");
		updateComboBox();
		clearErrors();
	}

	@Override
	public void closeView() {

	}

	@Input(id = "btnCreateNew")
	public void clickCreateNew(InputEvent event) {
		if (event.getEventType() == ON_PRESSED) {
			try {
				clearErrors();
				//Create a global variable with an initial value and store it in the data model to make it available to all program nodes.
				GlobalVariable variable = variableFactory.createGlobalVariable(inputVariableName.getText(),
						api.getValueFactoryProvider().createExpressionBuilder().append("0").build());
				model.set(SELECTED_VAR, variable);
			} catch (VariableException e) {
				setError(e.getLocalizedMessage());
				e.printStackTrace();
			} catch (InvalidExpressionException e) {
				setError(e.getLocalizedMessage());
				e.printStackTrace();
			}
			updateComboBox();
		}
	}

	@Select(id = "comboVariables")
	private void selectComboVariables(SelectEvent event) {
		if (event.getEvent() == ON_SELECT) {
			Object selectedItem = comboVariables.getSelectedItem();
			if (selectedItem instanceof Variable) {
				model.set(SELECTED_VAR, (Variable) selectedItem);
			} else {
				model.remove(SELECTED_VAR);
			}
		}
	}

	private void updateComboBox() {
		ArrayList<Object> items = new ArrayList<Object>();
		items.addAll(api.getVariableModel().get(new Filter<Variable>() {
			@Override
			public boolean accept(Variable element) {
				return element.getType().equals(Variable.Type.GLOBAL) || element.getType().equals(Variable.Type.VALUE_PERSISTED);
			}
		}));

		Collections.sort(items, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				if (o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) == 0) {
					//Sort lowercase/uppercase consistently
					return o1.toString().compareTo(o2.toString());
				} else {
					return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
				}
			}
		});

		//Insert at top after sorting
		items.add(0, "<Variable>");

		comboVariables.setItems(items);

		Variable selectedVar = getSelectedVariable();
		if (selectedVar != null) {
			comboVariables.selectItem(selectedVar);
		}
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

	private void clearErrors() {
		if (errorLabel != null) {
			errorLabel.setVisible(false);
		}
	}

	private void setError(final String message) {
		if (errorLabel != null) {
			errorLabel.setText("<html>Error: Could not create variable<br>" + message + "</html>");
			errorLabel.setImage(errorIcon);
			errorLabel.setVisible(true);
		}
	}

	private double getTotalWaitTime() {
		TreeNode rootTreeNode = api.getProgramModel().getRootTreeNode(this);
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

	private Variable getSelectedVariable() {
		return model.get(SELECTED_VAR, (Variable) null);
	}

	private BufferedImage getErrorImage() {
		BufferedImage image;
		try {
			image = ImageIO.read(getClass().getResource("/com/ur/urcap/examples/idletime/impl/warning-bigger.png"));
		} catch (IOException e) {
			// Should not happen.
			throw new RuntimeException("Unexpected exception while loading icon.", e);
		}
		return image;
	}

}
