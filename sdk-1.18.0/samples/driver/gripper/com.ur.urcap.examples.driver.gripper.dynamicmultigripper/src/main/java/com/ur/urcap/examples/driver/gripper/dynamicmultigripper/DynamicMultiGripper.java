package com.ur.urcap.examples.driver.gripper.dynamicmultigripper;

import com.ur.urcap.api.contribution.driver.general.tcp.TCPConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.CustomUserInputConfiguration;
import com.ur.urcap.api.contribution.driver.general.userinput.ValueChangedListener;
import com.ur.urcap.api.contribution.driver.general.userinput.selectableinput.ElementResolver;
import com.ur.urcap.api.contribution.driver.general.userinput.selectableinput.SelectableUserInput;

import com.ur.urcap.api.contribution.driver.gripper.ContributionConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.GripperAPIProvider;
import com.ur.urcap.api.contribution.driver.gripper.GripperConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.GripperContribution;
import com.ur.urcap.api.contribution.driver.gripper.ReleaseActionParameters;
import com.ur.urcap.api.contribution.driver.gripper.SystemConfiguration;
import com.ur.urcap.api.contribution.driver.gripper.capability.GripperCapabilities;
import com.ur.urcap.api.contribution.driver.gripper.capability.MultiGripperCapability;
import com.ur.urcap.api.contribution.driver.gripper.capability.WidthCapability;
import com.ur.urcap.api.contribution.driver.gripper.capability.multigripper.GripperList;
import com.ur.urcap.api.contribution.driver.gripper.capability.multigripper.GripperListBuilder;
import com.ur.urcap.api.contribution.driver.gripper.capability.multigripper.GripperListProvider;

import com.ur.urcap.api.domain.program.nodes.contributable.device.gripper.configuration.SelectableGripper;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.value.Pose;
import com.ur.urcap.api.domain.value.PoseFactory;
import com.ur.urcap.api.domain.value.simple.Angle;
import com.ur.urcap.api.domain.value.simple.Length;

