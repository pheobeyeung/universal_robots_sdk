package com.ur.urcap.examples.createpayload.installation;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.payload.Payload;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.ur.urcap.api.domain.value.simple.Length;
import com.ur.urcap.api.domain.value.simple.Mass;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class CreatePayloadInstallationNodeView implements SwingInstallationNodeView<CreatePayloadInstallationNodeContribution> {
	private static final String DESCRIPTION_TXT = "<html>Create and modify an installation payload, that will be used in the Use Payload program node.<br/> " +
			"The created payload can be inspected in the Payload screen under the installation tab</html>";
	private static final String CREATE_PAYLOAD_TXT = "Create Payload";
	private static final String DELETE_PAYLOAD_TXT = "Delete Payload";
	private static final String LENGTH_UNIT_TXT = "mm";
	private static final String MASS_UNIT_TXT = "kg";
	private static final String MASS_TXT = "Mass";
	private static final String CENTER_OF_GRAVITY_TXT = "Center Of Gravity ";
	private static final String X_COORDINATE_TXT = "X ";
	private static final String Y_COORDINATE_TXT = "Y ";
	private static final String Z_COORDINATE_TXT = "Z ";

	private static final String LENGTH_FORMAT = "%1$.2f";
	private static final String MASS_FORMAT = "%1$.3f";

	private static final Dimension INPUT_FIELD_SIZE = new Dimension(120, 30);
	private CreatePayloadInstallationNodeContribution contribution;

	private JButton createPayloadButton;
	private JButton deletePayloadButton;
	private JPanel centerPanel;

	private final JTextField massField = new JTextField();
	private final JTextField centerOfGravityXField = new JTextField();
	private final JTextField centerOfGravityYField = new JTextField();
	private final JTextField centerOfGravityZField = new JTextField();

	@Override
	public void buildUI(JPanel panel, final CreatePayloadInstallationNodeContribution contribution) {
		this.contribution = contribution;

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(createHeaderPanel());
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		panel.add(createCenterPanel());
		panel.add(Box.createRigidArea(new Dimension(0,350)));

		centerPanel.setVisible(false);
		deletePayloadButton.setEnabled(false);
	}

	private JPanel createHeaderPanel() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));

		createPayloadButton = createCreatePayloadButton();
		deletePayloadButton = createDeletePayloadButton();

		result.add(createPayloadButton);
		result.add(Box.createRigidArea(new Dimension(10,0)));
		result.add(deletePayloadButton);

		return result;
	}

	private JButton createDeletePayloadButton() {
		JButton deleteButton = new JButton(DELETE_PAYLOAD_TXT);
		deleteButton.setFocusPainted(false);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				contribution.deletePayload();
			}
		});
		return deleteButton;
	}

	private JButton createCreatePayloadButton() {
		JButton createButton = new JButton(CREATE_PAYLOAD_TXT);
		createButton.setFocusPainted(false);
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				contribution.createPayload();
			}
		});
		return createButton;
	}

	private JPanel createCenterPanel() {
		centerPanel = new JPanel();

		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		centerPanel.add(createMassAndCenterOfGravityPanel());
		centerPanel.add(Box.createRigidArea(new Dimension(0,10)));

		return centerPanel;
	}

	private JPanel createMassAndCenterOfGravityPanel() {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		result.add(createMassPanel());
		result.add(Box.createRigidArea(new Dimension(0,10)));
		result.add(createCenterOfGravityPanel());

		return result;
	}

	private JPanel createMassPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));

		Box massBox = createLabelInputField(MASS_TXT, MASS_UNIT_TXT, massField, new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				KeyboardNumberInput<Double> keyboardInput = contribution.getKeyboardInputForMass();
				keyboardInput.show(massField, contribution.getKeyBoardCallbackForMass());
			}
		});

		panel.add(massBox);
		return panel;
	}

	public void updateInputFields(Payload payload) {
		massField.setText(String.format(MASS_FORMAT, payload.getMass().getAs(Mass.Unit.KG)));
		centerOfGravityXField.setText(String.format(LENGTH_FORMAT, payload.getCenterOfGravity().getX(Length.Unit.MM)));
		centerOfGravityYField.setText(String.format(LENGTH_FORMAT, payload.getCenterOfGravity().getY(Length.Unit.MM)));
		centerOfGravityZField.setText(String.format(LENGTH_FORMAT, payload.getCenterOfGravity().getZ(Length.Unit.MM)));
	}

	private JPanel createCenterOfGravityPanel() {
		JPanel outerpanel = new JPanel();
		outerpanel.setLayout(new BoxLayout(outerpanel, BoxLayout.Y_AXIS));

		JPanel panel = createLeftFlowPanel(new JLabel(CENTER_OF_GRAVITY_TXT));
		outerpanel.add(panel);

		panel = createLeftFlowPanel(createLabelInputField(X_COORDINATE_TXT,
				LENGTH_UNIT_TXT, centerOfGravityXField,
				new CenterOfGravityMouseAdapter(centerOfGravityXField, CenterOfGravityCoordinate.CX)));
		outerpanel.add(panel);

		panel = createLeftFlowPanel(createLabelInputField(Y_COORDINATE_TXT,
				LENGTH_UNIT_TXT, centerOfGravityYField,
				new CenterOfGravityMouseAdapter(centerOfGravityYField, CenterOfGravityCoordinate.CY)));
		outerpanel.add(panel);

		panel = createLeftFlowPanel(createLabelInputField(Z_COORDINATE_TXT,
				LENGTH_UNIT_TXT, centerOfGravityZField,
				new CenterOfGravityMouseAdapter(centerOfGravityZField, CenterOfGravityCoordinate.CZ)));
		outerpanel.add(panel);

		return outerpanel;
	}

	private JPanel createLeftFlowPanel(JComponent component) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(component);
		return panel;
	}

	public void update() {
		Payload payload = contribution.getPayload();

		if (payload == null || !payload.isResolvable()) {
			createPayloadButton.setEnabled(true);
			deletePayloadButton.setEnabled(false);
			centerPanel.setVisible(false);
			return;
		}

		createPayloadButton.setEnabled(false);
		deletePayloadButton.setEnabled(true);
		centerPanel.setVisible(true);

		updateInputFields(contribution.getPayload());
	}

	private Box createLabelInputField(String label, String unit, final JTextField inputField, MouseAdapter mouseAdapter) {
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel jLabel = new JLabel(label);
		horizontalBox.add(jLabel);
		horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));

		inputField.setHorizontalAlignment(SwingConstants.RIGHT);
		inputField.setFocusable(false);
		inputField.setPreferredSize(INPUT_FIELD_SIZE);
		inputField.setMaximumSize(INPUT_FIELD_SIZE);
		inputField.setMinimumSize(INPUT_FIELD_SIZE);
		inputField.addMouseListener(mouseAdapter);
		horizontalBox.add(inputField);
		horizontalBox.add(Box.createRigidArea(new Dimension(10, 0)));

		jLabel = new JLabel(unit);
		horizontalBox.add(jLabel);

		return horizontalBox;
	}

	private class CenterOfGravityMouseAdapter extends MouseAdapter {
		private final JTextField centerOfGravityField;
		private final CenterOfGravityCoordinate centerOfGravityCoordinate;
		public CenterOfGravityMouseAdapter(JTextField centerOfGravityField, CenterOfGravityCoordinate centerOfGravityCoordinate) {
			this.centerOfGravityField = centerOfGravityField;
			this.centerOfGravityCoordinate = centerOfGravityCoordinate;
		}

		@Override
		public void mousePressed(MouseEvent mouseEvent) {
			KeyboardNumberInput<Double> keyboardInput = contribution.getKeyboardInputForCenterOfGravity(centerOfGravityCoordinate);
			keyboardInput.show(centerOfGravityField, contribution.getKeyBoardCallbackForCenterOfGravity(centerOfGravityCoordinate));
		}
	}
}
