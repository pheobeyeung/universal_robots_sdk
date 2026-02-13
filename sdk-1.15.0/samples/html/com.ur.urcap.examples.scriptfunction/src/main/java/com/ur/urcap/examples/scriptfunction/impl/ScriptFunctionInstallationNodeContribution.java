package com.ur.urcap.examples.scriptfunction.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.URCapInfo;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.function.Function;
import com.ur.urcap.api.domain.function.FunctionException;
import com.ur.urcap.api.domain.function.FunctionModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.component.InputCheckBox;
import com.ur.urcap.api.ui.component.InputEvent;


public class ScriptFunctionInstallationNodeContribution implements InstallationNodeContribution {

	private DataModel model;
	private URCapAPI api;

	private static final String SYMBOLICNAME = "com.ur.urcap.examples.scriptfunction";
	private static final String SHOWMUL_KEY = "showmultiplicationfunction";
	private static final boolean SHOWMUL_DEFAULT = false;
	private static final String FUNCTIONNAME_ADD = "add";
	private static final String FUNCTIONNAME_MUL = "mul";


	public ScriptFunctionInstallationNodeContribution(URCapAPI api, DataModel model) {
		this.api = api;
		this.model = model;

		// always add the "add()" function to the expression editor
		addFunction(FUNCTIONNAME_ADD, "par1", "par2");

		// update the "mul()" function according to the data model
		updateShowMulFunction(model.get(SHOWMUL_KEY, SHOWMUL_DEFAULT));
	}

	@Input(id = "chkShowMulFunction")
	private InputCheckBox chkShowMulFunction;

	@Input(id = "chkShowMulFunction")
	public void onClickShowAdvancedFunction(InputEvent event) {
		if (event.getEventType() == InputEvent.EventType.ON_CHANGE) {
			boolean showMulFunction = chkShowMulFunction.isSelected();
			updateShowMulFunction(showMulFunction);
			model.set(SHOWMUL_KEY, showMulFunction);
		}
	}

	private void updateShowMulFunction(boolean enable) {
		if (enable) {
			addFunction(FUNCTIONNAME_MUL, "par1", "par2");
		} else {
			removeFunction(FUNCTIONNAME_MUL);
		}
	}

	private void addFunction(String name, String... argumentNames) {
		FunctionModel functionModel = api.getFunctionModel();
		if(functionModel.getFunction(name) == null) {
			try {
				functionModel.addFunction(name, argumentNames);
			} catch (FunctionException e) {
				// See e.getMessage() for explanation
			}
		}
	}

	private void removeFunction(String name) {
		FunctionModel functionModel = api.getFunctionModel();
		Function f = functionModel.getFunction(name);
		if(f != null) {
			URCapInfo info = f.getProvidingURCapInfo();
			if (info.getSymbolicName().equals(SYMBOLICNAME)) {
				functionModel.removeFunction(f);
			}
		}
	}

	@Override
	public void openView() {
		chkShowMulFunction.setSelected(model.get(SHOWMUL_KEY, SHOWMUL_DEFAULT));
	}

	@Override
	public void closeView() { }

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.appendLine("def " + FUNCTIONNAME_ADD + "(p1, p2):");
		writer.appendLine("return p1 + p2");
		writer.appendLine("end");

		writer.appendLine("def " + FUNCTIONNAME_MUL + "(p1, p2):");
		writer.appendLine("return p1 * p2");
		writer.appendLine("end");
	}
}
