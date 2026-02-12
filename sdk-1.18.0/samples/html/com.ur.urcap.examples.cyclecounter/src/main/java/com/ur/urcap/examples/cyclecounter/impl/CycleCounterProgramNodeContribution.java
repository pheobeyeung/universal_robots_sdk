package com.ur.urcap.examples.cyclecounter.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.nodes.builtin.InvalidDomainException;
import com.ur.urcap.api.domain.program.nodes.builtin.WaitNode;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.waitnode.TimeWaitNodeConfig;
import com.ur.urcap.api.domain.program.structure.TreeStructureException;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.util.Filter;
import com.ur.urcap.api.domain.validation.ErrorHandler;
import com.ur.urcap.api.domain.value.expression.InvalidExpressionException;
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

public class CycleCounterProgramNodeContribution implements ProgramNodeContribution {
	public static final String SELECTED_VAR = "selectedVar";
	private final ProgramAPI programAPI;
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

	public CycleCounterProgramNodeContribution(URCapAPI api, DataModel model) {
		this.programAPI = api.getProgramAPIProvider().getProgramAPI();
		this.model = model;
		variableFactory = programAPI.getVariableModel().getVariableFactory();
		errorIcon = getErrorImage();

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
		inputVariableName.setText("");
		updateComboBox();
		clearErrors();
	}

	@Override
	public void closeView() {
		// No action needed when closing view
	}

	@Input(id = "btnCreateNew")
	public void clickCreateNew(InputEvent event) {
		if (event.getEventType() == ON_PRESSED) {
			try {
				clearErrors();
				//Create a global variable with an initial value and store it in the data model to make it available to all program nodes.
				GlobalVariable variable = variableFactory.createGlobalVariable(inputVariableName.getText(),
						programAPI.getValueFactoryProvider().createExpressionBuilder().append("0").build());
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
		items.addAll(programAPI.getVariableModel().get(new Filter<Variable>() {
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
		items.add(0, "Select counting variable");

		comboVariables.setItems(items);

		Variable selectedVar = getSelectedVariable();
		if (selectedVar != null) {
			comboVariables.selectItem(selectedVar);
		}
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

	private Variable getSelectedVariable() {
		return model.get(SELECTED_VAR, (Variable) null);
	}

	private BufferedImage getErrorImage() {
		BufferedImage image;
		try {
			image = ImageIO.read(getClass().getResource("/com/ur/urcap/examples/cyclecounter/impl/warning-bigger.png"));
		} catch (IOException e) {
			// Should not happen.
			throw new RuntimeException("Unexpected exception while loading icon.", e);
		}
		return image;
	}

}
