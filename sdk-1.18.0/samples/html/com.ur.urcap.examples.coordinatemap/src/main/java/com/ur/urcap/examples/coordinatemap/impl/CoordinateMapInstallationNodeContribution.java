package com.ur.urcap.examples.coordinatemap.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Img;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.annotation.Touch;
import com.ur.urcap.api.ui.component.ImgComponent;
import com.ur.urcap.api.ui.component.LabelComponent;
import com.ur.urcap.api.ui.component.TouchEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

public class CoordinateMapInstallationNodeContribution implements InstallationNodeContribution {

	private final static String IMAGE_PATH = "com/ur/urcap/examples/coordinatemap/impl/grid.png";
	public CoordinateMapInstallationNodeContribution(DataModel model) {
	}

	@Label(id = "x-coordinate")
	private LabelComponent xLabel;

	@Label(id = "y-coordinate")
	private LabelComponent yLabel;

	@Img(id="img")
	private ImgComponent imgComponent;

	@Touch(id = "img")
	public void onTouch(TouchEvent touchEvent) {
		if (touchEvent.getEventType() == TouchEvent.EventType.ON_PRESSED) {
			xLabel.setText("x=" + touchEvent.getRelativePosX());
			yLabel.setText("y=" + touchEvent.getRelativePosY());
		}
	}



	@Override
	public void openView() {
		BufferedImage img = loadImage();
		if (img != null) {
			imgComponent.setImage(img);
		}
		xLabel.setText("x=");
		yLabel.setText("y=");
	}

	private BufferedImage loadImage() {
		BufferedImage bufferedImage = null;
		BufferedInputStream bufferedInputStream = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(IMAGE_PATH));
		try {
			bufferedImage = ImageIO.read(bufferedInputStream);
		} catch (IOException e) {
			System.err.println("Unable to load image: " + IMAGE_PATH);
		} finally {
			if (bufferedImage != null) {
				try {
					bufferedInputStream.close();
				} catch (IOException e) {
					System.err.println("Failed to close image input stream " + e.getMessage());
				}
			}
		}
		return bufferedImage;
	}

	@Override
	public void closeView() { }

	@Override
	public void generateScript(ScriptWriter writer) {
	}
}
