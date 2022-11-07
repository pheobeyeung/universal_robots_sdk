package com.ur.urcap.examples.driver.gripper.advancedgripper;

import com.ur.urcap.api.domain.resource.tooliointerface.ToolIOInterface.OutputVoltage;
import com.ur.urcap.api.domain.resource.tooliointerface.control.ToolIOInterfaceControlEvent;
import com.ur.urcap.api.domain.resource.tooliointerface.control.ToolIOInterfaceControllable;
import com.ur.urcap.api.domain.resource.tooliointerface.control.ToolIOInterfaceController;


class ToolIOController implements ToolIOInterfaceController {

	private static final OutputVoltage REQUIRED_VOLTAGE = OutputVoltage.OUTPUT_VOLTAGE_24V;
	private static final OutputVoltage SHUTDOWN_VOLTAGE = OutputVoltage.OUTPUT_VOLTAGE_0V;

	private ToolIOInterfaceControllable controllableInstance;

	@Override
	public void onControlGranted(ToolIOInterfaceControlEvent event) {
		controllableInstance = event.getControllableResource();
		powerOnGripper();
	}

	@Override
	public void onControlToBeRevoked(ToolIOInterfaceControlEvent event) {
		// Shutdown device "gracefully"
		shutDownGripper();
	}

	private void powerOnGripper() {
		controllableInstance.setOutputVoltage(REQUIRED_VOLTAGE);
	}

	private void shutDownGripper() {
		controllableInstance.setOutputVoltage(SHUTDOWN_VOLTAGE);
	}
}
