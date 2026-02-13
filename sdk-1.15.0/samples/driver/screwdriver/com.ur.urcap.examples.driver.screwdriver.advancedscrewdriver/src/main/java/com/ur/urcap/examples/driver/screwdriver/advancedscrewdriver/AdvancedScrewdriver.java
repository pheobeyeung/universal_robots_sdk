package com.ur.urcap.examples.driver.screwdriver.advancedscrewdriver;

import com.ur.urcap.api.contribution.driver.general.script.ScriptCodeGenerator;
import com.ur.urcap.api.contribution.driver.general.tcp.TCPConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.CustomUserInputConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.ContributionConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverAPIProvider;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverContribution;
import com.ur.urcap.api.contribution.driver.screwdriver.ScrewdriverParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.SystemConfiguration;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.DriveScrewNotOKParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.DriveScrewOKParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.FeedScrewParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.PrepareToStartScrewdriverParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.ProgramSelectionParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.ScrewdriverCapabilities;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.ScrewdriverFeedbackCapabilities;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.ScrewdriverReadyParameters;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.screwdriverprogram.ScrewdriverProgram;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.screwdriverprogram.ScrewdriverProgramList;
import com.ur.urcap.api.contribution.driver.screwdriver.capability.screwdriverprogram.ScrewdriverProgramListProvider;
import com.ur.urcap.api.domain.script.ScriptWriter;

