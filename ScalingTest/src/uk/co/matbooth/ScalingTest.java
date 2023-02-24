package uk.co.matbooth;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
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

	private final List<String> imageNames = List.of("gear_icon0", /*"gear_icon1", "gear_icon2",*/ "gear_icon3");
	private final List<Image> images = new ArrayList<>();

	private void createSampleRow(Composite parent, String imageName) throws URISyntaxException {
		URL resource100 = getClass().getResource(imageName + ".png");
//		URL resource150 = getClass().getResource(imageName + "@1.5x.png");
		URL resource200 = getClass().getResource(imageName + "@2x.png");

		StringBuilder available = new StringBuilder();
		if(!Objects.isNull(resource100)) {
			available.append("1x");
		}
//		if(!Objects.isNull(resource150)) {
//			available.append(", 1.5x");
//		}
		if(!Objects.isNull(resource200)) {
			available.append(", 2x");
		}

		ImageDescriptor desc = ImageDescriptor.createFromURL(resource100);
		Image image = desc.createImage();

		boolean[] found = new boolean[2];
		ImageData choiceData = DPIUtil.validateAndGetImageDataAtZoom((ImageDataProvider) desc, DPIUtil.getDeviceZoom(),
				found);

		Label label = new Label(parent, SWT.NONE);
		label.setText(available.toString());
		Label pic = new Label(parent, SWT.NONE);
		pic.setImage(image);
		Label labelChoice = new Label(parent, SWT.NONE);
		switch (choiceData.width) {
		case 16:
			labelChoice.setText("1x (" + choiceData.width + "," + choiceData.height + ")");
			break;
//		case 24:
//			labelChoice.setText("1.5x (" + choiceData.width + "," + choiceData.height + ")");
//			break;
		case 32:
			labelChoice.setText("2x (" + choiceData.width + "," + choiceData.height + ")");
			break;
		}

		images.add(image);
	}

	public void run() throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(320, 160);
		shell.setLayout(new GridLayout(1, false));

		Label zoomLabel = new Label(shell, SWT.NONE);
		zoomLabel.setText("Current Scaling Factor: " + DPIUtil.getDeviceZoom() / 100f + "x");

		Composite comp = new Composite(shell, SWT.NONE);
		comp.setLayout(new GridLayout(3, false));

		Label availableHeading = new Label(comp, SWT.NONE);
		availableHeading.setText("Available:");
		Label choiceHeading = new Label(comp, SWT.NONE);
		choiceHeading.setText("Chosen:");
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		choiceHeading.setLayoutData(gridData);

		for (String imageName : imageNames) {
			createSampleRow(comp, imageName);
		}

		shell.open();
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
