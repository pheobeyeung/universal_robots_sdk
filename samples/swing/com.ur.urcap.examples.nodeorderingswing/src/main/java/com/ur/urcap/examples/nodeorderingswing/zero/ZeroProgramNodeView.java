package com.ur.urcap.examples.nodeorderingswing.zero;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.examples.nodeorderingswing.style.Style;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;

public class ZeroProgramNodeView implements SwingProgramNodeView<ZeroProgramNodeContribution> {

	private static final String SORT_ORDER_ID = "Display Order Id";
	private JLabel orderIdTitle;
	private JLabel orderIdValue;

	private final Style style;

	ZeroProgramNodeView(Style style) {
		this.style = style;
	}

	@Override
	public void buildUI(JPanel jPanel, final ContributionProvider<ZeroProgramNodeContribution> provider) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.add(createPreview());
	}

	private Box createPreview() {
		Box previewBox = Box.createHorizontalBox();
		previewBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		orderIdTitle = new JLabel(SORT_ORDER_ID);
		previewBox.add(orderIdTitle);

		previewBox.add(createHorizontalSpacing());

		orderIdValue = new JLabel();
		previewBox.add(orderIdValue);

		return previewBox;
	}

	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension(style.getHorizontalSpacing(), 0));
	}

	public void setNodeOrderId(double orderId) {
		orderIdValue.setText(Double.toString(orderId));
	}
}
