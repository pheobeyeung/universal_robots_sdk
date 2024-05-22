package com.ur.urcap.examples.moveuntildetection;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.io.DigitalIO;
import com.ur.urcap.api.domain.io.IO;
import com.ur.urcap.api.domain.io.IOModel;
import com.ur.urcap.api.domain.program.ProgramModel;
import com.ur.urcap.api.domain.program.nodes.ProgramNodeFactory;
import com.ur.urcap.api.domain.program.nodes.builtin.*;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.directionnode.DirectionAxis;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.directionnode.DirectionNodeConfig;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.directionnode.DirectionNodeConfigBuilder;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.movenode.MoveLMoveNodeConfig;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.movenode.builder.MoveLConfigBuilder;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.movenode.builder.MoveNodeConfigBuilders;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.setnode.DigitalOutputSetNodeConfig;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.setnode.SetNodeConfigFactory;
import com.ur.urcap.api.domain.program.nodes.builtin.configurations.untilnode.*;
import com.ur.urcap.api.domain.program.structure.ProgramNodeVisitor;
import com.ur.urcap.api.domain.program.structure.TreeNode;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.validation.ErrorHandler;
import com.ur.urcap.api.domain.value.ValueFactoryProvider;
import com.ur.urcap.api.domain.value.blend.Blend;
import com.ur.urcap.api.domain.value.blend.BlendFactory;
import com.ur.urcap.api.domain.value.expression.Expression;
import com.ur.urcap.api.domain.value.expression.ExpressionBuilder;
import com.ur.urcap.api.domain.value.expression.InvalidExpressionException;
import com.ur.urcap.api.domain.value.simple.Acceleration;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.api.domain.value.simple.SimpleValueFactory;

import java.util.Iterator;

public class MoveUntilDetectionProgramNodeContribution implements ProgramNodeContribution {
	public final static double MIN_DISTANCE = 0;
	public final static double MAX_DISTANCE = 500;
	private static final String CONST_DISTANCE = "max-distance";

	private final MoveUntilDetectionProgramNodeView view;
	private final ProgramAPI programAPI;
	private final DataModel model;
	private final ProgramModel programModel;
	private final ProgramNodeFactory programNodeFactory;
	private final ValueFactoryProvider valueFactoryProvider;

	public MoveUntilDetectionProgramNodeContribution(ProgramAPIProvider apiProvider,
													 MoveUntilDetectionProgramNodeView view,
													 DataModel model,
													 CreationContext context) {
		this.view = view;
		this.programAPI = apiProvider.getProgramAPI();
		this.model = model;
		this.programModel = programAPI.getProgramModel();
		this.programNodeFactory = programModel.getProgramNodeFactory();
		this.valueFactoryProvider = programAPI.getValueFactoryProvider();

		if (context.getNodeCreationType() == CreationContext.NodeCreationType.NEW) {
			initialize();
		}
	}

	private void initialize() {
		try {
			final TreeNode rootNode = programModel.getRootTreeNode(this);

			MoveNode moveNode = createMoveNode();
			TreeNode moveTreeNode = rootNode.addChild(moveNode);

			DirectionNode directionNode = createDirectionNode();
			TreeNode directionTreeNode = moveTreeNode.addChild(directionNode);

			TreeNode untilDistanceTreeNode = getFirstChildNode(directionTreeNode);
			UntilNode untilDistance = (UntilNode) untilDistanceTreeNode.getProgramNode();
			configureUntilNode(untilDistance);

			PopupNode maxDistancePopup = createMaxDistancePopup();
			untilDistanceTreeNode.addChild(maxDistancePopup);

			UntilNode untilPartNode = createUntilPartNode();
			TreeNode utilPartTreeNode = directionTreeNode.addChild(untilPartNode);

			SetNode setNode = createSetNode();
			utilPartTreeNode.addChild(setNode);

			toggleTreeLock(rootNode, true);

		} catch (Exception e) {
			throw new IllegalStateException("Could not add direction and until nodes to program tree", e);
		}
	}

	@Override
	public void openView() {
		view.updateView(getDistanceInMM());
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return "Move Until Detection";
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		writer.writeChildren();
	}


