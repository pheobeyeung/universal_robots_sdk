package com.ur.urcap.examples.tooliocontrol.program;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;

public class ToolIOControlProgramNodeView implements SwingProgramNodeView<ToolIOControlProgramNodeContribution> {
	private static final int SPACING = 10;

	protected final ViewAPIProvider apiProvider;
	private Box help;

	public ToolIOControlProgramNodeView(ViewAPIProvider apiProvider) {
		this.apiProvider = apiProvider;
	}

	@Override
	public void buildUI(JPanel panel, ContributionProvider<ToolIOControlProgramNodeContribution> provider) {
		BoxLayout mgr = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(mgr);

		help = Box.createHorizontalBox();
		help.add(new JLabel(new ImageIcon(getClass().getResource("/icons/warning.png"))));
		help.add(new JLabel("Incompatible Tool I/O interface settings."));
		help.add(createSpacing());
		panel.add(help);
	}

	public void showHelp(boolean show) {
		help.setVisible(show);
	}

	protected Component createSpacing() {
		return Box.createVerticalStrut(SPACING);
	}
}
