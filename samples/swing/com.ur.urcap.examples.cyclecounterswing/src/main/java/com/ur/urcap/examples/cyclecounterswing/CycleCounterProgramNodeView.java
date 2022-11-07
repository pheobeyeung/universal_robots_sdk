package com.ur.urcap.examples.cyclecounterswing;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
import com.ur.urcap.api.domain.variable.GlobalVariable;
import com.ur.urcap.api.domain.variable.Variable;
import com.ur.urcap.examples.style.Style;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CycleCounterProgramNodeView implements SwingProgramNodeView<CycleCounterProgramNodeContribution> {

	public static final String INFO_1 = "<html><body><p>Each time this node and its children are run, the counting variable will increment by one.<br>\n" +
			" Select an existing variable or create your own.</p><br>" +
			" <p>The variable is visible in the Variables tab, and can be used to trigger actions<br>" +
			" elsewhere in the program.</p></body></html>";

	public static final String INFO_2 = "Input variable name and press the \"Create New\" button";

	private final Style style;

	private JTextField newVariableTextField = new JTextField();
	private JButton newVariableButton = new JButton("Create new");
	private JComboBox variablesComboBox = new JComboBox();
	private JLabel errorLabel = new JLabel();
	private final ImageIcon errorIcon;

	public CycleCounterProgramNodeView(Style style) {
		this.style = style;
		this.errorIcon = getErrorImage();
	}

	@Override
	public void buildUI(JPanel jPanel, ContributionProvider<CycleCounterProgramNodeContribution> contributionProvider) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		jPanel.add(createInfo(INFO_1));
		jPanel.add(createVerticalSpacing());
		jPanel.add(createComboBox(contributionProvider));
		jPanel.add(createInfo(INFO_2));
		jPanel.add(createButtonBox(contributionProvider));
		jPanel.add(createVerticalSpacing());
		jPanel.add(createErrorLabel());
		jPanel.add(Box.createVerticalGlue());
	}

	private Box createErrorLabel() {
		Box infoBox = Box.createHorizontalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		errorLabel.setVisible(false);
		infoBox.add(errorLabel);
		return infoBox;
	}

	private Box createButtonBox(final ContributionProvider<CycleCounterProgramNodeContribution> provider) {
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		newVariableTextField.setFocusable(false);
		newVariableTextField.setPreferredSize(style.getInputfieldDimension());
		newVariableTextField.setMaximumSize(newVariableTextField.getPreferredSize());
		newVariableTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				KeyboardTextInput keyboardInput = provider.get().getKeyboardForInput();
				keyboardInput.show(newVariableTextField, provider.get().getCallbackForInput());
			}
		});

		horizontalBox.add(newVariableTextField);
		horizontalBox.add(createHorizontalSpacing());

		newVariableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				clearErrors();
				//Create a global variable with an initial value and store it in the data model to make it available to all program nodes.
				GlobalVariable variable = provider.get().createGlobalVariable(newVariableTextField.getText());
				provider.get().setVariable(variable);
				updateVariablesComboBox(provider.get());
			}
		});

		horizontalBox.add(newVariableButton);
		return horizontalBox;
	}

	private Box createInfo(String info) {
		Box infoBox = Box.createHorizontalBox();
		infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel label = new JLabel();
		label.setText(info);
		label.setSize(label.getPreferredSize());
		infoBox.add(label);
		return infoBox;
	}

	private Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(0, style.getVerticalSpacing()));
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	private Box createComboBox(final ContributionProvider<CycleCounterProgramNodeContribution> provider) {
		Box inputBox = Box.createHorizontalBox();
		inputBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		variablesComboBox.setFocusable(false);
		variablesComboBox.setPreferredSize(style.getComboBoxDimension());
		variablesComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					if (itemEvent.getItem() instanceof Variable) {
						provider.get().setVariable(((Variable) itemEvent.getItem()));
					} else {
						provider.get().removeVariable();
					}
				}
			}
		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(variablesComboBox, BorderLayout.CENTER);

		inputBox.add(panel);
		return inputBox;
	}

	public void setNewVariable(String value) {
		newVariableTextField.setText(value);
	}

	private ImageIcon getErrorImage() {
		try {
			BufferedImage image = ImageIO.read(getClass().getResource("/com/ur/urcap/examples/cyclecounterswing/warning-bigger.png"));
			return new ImageIcon(image);
		} catch (IOException e) {
			// Should not happen.
			throw new RuntimeException("Unexpected exception while loading icon.", e);
		}
	}

	public void setError(final String message) {
		errorLabel.setText("<html>Error: Could not create variable<br>" + message + "</html>");
		errorLabel.setIcon(errorIcon);
		errorLabel.setVisible(true);
	}

	private void clearInputVariableName() {
		newVariableTextField.setText("");
	}

	private void clearErrors() {
		errorLabel.setVisible(false);
	}

	private void updateVariablesComboBox(CycleCounterProgramNodeContribution contribution) {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		List<Variable> variables = getVariables(contribution);

		Variable selectedVariable = contribution.getSelectedVariable();
		if (selectedVariable != null) {
			model.setSelectedItem(selectedVariable);
		}
		model.addElement("<Variable>");

		for (Variable variable : variables) {
			model.addElement(variable);
		}

		variablesComboBox.setModel(model);
	}

	private List<Variable> getVariables(CycleCounterProgramNodeContribution contribution) {
		List<Variable> sortedVariables = new ArrayList<Variable>(contribution.getGlobalVariables());

		Collections.sort(sortedVariables, new Comparator<Variable>() {
			@Override
			public int compare(Variable var1, Variable var2) {
				if (var1.toString().toLowerCase().compareTo(var2.toString().toLowerCase()) == 0) {
					//Sort lowercase/uppercase consistently
					return var1.toString().compareTo(var2.toString());
				} else {
					return var1.toString().toLowerCase().compareTo(var2.toString().toLowerCase());
				}
			}
		});

		return sortedVariables;
	}

	public void updateView(CycleCounterProgramNodeContribution contribution) {
		clearInputVariableName();
		clearErrors();
		updateVariablesComboBox(contribution);
	}
}