	public void setDistance(final double mm) {
		final UntilNode untilDistance = findUntilNode();
		if (untilDistance == null) {
			return;
		}

		UndoRedoManager undoRedoManager = this.programAPI.getUndoRedoManager();
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				SimpleValueFactory simpleValueFactory = valueFactoryProvider.getSimpleValueFactory();
				Length distance = simpleValueFactory.createLength(mm, Length.Unit.MM);
				UntilNodeConfigFactory untilNodeConfigFactory = untilDistance.getConfigFactory();

				UntilNodeConfig config = untilDistance.getConfig();

				BlendParameters blendParameters;
				if (config instanceof DistanceUntilNodeConfig) {
					blendParameters = ((DistanceUntilNodeConfig) config).getBlendParameters();
				} else {
					blendParameters = createDefaultBlendParameters(untilNodeConfigFactory);
				}

				DistanceUntilNodeConfig updatedConfig = untilNodeConfigFactory.createDistanceConfig(
						distance,
						ErrorHandler.AUTO_CORRECT,
						blendParameters);

				model.set(CONST_DISTANCE, updatedConfig.getDistance());

				untilDistance.setConfig(updatedConfig);
			}
		});
	}

	private UntilNode findUntilNode() {
		TreeNode rootTreeNode = programModel.getRootTreeNode(this);
		final UntilNode[] result = new UntilNode[]{null};
		rootTreeNode.traverse(new ProgramNodeVisitor() {
			@Override
			public void visit(UntilNode untilNode, int index, int depth) {
				if (untilNode.getConfig() instanceof DistanceUntilNodeConfig) {
					result[0] = untilNode;
				}
			}
		});
		return result[0];
	}

	private static TreeNode getFirstChildNode(TreeNode node) {
		return node.getChildren().get(0);
	}

	public double getDistanceInMM() {
		return getDistanceLength().getAs(Length.Unit.MM);
	}

	private Length getDistanceLength() {
		SimpleValueFactory simpleValueFactory = this.valueFactoryProvider.getSimpleValueFactory();
		Length defaultValue = simpleValueFactory.createLength(200, Length.Unit.MM);
		return model.get(CONST_DISTANCE, defaultValue);
	}

	private MoveNode createMoveNode() {
		MoveNode moveNode = this.programNodeFactory.createMoveNodeNoTemplate();
		MoveNodeConfigBuilders builders = moveNode.getConfigBuilders();
		MoveLConfigBuilder builder = builders.createMoveLConfigBuilder();
		MoveLMoveNodeConfig moveLMoveNodeConfig = builder.build();
		moveNode.setConfig(moveLMoveNodeConfig);
		return moveNode;
	}

	private DirectionNode createDirectionNode() {
		DirectionNode directionNode = this.programNodeFactory.createDirectionNode();
		DirectionNodeConfigBuilder builder = directionNode.createConfigBuilder();
		DirectionNodeConfig directionNodeConfig = builder.setDirection(DirectionAxis.Axis.Z_MINUS).build();
		directionNode.setConfig(directionNodeConfig);
		return directionNode;
	}

	private void configureUntilNode(UntilNode untilDistance) {
		UntilNodeConfigFactory configFactory = untilDistance.getConfigFactory();
		Length distanceLength = getDistanceLength();

		BlendParameters blendParameters = createDefaultBlendParameters(configFactory);
		DistanceUntilNodeConfig config = configFactory.createDistanceConfig(
				distanceLength,
				ErrorHandler.AUTO_CORRECT,
				blendParameters);
		untilDistance.setConfig(config);
	}

	private BlendParameters createDefaultBlendParameters(UntilNodeConfigFactory configFactory) {
		SimpleValueFactory simpleValueFactory = this.valueFactoryProvider.getSimpleValueFactory();
		Length blendLength = simpleValueFactory.createLength(1, Length.Unit.MM);
		BlendFactory blendFactory = this.valueFactoryProvider.getBlendFactory();
		Blend blend = blendFactory.createBlend(blendLength);
		return configFactory.createBlendParameters(blend);
	}

	private PopupNode createMaxDistancePopup() {
		PopupNode popup = this.programNodeFactory.createPopupNode();
		popup.setType(PopupNode.Type.ERROR);
		popup.setMessage("ERROR: No part detected within max distance");
		return popup;
	}

	private UntilNode createUntilPartNode() throws InvalidExpressionException {

		UntilNode untilExpressionNode = this.programNodeFactory.createUntilNode();
		UntilNodeConfigFactory untilNodeConfigFactory = untilExpressionNode.getConfigFactory();
		SimpleValueFactory simpleValueFactory = this.valueFactoryProvider.getSimpleValueFactory();
		Acceleration acceleration = simpleValueFactory.createAcceleration(3000, Acceleration.Unit.MM_S2);
		DecelerationParameters deceleration = untilNodeConfigFactory.createDecelerationParameters(
				acceleration, ErrorHandler.AUTO_CORRECT);


		ExpressionBuilder expressionBuilder = this.valueFactoryProvider.createExpressionBuilder();
		IOModel ioModel = this.programAPI.getIOModel();
		Iterator<DigitalIO> digitalIOs = ioModel.getIOs(DigitalIO.class).iterator();
		while (digitalIOs.hasNext()) {
			IO io = digitalIOs.next();
			if (io.isInput()) {
				expressionBuilder.appendIO(io);
				break;
			}
		}
		Expression untilIOExpression = expressionBuilder.build();

		UntilNodeConfig untilExpressionConfig = untilNodeConfigFactory.createExpressionConfig(untilIOExpression, deceleration);
		untilExpressionNode.setConfig(untilExpressionConfig);
		return untilExpressionNode;
	}

	private SetNode createSetNode() throws InvalidDomainException {
		SetNode setNode = this.programNodeFactory.createSetNode();
		IOModel ioModel = this.programAPI.getIOModel();

		SetNodeConfigFactory configFactory = setNode.getConfigFactory();

		Iterator<DigitalIO> digitalIOs = ioModel.getIOs(DigitalIO.class).iterator();
		while (digitalIOs.hasNext()) {
			DigitalIO io = digitalIOs.next();
			if (!io.isInput()) {
				DigitalOutputSetNodeConfig config = configFactory.createDigitalOutputConfig(io, true);
				setNode.setConfig(config);
				break;
			}
		}
		return setNode;
	}

	private void toggleTreeLock(TreeNode root, boolean lock) {
		root.setChildSequenceLocked(lock);
		for (TreeNode child : root.getChildren()) {
			child.setChildSequenceLocked(lock);
			toggleTreeLock(child, lock);
		}
	}
}
