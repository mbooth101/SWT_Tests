package uk.co.matbooth;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ScalingTest2 {

	public static void main(String[] args) throws MalformedURLException {
		// Enable smooth anti-aliasing
		System.setProperty("swt.autoScale.method", "smooth");

		// Have to trick the DPIUtil into thinking we are running with a scaling
		// factor of 2 (200%)
		Display display = new Display() {
			@Override
			protected int getDeviceZoom() {
				return 200;
			}

			@Override
			protected void checkSubclass() {
				// Nothing to see here...
			}
		};

		Shell shell = new Shell(display);
		shell.setSize(100, 100);

		System.out.println("Device Zoom: " + DPIUtil.getDeviceZoom());

		final URL url = new URL("file:gear_icon1.png");
		ImageDataProvider provider1x = new ImageDataProvider() {

			@Override
			public ImageData getImageData(int zoom) {
				if (zoom == 100) {
					return getImageData(url);
				}
				return null;
			}

			private ImageData getImageData(URL url) {
				ImageData result = null;
				try (InputStream in = new BufferedInputStream(url.openStream())) {
					if (in != null) {
						result = new ImageData(in);
					}
				} catch (IOException | SWTException e) {
				}
				return result;
			}
		};
		Image image1x = new Image(display, (ImageDataProvider) provider1x::getImageData);

		ToolBar bar = new ToolBar(shell, SWT.BORDER | SWT.FLAT);
		Rectangle clientArea = shell.getClientArea();
		bar.setBounds(clientArea.x, clientArea.y, 100, 32);

		ToolItem item1 = new ToolItem(bar, SWT.NONE);
		item1.setImage(image1x);
		item1.setToolTipText("1x");

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		image1x.dispose();
		display.dispose();
	}
}
