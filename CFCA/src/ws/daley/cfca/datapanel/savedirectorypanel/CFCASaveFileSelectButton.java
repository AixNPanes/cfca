package ws.daley.cfca.datapanel.savedirectorypanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.Tuple;
import ws.daley.cfca.chooser.CFCAChooserDirectory;
import ws.daley.cfca.chooser.CFCAFileChooserTitle;
import ws.daley.cfca.chooser.CFCAFileSelectionMode;
import ws.daley.cfca.chooser.pdffilechooser.CFCAPDFFileChooser;
import ws.daley.cfca.chooser.pdffilechooser.CFCAPdfFileNameExtensionFilter;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCASaveFileSelectButton extends JButton implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private CFCASaveLabeledLabel saveLabeledLabel;
	private CFCAFileChooserTitle fileChooserTitle;

	private CFCAPanelIntf cfcaTaskerIntf;

	public CFCASaveFileSelectButton(CFCAPanelIntf cfcaTaskerIntf, String text, Icon icon, CFCASaveLabeledLabel saveLabeledLabel, CFCAFileChooserTitle fileChooserTitle)
	{
		super(text, icon);
		this.cfcaTaskerIntf = cfcaTaskerIntf;
		this.saveLabeledLabel = saveLabeledLabel;
		this.fileChooserTitle = fileChooserTitle;
		this.addActionListener(this);
		setVisible(true);
		setEnabled(true);
	}

	@Override
	public void actionPerformed(@SuppressWarnings("unused") ActionEvent e)
	{
		CFCAPdfFileNameExtensionFilter pdfFileNameExtensionFilter = new CFCAPdfFileNameExtensionFilter(this.cfcaTaskerIntf);
		Tuple<String, File> tuple = getChooserDirectory().get(getCurrentFile());
		CFCAPDFFileChooser fileChooser = new CFCAPDFFileChooser(this.cfcaTaskerIntf, new CFCASpringLayout(), this.fileChooserTitle, pdfFileNameExtensionFilter, CFCAFileSelectionMode.DIRECTORIES_ONLY.mode(), false);
		if (tuple != null)
			fileChooser.getFileChooser().setSelectedFile(tuple.getValue());
		else
		{
			this.saveLabeledLabel.setForeground(Color.RED);
			(new RuntimeException("tuple is null! "+getChooserDirectory().getName())).printStackTrace();
		}
		if (JFileChooser.APPROVE_OPTION == fileChooser.getFileChooser().showDialog(this.getParent(), this.fileChooserTitle.getTitle()))
			this.saveLabeledLabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
		this.cfcaTaskerIntf.validateFiles();
	}

	private CFCAChooserDirectory getChooserDirectory() {return new CFCAChooserDirectory(getCurrentFile().getParentFile());}
	private File getCurrentFile() {return new File(this.saveLabeledLabel.getText());}
}