import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class AdvancedScrewdriver implements ScrewdriverContribution {

	private static final int SCREWDRIVER_READY_STATUS_DIGITAL_INPUT = 0;
	private static final int FEED_SCREW_STATUS_DIGITAL_INPUT = 1;
	private static final int DRIVE_SCREW_OK_DIGITAL_INPUT = 2;
	private static final int DRIVE_SCREW_NOK_DIGITAL_INPUT = 3;

	private static final int PROGRAM_SELECTION_DIGITAL_OUTPUT = 0;
	private static final int REQUEST_FEED_SCREW_DIGITAL_OUTPUT = 1;
	private static final int DRIVE_SCREW_DIGITAL_OUTPUT = 2;

	// Represents a program for the screwdriver
	private enum Program {
		LOW("Program_1_id", "Program 1"),
		HIGH("Program_2_id", "Program 2");

		private final String id;
		private final String displayName;

		Program(String id, String displayName) {
			this.id = id;
			this.displayName = displayName;
		}

		String getId() {
			return id;
		}

		String getDisplayName() {
			return displayName;
		}
	}


	@Override
	public String getTitle(Locale locale) {
		return "Advanced Screwdriver";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setLogo(new ImageIcon(getClass().getResource("/logo/logo.png")));
	}

	@Override
	public void configureScrewdriver(ScrewdriverConfiguration screwdriverConfiguration, ScrewdriverAPIProvider screwdriverAPIProvider) {
		ScrewdriverCapabilities capabilities = screwdriverConfiguration.getScrewdriverCapabilities();
		capabilities.registerProgramSelectionCapability(createScrewdriverProgramListProvider(),
														createProgramSelectionScriptCodeGenerator());
		capabilities.registerFeedScrewCapability(createFeedScrewScriptCodeGenerator());
		capabilities.registerPrepareToStartScrewdriverCapability(createPrepareToStartScrewdriverScriptCodeGenerator());
		capabilities.registerOperationTypeCapability();

		ScrewdriverFeedbackCapabilities feedbackCapabilities = screwdriverConfiguration.getScrewdriverFeedbackCapabilities();
		feedbackCapabilities.registerScrewdriverReadyCapability(createScrewdriverReadyScriptCodeGenerator());
		feedbackCapabilities.registerDriveScrewOKCapability(createDriveScrewOKScriptCodeGenerator());
		feedbackCapabilities.registerDriveScrewNotOKCapability(createDriveScrewNotOKScriptCodeGenerator());
	}

	private ScrewdriverProgramListProvider createScrewdriverProgramListProvider() {
		return new ScrewdriverProgramListProvider() {
			@Override
			public void populateList(ScrewdriverProgramList screwdriverProgramList) {
				screwdriverProgramList.addAll(retrieveScrewdriverProgramList());
			}
		};
	}

	// This method "simulates" retrieving the current list of screwdriver program (typically from the control box for
	// the screwdriver).
	private List<ScrewdriverProgram> retrieveScrewdriverProgramList() {
		List<ScrewdriverProgram> availableScrewdriverPrograms = Arrays.asList(createScrewdriverProgram(Program.LOW),
																			  createScrewdriverProgram(Program.HIGH));
		return availableScrewdriverPrograms;
	}

	private ScrewdriverProgram createScrewdriverProgram(final Program program) {
		return new ScrewdriverProgram() {
			@Override
			public String getId() {
				return program.getId();
			}

			@Override
			public String getDisplayName() {
				return program.getDisplayName();
			}
		};
	}

	private ScriptCodeGenerator<ProgramSelectionParameters> createProgramSelectionScriptCodeGenerator() {
		return new ScriptCodeGenerator<ProgramSelectionParameters>() {
			@Override
			public void generateScript(ScriptWriter scriptWriter, ProgramSelectionParameters parameters) {
				ScrewdriverProgram screwdriverProgram = parameters.getScrewdriverProgram();
				boolean programSelectionOutputValue = screwdriverProgram.getId().equals(Program.HIGH.getId());

				scriptWriter.appendLine(generateSetterScriptForDigitalOutput(PROGRAM_SELECTION_DIGITAL_OUTPUT, programSelectionOutputValue));
				// Note: In a real application, this line should return a boolean indicating whether or not selecting
				// the program succeeded.
				scriptWriter.appendLine("return True");
			}
		};
	}

	// We will request feed screw, wait 0.5 second and then return a status
	private ScriptCodeGenerator<FeedScrewParameters> createFeedScrewScriptCodeGenerator() {
		return new ScriptCodeGenerator<FeedScrewParameters>() {
			@Override
			public void generateScript(ScriptWriter scriptWriter, FeedScrewParameters parameters) {
				scriptWriter.appendLine(generateSetterScriptForDigitalOutput(REQUEST_FEED_SCREW_DIGITAL_OUTPUT, true));
				// Simulate feeding a screw to the screwdriver
				scriptWriter.sleep(0.5);
				// Check if the feeding the succeeded
				scriptWriter.appendLine("return " + generateGetterScriptForDigitalInput(FEED_SCREW_STATUS_DIGITAL_INPUT) + " == True");
			}
		};
	}

	private String generateSetterScriptForDigitalOutput(int digitalOutputId, boolean value) {
		String valueString = value ? "True" : "False";
		return "set_standard_digital_out(" + digitalOutputId + "," + valueString + ")";
	}

	private String generateGetterScriptForDigitalInput(int digitalInputId) {
		return "get_standard_digital_in(" + digitalInputId + ")";
	}

	private ScriptCodeGenerator<PrepareToStartScrewdriverParameters> createPrepareToStartScrewdriverScriptCodeGenerator() {
		return new ScriptCodeGenerator<PrepareToStartScrewdriverParameters>() {
			@Override
			public void generateScript(ScriptWriter scriptWriter, PrepareToStartScrewdriverParameters prepareToStartScrewdriverParameters) {
				// Simulate preparing the screwdriver
				scriptWriter.sleep(0.5);
				// Note: In a real application, this line should return a boolean indicating whether or not preparing
				// the screwdriver succeeded.
				scriptWriter.appendLine("return True");
			}
		};
	}

	private ScriptCodeGenerator<ScrewdriverReadyParameters> createScrewdriverReadyScriptCodeGenerator() {
		return new ScriptCodeGenerator<ScrewdriverReadyParameters>() {
			@Override
			public void generateScript(ScriptWriter scriptWriter, ScrewdriverReadyParameters screwdriverReadyParameters) {
				scriptWriter.appendLine("return " + generateGetterScriptForDigitalInput(SCREWDRIVER_READY_STATUS_DIGITAL_INPUT) + " == True");
			}
		};
	}

	private ScriptCodeGenerator<DriveScrewOKParameters> createDriveScrewOKScriptCodeGenerator() {
		return new ScriptCodeGenerator<DriveScrewOKParameters>() {
			@Override
			public void generateScript(ScriptWriter scriptWriter, DriveScrewOKParameters driveScrewOKParameters) {
				scriptWriter.appendLine("return " + generateGetterScriptForDigitalInput(DRIVE_SCREW_OK_DIGITAL_INPUT) + " == True");
			}
		};
	}

	private ScriptCodeGenerator<DriveScrewNotOKParameters> createDriveScrewNotOKScriptCodeGenerator() {
		return new ScriptCodeGenerator<DriveScrewNotOKParameters>() {
			@Override
			public void generateScript(ScriptWriter scriptWriter, DriveScrewNotOKParameters driveScrewNotOKParameters) {
				scriptWriter.appendLine("return " + generateGetterScriptForDigitalInput(DRIVE_SCREW_NOK_DIGITAL_INPUT) + " == True");
			}
		};
	}

	@Override
	public void configureInstallation(CustomUserInputConfiguration configurationUIBuilder,
	                                  SystemConfiguration systemConfiguration,
	                                  TCPConfiguration tcpConfiguration,
	                                  ScrewdriverAPIProvider apiProvider) {
		configurationUIBuilder.setDescriptionText("This is an example of a Screwdriver Driver supporting all capabilities");
	}

	@Override
	public void generatePreambleScript(ScriptWriter scriptWriter) {
		// Intentionally left empty
	}

	@Override
	public void generateStartScrewdriverScript(ScriptWriter scriptWriter, ScrewdriverParameters parameters) {
		scriptWriter.appendLine(generateSetterScriptForDigitalOutput(DRIVE_SCREW_DIGITAL_OUTPUT, true));
	}

	@Override
	public void generateStopScrewdriverScript(ScriptWriter scriptWriter, ScrewdriverParameters parameters) {
		scriptWriter.appendLine(generateSetterScriptForDigitalOutput(DRIVE_SCREW_DIGITAL_OUTPUT, false));
	}
}