import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class DynamicMultiGripper implements GripperContribution {

	private static final String GRIPPER_TITLE = "Dynamic Multi-Gripper";

	private static final String GRIPPER_1_NAME = "Gripper 1";
	private static final String GRIPPER_2_NAME = "Gripper 2";

	private static final String GRIPPER_1_TCP_NAME = "Gripper_1";
	private static final String GRIPPER_2_TCP_NAME = "Gripper_2";
	// Ids must remain constant over time and versions of the Gripper URCap, since they are used for persistence and
	// can be used by other URCaps for configuring Gripper program nodes.
	private static final String GRIPPER_1_ID = "Gripper1_id";
	private static final String GRIPPER_2_ID = "Gripper2_id";

	private static final String MOUNTING_INPUT_ID = "mounting_id";
	private static final String MOUNTING_INPUT_LABEL = "Gripper Mounting";
	private static final String GRIPPER_1_FINGER_TYPE_ID = "gripper1_finger_type_id";
	private static final String GRIPPER_2_FINGER_TYPE_ID = "gripper2_finger_type_id";

	private SelectableGripper gripper1;
	private SelectableGripper gripper2;
	private MultiGripperCapability multiGripperCapability;

	private Pose gripper1TCPPose;
	private Pose gripper2TCPPose;
	private Pose singleGripperTCPPose;

	private SelectableUserInput<MountingType> mountingSelector;
	private SelectableUserInput<FingerType> gripper1FingerType;
	private SelectableUserInput<FingerType> gripper2FingerType;

	private enum MountingType {
		SINGLE("single_id", "Single"),
		DUAL("dual_id", "Dual");

		private final String id;
		private final String displayName;

		MountingType(String id, String displayName) {
			this.id = id;
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String getId() {
			return id;
		}
	}

	private enum FingerType {
		STANDARD("standard_id", "Standard [0 - 60mm]", 0, 60, 10, 30),
		EXTENDED("extended_id", "Extended [40 - 200mm]", 40, 200, 50, 70);
		private final String id;
		private final String displayName;

		private final double minWidth;
		private final double maxWidth;
		private final double defaultGripWidth;
		private final double defaultReleaseWidth;

		FingerType(String id, String displayName, double minWidth, double maxWidth, double defaultGripWidth, double defaultReleaseWidth) {
			this.id = id;
			this.displayName = displayName;
			this.minWidth = minWidth;
			this.maxWidth = maxWidth;
			this.defaultGripWidth = defaultGripWidth;
			this.defaultReleaseWidth = defaultReleaseWidth;
		}

		public String getId() {
			return id;
		}

		public double getMinWidth() {
			return minWidth;
		}

		public double getMaxWidth() {
			return maxWidth;
		}

		public double getDefaultGripWidth() {
			return defaultGripWidth;
		}

		public double getDefaultReleaseWidth() {
			return defaultReleaseWidth;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	@Override
	public String getTitle(Locale locale) {
		return GRIPPER_TITLE;
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setLogo(new ImageIcon(getClass().getResource("/logo/logo.png")));
	}

	@Override
	public void configureGripper(GripperConfiguration gripperConfiguration, GripperAPIProvider gripperAPIProvider) {
		GripperCapabilities capabilities = gripperConfiguration.getGripperCapabilities();

		capabilities.registerWidthCapability(
				FingerType.STANDARD.getMinWidth(),
				FingerType.STANDARD.getMaxWidth(),
				FingerType.STANDARD.getDefaultGripWidth(),
				FingerType.STANDARD.getDefaultReleaseWidth(),
				Length.Unit.MM);

		multiGripperCapability = capabilities.registerMultiGripperCapability(new GripperListProvider() {
			@Override
			public GripperList getGripperList(GripperListBuilder gripperListBuilder, Locale locale) {
				gripper1 = gripperListBuilder.createGripper(GRIPPER_1_ID, GRIPPER_1_NAME, true);
				gripper2 = gripperListBuilder.createGripper(GRIPPER_2_ID, GRIPPER_2_NAME, false);

				return gripperListBuilder.buildList();
			}
		});
	}

	@Override
	public void configureInstallation(CustomUserInputConfiguration configurationUIBuilder,
									  final SystemConfiguration systemConfiguration,
									  TCPConfiguration tcpConfiguration,
									  GripperAPIProvider gripperAPIProvider) {
		configureGripperTCPs(systemConfiguration, gripperAPIProvider);
		customizeInstallationScreen(configurationUIBuilder, systemConfiguration);
	}

	private void customizeInstallationScreen(CustomUserInputConfiguration configurationUIBuilder,
	                                         final SystemConfiguration systemConfiguration) {
		mountingSelector = configurationUIBuilder.registerPreselectedComboBoxInput(
				MOUNTING_INPUT_ID,
				MOUNTING_INPUT_LABEL,
				MountingType.SINGLE,
				Arrays.asList(MountingType.values()), new ElementResolver<MountingType>() {
					@Override
					public String getId(MountingType mountingType) {
						return mountingType.getId();
					}

					@Override
					public String getDisplayName(MountingType mountingType) {
						return mountingType.getDisplayName();
					}
				});
		mountingSelector.setValueChangedListener(new ValueChangedListener<MountingType>() {
			@Override
			public void onValueChanged(MountingType mountingType) {
				TCPConfiguration gripper1TCP = systemConfiguration.getTCPConfiguration(gripper1);

				if (mountingType == MountingType.SINGLE) {
					multiGripperCapability.setEnabled(gripper2, false);
					gripper1TCP.updateTCP(singleGripperTCPPose);
				} else if (mountingType == MountingType.DUAL) {
					gripper1TCP.updateTCP(gripper1TCPPose);
					multiGripperCapability.setEnabled(gripper2, true);
				}
			}
		});

		configurationUIBuilder.addFiller();
		configurationUIBuilder.addFiller();
		configurationUIBuilder.addFiller();

		List<FingerType> fingerTypes = Arrays.asList(FingerType.values());

		gripper1FingerType = configurationUIBuilder.registerPreselectedComboBoxInput(
				GRIPPER_1_FINGER_TYPE_ID,
				GRIPPER_1_NAME,
				FingerType.STANDARD,
				fingerTypes,
				new ElementResolver<FingerType>() {
						@Override
						public String getId(FingerType fingerType) {
							return fingerType.getId();
						}

						@Override
						public String getDisplayName(FingerType fingerType) {
							return fingerType.getDisplayName();
						}
					});
		gripper1FingerType.setValueChangedListener(new ValueChangedListener<FingerType>() {
			@Override
			public void onValueChanged(FingerType fingerType) {
				updateWidthCapability(gripper1, fingerType);
			}
		});

		gripper2FingerType = configurationUIBuilder.registerPreselectedComboBoxInput(
				GRIPPER_2_FINGER_TYPE_ID,
				GRIPPER_2_NAME,
				FingerType.STANDARD,
				fingerTypes,
				new ElementResolver<FingerType>() {
					@Override
					public String getId(FingerType fingerType) {
						return fingerType.getId();
					}

					@Override
					public String getDisplayName(FingerType fingerType) {
						return fingerType.getDisplayName();
					}
				});
		gripper2FingerType.setValueChangedListener(new ValueChangedListener<FingerType>() {
			@Override
			public void onValueChanged(FingerType fingerType) {
				updateWidthCapability(gripper2, fingerType);
			}
		});
	}

	// This method updates the parameters of the registered width capability for a specific individual gripper
	private void updateWidthCapability(SelectableGripper gripper, FingerType fingerType) {
		WidthCapability widthCapability = multiGripperCapability.getRegisteredCapabilities(gripper).getWidthCapability();

		widthCapability.updateCapability(
				fingerType.getMinWidth(),
				fingerType.getMaxWidth(),
				fingerType.getDefaultGripWidth(),
				fingerType.getDefaultReleaseWidth(),
				Length.Unit.MM);
	}

	private void configureGripperTCPs(SystemConfiguration systemConfiguration, GripperAPIProvider gripperAPIProvider) {
		createMountingTCPPoses(gripperAPIProvider);

		TCPConfiguration gripper1TcpConfiguration = systemConfiguration.getTCPConfiguration(gripper1);
		gripper1TcpConfiguration.setTCP(GRIPPER_1_TCP_NAME, singleGripperTCPPose);

		TCPConfiguration gripper2TcpConfiguration = systemConfiguration.getTCPConfiguration(gripper2);
		gripper2TcpConfiguration.setTCP(GRIPPER_2_TCP_NAME, gripper2TCPPose);
	}

	private void createMountingTCPPoses(GripperAPIProvider gripperAPIProvider) {
		PoseFactory poseFactory = gripperAPIProvider.getPoseFactory();

		singleGripperTCPPose = poseFactory.createPose(0, 0, 100, 0, 0, 0, Length.Unit.MM, Angle.Unit.RAD);
		gripper1TCPPose = poseFactory.createPose(50, 0, 80, 0, 0.61, 0, Length.Unit.MM, Angle.Unit.RAD);
		gripper2TCPPose = poseFactory.createPose(-50, 0, 80, 0, -0.61, 0, Length.Unit.MM, Angle.Unit.RAD);
	}

	@Override
	public void generatePreambleScript(ScriptWriter scriptWriter) {
		// Intentionally left empty
	}

	@Override
	public void generateGripActionScript(ScriptWriter scriptWriter, GripActionParameters gripActionParameters) {
		System.out.println("Grip Action :");

		// The mounting type could be used during script generation
		printMountingType();

		SelectableGripper selectedGripper = gripActionParameters.getGripperSelection();
		printSelectedGripper(selectedGripper);

		if (gripper1.equals(selectedGripper)) {
			// The selected finger type could be used during script generation
			printFingerType(selectedGripper);
			scriptWriter.appendLine("set_tool_digital_out(0, True)");
		} else if (gripper2.equals(selectedGripper)) {
			// The selected finger type could be used during script generation
			printFingerType(selectedGripper);
			scriptWriter.appendLine("set_tool_digital_out(1, True)");
		}
	}

	@Override
	public void generateReleaseActionScript(ScriptWriter scriptWriter, ReleaseActionParameters releaseActionParameters) {
		System.out.println("Release Action :");

		// The mounting type could be used during script generation
		printMountingType();

		SelectableGripper selectedGripper = releaseActionParameters.getGripperSelection();
		printSelectedGripper(selectedGripper);

		if (gripper1.equals(selectedGripper)) {
			// The selected finger type could be used during script generation
			printFingerType(selectedGripper);
			scriptWriter.appendLine("set_tool_digital_out(0, False)");
		} else if (gripper2.equals(selectedGripper)) {
			// The selected finger type could be used during script generation
			printFingerType(selectedGripper);
			scriptWriter.appendLine("set_tool_digital_out(1, False)");
		}
	}

	private void printSelectedGripper(SelectableGripper selectedGripper) {
		System.out.println("Selected Gripper: " + selectedGripper.getDisplayName());
	}
	private void printMountingType() {
		System.out.println("Mounting Type: " + mountingSelector.getValue().getDisplayName());
	}

	private void printFingerType(SelectableGripper selectedGripper) {
		if (gripper1.equals(selectedGripper)) {
			System.out.println("Finger Type: " + gripper1FingerType.getValue().getDisplayName() + "\n");
		} else if (gripper2.equals(selectedGripper)) {
			System.out.println("Finger Type: " + gripper2FingerType.getValue().getDisplayName() + "\n");
		}
	}
}