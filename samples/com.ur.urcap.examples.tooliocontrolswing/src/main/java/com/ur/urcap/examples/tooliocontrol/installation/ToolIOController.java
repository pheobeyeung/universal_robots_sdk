package com.ur.urcap.examples.tooliocontrol.installation;

import com.ur.urcap.api.domain.resource.ResourceModel;
import com.ur.urcap.api.domain.resource.tooliointerface.AnalogInputDomainConfig;
import com.ur.urcap.api.domain.resource.tooliointerface.AnalogInputDomainConfig.AnalogDomain;
import com.ur.urcap.api.domain.resource.tooliointerface.AnalogInputModeConfig;
import com.ur.urcap.api.domain.resource.tooliointerface.CommunicationInterfaceConfig;
import com.ur.urcap.api.domain.resource.tooliointerface.CommunicationInterfaceConfig.BaudRate;
import com.ur.urcap.api.domain.resource.tooliointerface.CommunicationInterfaceConfig.Parity;
import com.ur.urcap.api.domain.resource.tooliointerface.CommunicationInterfaceConfig.StopBits;
import com.ur.urcap.api.domain.resource.tooliointerface.DigitalOutputModeConfig;
import com.ur.urcap.api.domain.resource.tooliointerface.StandardDigitalOutputModeConfig;
import com.ur.urcap.api.domain.resource.tooliointerface.StandardDigitalOutputModeConfig.OutputMode;
import com.ur.urcap.api.domain.resource.tooliointerface.ToolIOInterface;
import com.ur.urcap.api.domain.resource.tooliointerface.ToolIOInterface.OutputVoltage;
import com.ur.urcap.api.domain.resource.tooliointerface.control.AnalogInputModeConfigFactory;
import com.ur.urcap.api.domain.resource.tooliointerface.control.DigitalOutputModeConfigFactory;
import com.ur.urcap.api.domain.resource.tooliointerface.control.ToolIOInterfaceControlEvent;
import com.ur.urcap.api.domain.resource.tooliointerface.control.ToolIOInterfaceControllable;
import com.ur.urcap.api.domain.resource.tooliointerface.control.ToolIOInterfaceController;
import com.ur.urcap.api.domain.system.capability.CapabilityManager;

import static com.ur.urcap.api.domain.resource.tooliointerface.AnalogInputModeConfig.ConfigType.ANALOG_INPUT_DOMAIN;
import static com.ur.urcap.api.domain.resource.tooliointerface.AnalogInputModeConfig.ConfigType.TOOL_COMMUNICATION_INTERFACE;
import static com.ur.urcap.api.domain.resource.tooliointerface.DigitalOutputModeConfig.ConfigType.STANDARD_DIGITAL_OUTPUT_MODE;
import static com.ur.urcap.api.domain.system.capability.tooliointerface.ToolIOCapability.COMMUNICATION_INTERFACE_MODE;
import static com.ur.urcap.api.domain.system.capability.tooliointerface.ToolIOCapability.DIGITAL_OUTPUT_MODE;

class ToolIOController implements ToolIOInterfaceController {

	private static final OutputVoltage REQUIRED_VOLTAGE = OutputVoltage.OUTPUT_VOLTAGE_24V;
	private static final OutputVoltage SHUTDOWN_VOLTAGE = OutputVoltage.OUTPUT_VOLTAGE_0V;
	private static final BaudRate REQUIRED_BAUDRATE = BaudRate.BAUD_2M;
	private static final BaudRate SHUTDOWN_BAUDRATE = BaudRate.BAUD_115200;
	private static final Parity REQUIRED_PARITY = Parity.EVEN;
	private static final Parity SHUTDOWN_PARITY = Parity.NONE;
	private static final StopBits REQUIRED_STOPBITS = StopBits.TWO;
	private static final StopBits SHUTDOWN_STOPBITS = StopBits.ONE;
	private static final double REQUIRED_RXIDLE = 5.0;
	private static final double SHUTDOWN_RXIDLE = 1.5;
	private static final double REQUIRED_TXIDLE = 5.5;
	private static final double SHUTDOWN_TXIDLE = 3.5;
	private static final OutputMode REQUIRED_DIGITAL_OUTPUT_0 = OutputMode.SOURCING_PNP;
	private static final OutputMode SHUTDOWN_DIGITAL_OUTPUT_0 = OutputMode.SINKING_NPN;
	private static final OutputMode REQUIRED_DIGITAL_OUTPUT_1 = OutputMode.PUSH_PULL;
	private static final OutputMode SHUTDOWN_DIGITAL_OUTPUT_1 = OutputMode.SINKING_NPN;
	private static final AnalogDomain REQUIRED_ANALOGDOMAIN = AnalogDomain.CURRENT;
	private static final AnalogDomain SHUTDOWN_ANALOGDOMAIN = AnalogDomain.VOLTAGE;

	private static final float ACCEPTED_TOLERANCE = 0.01f;

	private final ToolIOInterface toolIOInterface;
	private final CapabilityManager capabilityManager;
	private ToolIOInterfaceControllable controllableInstance;

	public ToolIOController(ResourceModel resourceModel, CapabilityManager capabilityManager) {
		toolIOInterface = resourceModel.getToolIOInterface();
		this.capabilityManager = capabilityManager;
	}

	@Override
	public void onControlGranted(ToolIOInterfaceControlEvent event) {
		controllableInstance = event.getControllableResource();
		powerOnTool();
	}

	@Override
	public void onControlToBeRevoked(ToolIOInterfaceControlEvent event) {
		shutDownTool();
	}

	public boolean hasControl() {
		return controllableInstance != null && controllableInstance.hasControl();
	}

