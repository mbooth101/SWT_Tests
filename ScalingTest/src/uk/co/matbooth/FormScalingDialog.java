package uk.co.matbooth;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

public class FormScalingDialog extends FormDialog {

	public FormScalingDialog(Shell shell) {
		super(shell);
	}

	@Override
	protected void createFormContent(IManagedForm mform) {
		mform.getForm().setText("Devices");
		mform.getForm().getBody().setLayout(new GridLayout(1, true));

		Composite content = mform.getToolkit().createComposite(mform.getForm().getBody(), SWT.NONE);
		content.setLayout(new GridLayout(1, false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		addExpandableComposite(mform, content);
		addSection(mform, content);
	}

	private void addSection(IManagedForm mform, Composite parent) {
		Section sectionDeviceSelection = mform.getToolkit().createSection(parent, Section.TITLE_BAR);
		addControls(mform, sectionDeviceSelection);
	}
	private void addExpandableComposite(IManagedForm mform, Composite parent) {
		ExpandableComposite sectionDeviceSelection = mform.getToolkit().createExpandableComposite(parent, Section.TITLE_BAR);
		addControls(mform, sectionDeviceSelection);
	}
	
	private void addControls(IManagedForm mform, ExpandableComposite sectionDeviceSelection) {
		sectionDeviceSelection.setText("Device Selection");
		sectionDeviceSelection.setLayout(new GridLayout(1, false));
		sectionDeviceSelection.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		Composite parentDeviceDetails = mform.getToolkit().createComposite(sectionDeviceSelection, SWT.NONE);
		parentDeviceDetails.setLayout(new GridLayout(2, false));
		parentDeviceDetails.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		sectionDeviceSelection.setClient(parentDeviceDetails);

		Composite parentDeviceSelection = mform.getToolkit().createComposite(parentDeviceDetails, SWT.NONE);
		parentDeviceSelection.setLayout(new GridLayout(2, false));
		Group boardDetailsParent = new Group(parentDeviceDetails, SWT.NONE);
		mform.getToolkit().adapt(boardDetailsParent);
		boardDetailsParent.setText("Board Details");
		boardDetailsParent.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gridData.widthHint = 300;
		gridData.heightHint = 80;
		boardDetailsParent.setLayoutData(gridData);
	}

	public static void main(String[] args) throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);

		FormScalingDialog dialog = new FormScalingDialog(shell);
		dialog.create();
		dialog.getShell().setSize(500, 500);
		dialog.open();
	}
}
