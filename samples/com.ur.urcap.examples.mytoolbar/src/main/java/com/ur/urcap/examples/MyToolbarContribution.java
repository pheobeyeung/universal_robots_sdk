package com.ur.urcap.examples;

import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

class MyToolbarContribution implements SwingToolbarContribution {
	private static final int VERTICAL_SPACE = 10;
	private static final int HEADER_FONT_SIZE = 24;

	private final ToolbarContext context;
	private JLabel demoToolStatus;

	MyToolbarContribution(ToolbarContext context) {
		this.context = context;
	}

	@Override
	public void openView() {
		demoToolStatus.setText("<HTML>" + get3rdPartyStatus() + "</HTML>");
	}

	@Override
	public void closeView() {
	}

	public void buildUI(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

		jPanel.add(createHeader());
		jPanel.add(createVerticalSpace());
		jPanel.add(createInfo());
	}

	private Box createHeader() {
		Box headerBox = Box.createHorizontalBox();
		headerBox.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel header = new JLabel("My Swing Toolbar");
		header.setFont(header.getFont().deriveFont(Font.BOLD, HEADER_FONT_SIZE));
		headerBox.add(header);
		return headerBox;
	}

	private Box createInfo() {
		Box infoBox = Box.createVerticalBox();
		infoBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel pane1 = new JLabel();
		pane1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pane1.setText("<HTML>This is a sample URCap Toolbar contribution. Feel free to use this as an example for creating new contributions.</HTML>");
		pane1.setBackground(infoBox.getBackground());
		infoBox.add(pane1);

		JLabel pane2 = new JLabel();
		Locale locale = context.getAPIProvider().getSystemAPI().getSystemSettings().getLocalization().getLocale();
		pane2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		pane2.setText("<HTML>Currently, the robot is configured to use the Locale: " + locale.getDisplayName() + "</HTML>");
		infoBox.add(pane2);

		demoToolStatus = new JLabel();
		demoToolStatus.setText("<HTML>" + get3rdPartyStatus() +"</HTML>");
		demoToolStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		infoBox.add(demoToolStatus);
		return infoBox;
	}

	private Component createVerticalSpace() {
		return Box.createRigidArea(new Dimension(0, VERTICAL_SPACE));
	}

	private String get3rdPartyStatus() {
		Date now = new Date();
		int number = new Random().nextInt(10) + 20;
		return  String.format("Tool status reading: %d, read at %tF %tT.", number, now, now);
	}
}
