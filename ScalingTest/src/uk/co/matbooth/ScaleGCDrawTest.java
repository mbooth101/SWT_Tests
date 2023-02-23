package uk.co.matbooth;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ScaleGCDrawTest {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final int width = 40;
		final int height = 15;

		Button button = new Button(shell, SWT.FLAT);
		button.setSize(60, 30);

		Image image = new Image(display, width, height);
		GC gc = new GC(image);

		Color color = new Color(display, 255, 45, 45);
		gc.setBackground(color);
		gc.fillRectangle(0, 0, width, height);

		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawRectangle(0, 0, width - 1, height - 1);

		gc.dispose();

		button.setImage(image);

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
