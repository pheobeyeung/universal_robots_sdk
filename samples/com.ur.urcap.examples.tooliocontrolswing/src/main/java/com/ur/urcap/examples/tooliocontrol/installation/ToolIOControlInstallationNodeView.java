package com.ur.urcap.examples.tooliocontrol.installation;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.robot.RobotModel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;

public class ToolIOControlInstallationNodeView implements SwingInstallationNodeView<ToolIOControlInstallationNodeContribution> {
	private static final int SPACING = 10;
	private static final String HELP_E_SERIES = "Please assign control to this URCap on the Tool I/O installation tab.";
	private static final String HELP_CB3 = "Please assign control to this URCap on the I/O tab.";

	private final boolean isESeries;
	private Box help;

	public ToolIOControlInstallationNodeView(ViewAPIProvider apiProvider) {
		isESeries = apiProvider.getSystemAPI().getRobotModel().getRobotSeries().equals(RobotModel.RobotSeries.E_SERIES);
	}

	@Override
	public void buildUI(JPanel panel, ToolIOControlInstallationNodeContribution contribution) {
		BoxLayout mgr = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(mgr);

		help = Box.createHorizontalBox();
		help.add(new JLabel(new ImageIcon(getClass().getResource("/icons/warning.png"))));
		help.add(new JLabel(isESeries ? HELP_E_SERIES : HELP_CB3));
		help.add(createSpacing());
		panel.add(help);
	}

	public void showHelp(boolean show) {
		help.setVisible(show);
	}

	private Component createSpacing() {
		return Box.createVerticalStrut(SPACING);
	}
}
