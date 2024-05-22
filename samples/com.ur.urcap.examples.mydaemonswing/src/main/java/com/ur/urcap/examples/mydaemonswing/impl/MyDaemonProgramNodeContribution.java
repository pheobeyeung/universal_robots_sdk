package com.ur.urcap.examples.mydaemonswing.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import java.awt.EventQueue;

public class MyDaemonProgramNodeContribution implements ProgramNodeContribution {
	private static final String NAME = "name";

	private final ProgramAPIProvider apiProvider;
	private final MyDaemonProgramNodeView view;
	private final DataModel model;
	private final XmlRpcMyDaemonInterface daemonStatusMonitor;
	private final KeyboardInputFactory keyboardInputFactory;

	public MyDaemonProgramNodeContribution(ProgramAPIProvider apiProvider,
										   MyDaemonProgramNodeView view,
										   DataModel model) {
		keyboardInputFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.daemonStatusMonitor = getInstallation().getDaemonStatusMonitor();

	}

	@Override
	public void openView() {
		view.setNameText(getName());
		daemonStatusMonitor.startMonitorThread();

		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		updateUI();
	}

	private void updateUI() {
		String title;
		String message;
		try {
			// Provide a real-time preview of the daemon state
			if (daemonStatusMonitor.isDaemonReachable()) {
				title = daemonStatusMonitor.getTitle();
				message = daemonStatusMonitor.getMessage(getName());
			} else {
				title = message = "<Daemon disconnected>";
			}
		} catch (Exception e) {
			System.err.println("Could not retrieve essential data from the daemon process for the preview.");
			title = message = "<Daemon disconnected>";
		}
		setTitleAndMessage(title, message);
	}

	private void setTitleAndMessage(final String title, final String message) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MyDaemonProgramNodeContribution.this.updatePreview(title, message);
			}
		});
	}

	private void updatePreview(String title, String message) {
		view.setTitlePreview(title);
		view.setMessagePreview(message);
		updateUI();
	}

	@Override
	public void closeView() {
		daemonStatusMonitor.stopMonitorThread();
	}

	@Override
	public String getTitle() {
		return "My Daemon: " + getName();
	}

	@Override
	public boolean isDefined() {
		return daemonStatusMonitor.isDaemonReachable() && !getName().isEmpty() && getInstallation().isPopupTitleSet();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Interact with the daemon process through XML-RPC calls
		// Note, alternatively plain sockets can be used.
		writer.assign("mydaemon_message", getInstallation().getXMLRPCVariable() + ".get_message(\"" + getName() + "\")");
		writer.assign("mydaemon_title", getInstallation().getXMLRPCVariable() + ".get_title()");
		writer.appendLine("popup(mydaemon_message, mydaemon_title, False, False, blocking=True)");
		writer.writeChildren();
	}

	public KeyboardTextInput getInputForTextField() {
		KeyboardTextInput keyboardTextInput = keyboardInputFactory.createStringKeyboardInput();
		keyboardTextInput.setInitialValue(getName());
		return keyboardTextInput;
	}

	public KeyboardInputCallback<String> getCallbackForTextField() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setName(value);
				view.setNameText(value);
			}
		};
	}

	private String getName() {
		return model.get(NAME, "");
	}

	private void setName(String name) {
		if ("".equals(name)) {
			model.remove(NAME);
		} else {
			model.set(NAME, name);
		}
	}

	private MyDaemonInstallationNodeContribution getInstallation() {
		return apiProvider.getProgramAPI().getInstallationNode(MyDaemonInstallationNodeContribution.class);
	}
}
