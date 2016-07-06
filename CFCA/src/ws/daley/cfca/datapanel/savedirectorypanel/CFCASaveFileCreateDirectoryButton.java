package ws.daley.cfca.datapanel.savedirectorypanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JButton;

import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCASaveFileCreateDirectoryButton extends JButton implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private CFCASaveLabeledLabel saveLabeledLabel;
	private CFCAPanelIntf taskerPanelIntf;

	public CFCASaveFileCreateDirectoryButton(CFCAPanelIntf taskerPanelIntf, String text, Icon icon, CFCASaveLabeledLabel saveLabeledLabel)
	{
		super(text, icon);
		this.taskerPanelIntf = taskerPanelIntf;
		this.saveLabeledLabel = saveLabeledLabel;
		this.setVisible(!((new File(saveLabeledLabel.getText())).exists()));
		this.addActionListener(this);
		setVisible(true);
	}

	@Override
	public void actionPerformed(@SuppressWarnings("unused") ActionEvent e)
	{
		String directoryName = this.saveLabeledLabel.getText();
		File directory = new File(directoryName);
		directory.mkdirs();
		this.taskerPanelIntf.validateFiles();
	}
}