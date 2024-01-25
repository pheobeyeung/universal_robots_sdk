package com.ur.urcap.examples.createfeature.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.UserInterfaceAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.feature.Feature;
import com.ur.urcap.api.domain.feature.FeatureContributionModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.RobotPositionCallback2;
import com.ur.urcap.api.domain.value.jointposition.JointPositions;
import com.ur.urcap.api.domain.value.robotposition.PositionParameters;

public class CreateFeatureInstallationNodeContribution implements InstallationNodeContribution {
	private static final String FEATURE_KEY = "FeatureKey";
	private static final String FEATURE_JOINT_ANGLES_KEY = "jointAngles";
	private static final String SUGGESTED_FEATURE_NAME = "URCapFeature";
	private final DataModel model;
	private final CreateFeatureInstallationNodeView view;
	private FeatureContributionModel featureContributionModel;
	private UserInterfaceAPI userInterfaceAPI;

	CreateFeatureInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, CreateFeatureInstallationNodeView view) {

		this.model = model;
		this.view = view;
		featureContributionModel = apiProvider.getInstallationAPI().getFeatureContributionModel();
		userInterfaceAPI = apiProvider.getUserInterfaceAPI();
	}

	@Override
	public void openView() {
		view.featureIsCreated(isFeatureCreated());
	}

	@Override
	public void closeView() {
		// Intentionally left empty
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		// Intentionally left empty
	}

	public boolean isFeatureCreated() {
		return model.isSet(FEATURE_KEY);
	}

	public Feature getFeature() {
		return model.get(FEATURE_KEY, (Feature)null);
	}

	public JointPositions getFeatureJointPositions() {
		return model.get(FEATURE_JOINT_ANGLES_KEY, (JointPositions)null);
	}

	void createFeature() {
		userInterfaceAPI.getUserInteraction().getUserDefinedRobotPosition(new RobotPositionCallback2() {
			@Override
			public void onOk(PositionParameters positionParameters) {
				Feature feature = featureContributionModel.addFeature(FEATURE_KEY, SUGGESTED_FEATURE_NAME, positionParameters.getPose());
				model.set(FEATURE_KEY, feature);
				// joint positions are not part of the feature. They are only used by the program node, for creating waypoints
				model.set(FEATURE_JOINT_ANGLES_KEY, positionParameters.getJointPositions());
				view.featureIsCreated(true);
			}
		});
	}

	void updateFeature() {
		userInterfaceAPI.getUserInteraction().getUserDefinedRobotPosition(new RobotPositionCallback2() {
			@Override
			public void onOk(PositionParameters positionParameters) {
				featureContributionModel.updateFeature(FEATURE_KEY, positionParameters.getPose());
				model.set(FEATURE_JOINT_ANGLES_KEY, positionParameters.getJointPositions());
			}
		});

	}

	void deleteFeature() {
		featureContributionModel.removeFeature(FEATURE_KEY);
		model.remove(FEATURE_KEY);
		model.remove(FEATURE_JOINT_ANGLES_KEY);
		view.featureIsCreated(false);
	}
}
