package uk.co.matbooth;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ScalingTest {

	private final List<String> imageNames = List.of("gear_icon0", "gear_icon1", "gear_icon2");
	private final List<Image> images = new ArrayList<>();

	private static void fixupAutoScaleMethod(boolean nearest) {
		try {
			Field autoScaleMethod = DPIUtil.class.getDeclaredField("autoScaleMethod");
			autoScaleMethod.setAccessible(true);
			if (nearest) {
				Enum value = Enum.valueOf((Class<Enum>) autoScaleMethod.getType(), "NEAREST");
				autoScaleMethod.set(null, value);
			} else {
				Enum value = Enum.valueOf((Class<Enum>) autoScaleMethod.getType(), "SMOOTH");
				autoScaleMethod.set(null, value);
			}
		} catch (ReflectiveOperationException | RuntimeException e) {
			e.printStackTrace();
		}
	}

	private void createSampleRow(Composite parent, String imageName) throws Exception {

		StringBuilder available = new StringBuilder();
		if (!Objects.isNull(getClass().getResource(imageName + ".png"))) {
			available.append("1x");
		}
		if (!Objects.isNull(getClass().getResource(imageName + "@2x.png"))) {
			if (available.length() != 0) {
				available.append(", ");
			}
			available.append("2x");
		}

		Label label = new Label(parent, SWT.NONE);
		label.setText(available.toString());

		URL resource = getClass().getResource(".").toURI().resolve(imageName + ".png").toURL();

		fixupAutoScaleMethod(true);
		ImageDescriptor descNearest = ImageDescriptor.createFromURL(resource);
		Image imageNearest = descNearest.createImage();

		Label picNearest = new Label(parent, SWT.NONE);
		picNearest.setImage(imageNearest);
		picNearest.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		fixupAutoScaleMethod(false);
		ImageDescriptor descSmooth = ImageDescriptor.createFromURL(resource);
		Image imageSmooth = descSmooth.createImage();

		Label picSmooth = new Label(parent, SWT.NONE);
		picSmooth.setImage(imageSmooth);
		picSmooth.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		Label labelChoice = new Label(parent, SWT.NONE);
		boolean[] found = new boolean[2];
		try {
			ImageData choiceData = DPIUtil.validateAndGetImageDataAtZoom((ImageDataProvider) descNearest,
					DPIUtil.getDeviceZoom(), found);
			switch (choiceData.width) {
			case 32:
				labelChoice.setText("1x (" + choiceData.width + "," + choiceData.height + ")");
				break;
			case 64:
				labelChoice.setText("2x (" + choiceData.width + "," + choiceData.height + ")");
				break;
			}
		} catch (Exception e) {
			labelChoice.setText("Error");
			e.printStackTrace();
		}

		images.add(imageNearest);
		images.add(imageSmooth);
	}

	public void run() throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(320, 160);
		shell.setLayout(new GridLayout(1, false));

		Label zoomLabel = new Label(shell, SWT.NONE);
		zoomLabel.setText("Current Scaling Factor: " + DPIUtil.getDeviceZoom() / 100f + "x");

		Composite comp = new Composite(shell, SWT.NONE);
		comp.setLayout(new GridLayout(4, false));

		Label availableHeading = new Label(comp, SWT.NONE);
		availableHeading.setText("Available:");

		Label choiceHeading = new Label(comp, SWT.NONE);
		choiceHeading.setText("Chosen:");
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		choiceHeading.setLayoutData(gridData);

		new Label(comp, SWT.NONE);
		Label nHeading = new Label(comp, SWT.NONE);
		nHeading.setText("Nearest");
		FontData[] nFontData = nHeading.getFont().getFontData();
		nFontData[0].setHeight(5);
		nHeading.setFont(new Font(display, nFontData));
		nHeading.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		Label sHeading = new Label(comp, SWT.NONE);
		sHeading.setText("Smooth");
		FontData[] sFontData = sHeading.getFont().getFontData();
		sFontData[0].setHeight(5);
		sHeading.setFont(new Font(display, sFontData));
		sHeading.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		new Label(comp, SWT.NONE);

		for (String imageName : imageNames) {
			createSampleRow(comp, imageName);
		}

		shell.open();
		shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		for (Image image : images) {
			image.dispose();
		}
		display.dispose();
	}

	public static void main(String[] args) throws Exception {
		new ScalingTest().run();
	}
}
