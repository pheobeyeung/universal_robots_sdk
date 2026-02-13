package com.ur.urcap.examples.scriptfunctionswing.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.InstallationAPI;
import com.ur.urcap.api.domain.URCapInfo;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.function.Function;
import com.ur.urcap.api.domain.function.FunctionException;
import com.ur.urcap.api.domain.function.FunctionModel;
import com.ur.urcap.api.domain.script.ScriptWriter;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class ScriptFunctionInstallationNodeContribution implements InstallationNodeContribution {

	private DataModel model;
	private final ScriptFunctionInstallationNodeView view;
	private final InstallationAPI installationAPI;

	private static final String SYMBOLICNAME = "com.ur.urcap.examples.scriptfunctionswing";
	private static final String SHOWMUL_KEY = "showmultiplicationfunction";
	private static final boolean SHOWMUL_DEFAULT = false;
	private static final String FUNCTIONNAME_ADD = "add_swing";
	private static final String FUNCTIONNAME_MUL = "mul_swing";
	private static final String PAR1 = "par1";
	private static final String PAR2 = "par2";


	public ScriptFunctionInstallationNodeContribution(InstallationAPIProvider apiProvider, ScriptFunctionInstallationNodeView view, DataModel model) {
		this.installationAPI = apiProvider.getInstallationAPI();
		this.model = model;
		this.view = view;

		// always add the "add()" function to the expression editor
		addFunction(FUNCTIONNAME_ADD, PAR1, PAR2);

		// update the "mul()" function according to the data model
		updateShowMulFunction(getShowMul());

	}

	public ItemListener getListenerForShowMul() {
		return new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					setShowMul(true);
				} else {
					setShowMul(false);
				}
			}
		};
	}

	private boolean getShowMul() {
		return model.get(SHOWMUL_KEY, SHOWMUL_DEFAULT);
	}

	private void setShowMul(boolean show) {
		updateShowMulFunction(show);
		model.set(SHOWMUL_KEY, show);
	}

	private void updateShowMulFunction(boolean enable) {
		if (enable) {
			addFunction(FUNCTIONNAME_MUL, PAR1, PAR2);
		} else {
			removeFunction(FUNCTIONNAME_MUL);
		}
	}

	private void addFunction(String name, String... argumentNames) {
		FunctionModel functionModel = installationAPI.getFunctionModel();
		if(functionModel.getFunction(name) == null) {
			try {
				functionModel.addFunction(name, argumentNames);
			} catch (FunctionException e) {
				// See e.getMessage() for explanation
			}
		}
	}

	private void removeFunction(String name) {
		FunctionModel functionModel = installationAPI.getFunctionModel();
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
		view.setMulChecked(getShowMul());
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