	private void powerOnTool() {
		controllableInstance.setOutputVoltage(REQUIRED_VOLTAGE);

		if (capabilityManager.hasCapability(COMMUNICATION_INTERFACE_MODE)) {
			controlTCI(REQUIRED_BAUDRATE, REQUIRED_PARITY, REQUIRED_STOPBITS, REQUIRED_RXIDLE, REQUIRED_TXIDLE);
		} else {
			controlAnalogInputDomain(REQUIRED_ANALOGDOMAIN);
		}

		if (capabilityManager.hasCapability(DIGITAL_OUTPUT_MODE)) {
			controlToolOutput(REQUIRED_DIGITAL_OUTPUT_0, REQUIRED_DIGITAL_OUTPUT_1);
		}
	}

	private void shutDownTool() {
		controllableInstance.setOutputVoltage(SHUTDOWN_VOLTAGE);

		if (capabilityManager.hasCapability(COMMUNICATION_INTERFACE_MODE)) {
			controlTCI(SHUTDOWN_BAUDRATE, SHUTDOWN_PARITY, SHUTDOWN_STOPBITS, SHUTDOWN_RXIDLE, SHUTDOWN_TXIDLE);
		} else {
			controlAnalogInputDomain(SHUTDOWN_ANALOGDOMAIN);
		}

		if (capabilityManager.hasCapability(DIGITAL_OUTPUT_MODE)) {
			controlToolOutput(SHUTDOWN_DIGITAL_OUTPUT_0, SHUTDOWN_DIGITAL_OUTPUT_1);
		}
	}

	private void controlAnalogInputDomain(AnalogDomain analogdomain) {
		AnalogInputModeConfigFactory factory = controllableInstance.getAnalogInputModeConfigFactory();
		AnalogInputDomainConfig config = factory.createAnalogInputDomainConfig(analogdomain, analogdomain);
		controllableInstance.setAnalogInputModeConfig(config);
	}

	private void controlToolOutput(OutputMode digitalOutput0, OutputMode digitalOutput1) {
		DigitalOutputModeConfigFactory factory = controllableInstance.getDigitalOutputModeConfigFactory();
		StandardDigitalOutputModeConfig config = factory.createStandardDigitalOutputModeConfig(digitalOutput0, digitalOutput1);
		controllableInstance.setDigitalOutputModeConfig(config);
	}

	private void controlTCI(BaudRate baudrate, Parity parity, StopBits stopbits, double rxidle, double txidle) {
		AnalogInputModeConfigFactory factory = controllableInstance.getAnalogInputModeConfigFactory();
		CommunicationInterfaceConfig config = factory.createCommunicationInterfaceConfig(baudrate, parity, stopbits, rxidle, txidle);
		controllableInstance.setAnalogInputModeConfig(config);
	}

	public boolean isSetupCorrect() {
		if (toolIOInterface.getOutputVoltage() != REQUIRED_VOLTAGE ) {
			return false;
		}

		AnalogInputModeConfig config = toolIOInterface.getAnalogInputModeConfig();
		if(capabilityManager.hasCapability(COMMUNICATION_INTERFACE_MODE)) {
			if(!isRequiredTCI(config)) {
				return false;
			}
		} else {
		   if(!isRequiredAnalogInput(config)){
		    	return false;
		   }
		}

		if (capabilityManager.hasCapability(DIGITAL_OUTPUT_MODE)) {
			return isRequiredToolOutput();
		}

		return true;
	}

	private Boolean isRequiredToolOutput() {
		DigitalOutputModeConfig config = toolIOInterface.getDigitalOutputModeConfig();
		if (config.getConfigType() != STANDARD_DIGITAL_OUTPUT_MODE) {
			return false;
		}

		StandardDigitalOutputModeConfig digitalOutputModeConfig = ((StandardDigitalOutputModeConfig) config);
		return digitalOutputModeConfig.getOutputModeForOutput0() == REQUIRED_DIGITAL_OUTPUT_0
				&& digitalOutputModeConfig.getOutputModeForOutput1() == REQUIRED_DIGITAL_OUTPUT_1;
	}

	private boolean isRequiredAnalogInput(AnalogInputModeConfig toolInputModeConfig) {
		if(toolInputModeConfig.getConfigType() != ANALOG_INPUT_DOMAIN) {
			return false;
		}

		AnalogInputDomainConfig config = ((AnalogInputDomainConfig) toolInputModeConfig);
		return config.getAnalogDomainForInput0() == REQUIRED_ANALOGDOMAIN
				&& config.getAnalogDomainForInput1() == REQUIRED_ANALOGDOMAIN;
	}

	private boolean isRequiredTCI(AnalogInputModeConfig toolInputModeConfig) {
		if(toolInputModeConfig.getConfigType() != TOOL_COMMUNICATION_INTERFACE) {
			return false;
		}

		CommunicationInterfaceConfig toolCommunicationInterfaceConfig = ((CommunicationInterfaceConfig) toolInputModeConfig);
		return toolCommunicationInterfaceConfig.getBaudRate() == REQUIRED_BAUDRATE
				&& toolCommunicationInterfaceConfig.getParity() == REQUIRED_PARITY
				&& toolCommunicationInterfaceConfig.getStopBits() == REQUIRED_STOPBITS
				&& Math.abs(toolCommunicationInterfaceConfig.getRxIdleChars() - REQUIRED_RXIDLE) < ACCEPTED_TOLERANCE
				&& Math.abs(toolCommunicationInterfaceConfig.getTxIdleChars() - REQUIRED_TXIDLE) < ACCEPTED_TOLERANCE;
	}
}
