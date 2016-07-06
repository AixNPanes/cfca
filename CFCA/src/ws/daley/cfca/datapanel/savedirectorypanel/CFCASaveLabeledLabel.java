package ws.daley.cfca.datapanel.savedirectorypanel;

import java.awt.Color;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.plaf.basic.BasicBorders;

import ws.daley.cfca.CFCAOutputType;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.chooser.CFCAFileChooserTitle;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCASaveLabeledLabel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private CFCASaveTextLabel textLabel;
	public CFCASaveTextLabel getTextLabel() {return this.textLabel;}

	private JTextField fileField;
	public JTextField getFileField() {return this.fileField;}

	private CFCASaveFileSelectButton fileSelectButton;
	public CFCASaveFileSelectButton getFileSelectButton() {return this.fileSelectButton;}

	private CFCASaveFileCreateDirectoryButton createDirectoryButton;
	public CFCASaveFileCreateDirectoryButton getCreateDirectoryButton() {return this.createDirectoryButton;}

	private CFCAPanelIntf cfcaTaskerIntf;
	CFCASpringLayout layout;
	
	public CFCASaveLabeledLabel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout layout, CFCAOutputType type)
	{
		this(cfcaTaskerIntf, layout, type.name() + " Directory:", new CFCAFileChooserTitle("Select "+type.name()+" Output Directory"), type);
	}
	
	private CFCASaveLabeledLabel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout layout, String text, CFCAFileChooserTitle title, CFCAOutputType type)
	{
		super(layout);
		this.setName(type.name());
		this.layout = layout;
		this.cfcaTaskerIntf = cfcaTaskerIntf;
		
		this.textLabel = new CFCASaveTextLabel(text);
		this.textLabel.setLabelFor(this);
		this.add(this.textLabel);
		layout.putConstraint(SpringLayout.WEST, this.textLabel, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, this.textLabel, 5, SpringLayout.NORTH, this);
		
		this.fileField = new CFCASaveTextField(type.name());
		this.add(this.fileField);
		layout.putConstraint(SpringLayout.WEST, this.fileField, 5, SpringLayout.EAST, this.textLabel);
		layout.putConstraint(SpringLayout.NORTH, this.fileField, 5, SpringLayout.NORTH, this);

		this.fileSelectButton = new CFCASaveFileSelectButton(this.cfcaTaskerIntf, "Change", null, this, title);
		this.add(this.fileSelectButton);
		layout.putConstraint(SpringLayout.WEST, this.fileSelectButton, 5, SpringLayout.EAST, this.fileField);
		layout.putConstraint(SpringLayout.NORTH, this.fileSelectButton, 2, SpringLayout.NORTH, this);

		this.createDirectoryButton = new CFCASaveFileCreateDirectoryButton(cfcaTaskerIntf, "Create Directory", null, this);
		this.add(this.createDirectoryButton);
		layout.putConstraint(SpringLayout.WEST, this.createDirectoryButton, 5, SpringLayout.EAST, this.fileSelectButton);
		layout.putConstraint(SpringLayout.NORTH, this.createDirectoryButton, 2, SpringLayout.NORTH, this);

		this.setBorder(new BasicBorders.ButtonBorder(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK));
		this.setVisible(true);
		this.setEnabled(true);
	}
	
	public void setForegroundColor()
	{
		String fileName = this.getFileField().getText();
		File file = new File(fileName);
		boolean fileExists = file.exists();
		if (this.createDirectoryButton != null)
			this.createDirectoryButton.setEnabled(!fileExists);
		else
			(new RuntimeException("CFCASaveLabeledLable file does not exist "+fileName)).printStackTrace();;
		this.fileField.setBackground(fileExists?Color.WHITE:Color.RED);
	}

	public String getText() {return this.fileField.getText();}
	public void setText(String text) {this.fileField.setText(text);}
}
