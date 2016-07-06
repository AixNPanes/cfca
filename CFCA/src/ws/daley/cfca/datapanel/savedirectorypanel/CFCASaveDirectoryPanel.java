package ws.daley.cfca.datapanel.savedirectorypanel;

import static ws.daley.cfca.util.CFCAUtil.setAllSize;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCAOutputType;
import ws.daley.cfca.CFCAOutputTypeList;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.button.CFCAButtonState;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntryList;
import ws.daley.cfca.buttonpanel.button.CFCAButtonType;
import ws.daley.cfca.panel.CFCADataSubPanel;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCASaveDirectoryPanel extends CFCADataSubPanel implements ActionListener, DocumentListener
{
	private static final long serialVersionUID = 1L;

	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCASaveDirectoryPanel.class);

	private static final CFCAButtonStateEntryList defaultButtonStateEntryList = new CFCAButtonStateEntryList(
		new CFCAButtonStateEntry[]
			{
				new CFCAButtonStateEntry(CFCAButtonType.OK, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.CANCEL, CFCAButtonState.FOCUS),
				new CFCAButtonStateEntry(CFCAButtonType.UP, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.DOWN, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.SAVE, CFCAButtonState.VISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.SELECT, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.ALL, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.NONE, CFCAButtonState.INVISIBLE)
			}
	);

	private Map<CFCAOutputType, CFCASaveLabeledLabel> cfcaSaveLabeledLabelMap = new TreeMap<CFCAOutputType, CFCASaveLabeledLabel>();
	public JLabel filler;

	public CFCASaveDirectoryPanel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout cfcaSpringLayout, boolean isDoubleBuffered)
	{
		super(cfcaTaskerIntf, cfcaSpringLayout, isDoubleBuffered);

		Component previous = this;
		for(CFCAOutputType outputType:CFCAOutputTypeList.cfcaOutputTypes)
		{
			CFCASaveLabeledLabel cfcaSaveLabeledLabel = new CFCASaveLabeledLabel(this.cfcaTaskerIntf, cfcaSpringLayout, outputType);
			this.cfcaSaveLabeledLabelMap.put(outputType, cfcaSaveLabeledLabel);
			cfcaSpringLayout.addToPanel(this, cfcaSaveLabeledLabel, previous, null, true, false, true);
			previous = cfcaSaveLabeledLabel;
		}
		this.setEnabled(false);
		this.setVisible(false);
		if (log.isTraceEnabled())
			log.trace("set visible");
	}

	@Override
	public Component add(Component comp)
	{
		CFCASaveLabeledLabel label = (CFCASaveLabeledLabel)comp;
		label.getFileField().getDocument().addDocumentListener(this);
		return super.add(comp);
	}

	public CFCASaveLabeledLabel getLabel(CFCAOutputType outputType)
	{
		CFCASaveLabeledLabel cfcaSaveLabeledLabel = this.cfcaSaveLabeledLabelMap.get(outputType);
		if (cfcaSaveLabeledLabel == null)
				throw new RuntimeException();
		return cfcaSaveLabeledLabel;
	}

	private void setSaveDirectory(File directory, CFCASaveLabeledLabel labeledLabel, String subdirName)
	{
		File subDirectory = new File(directory, subdirName);
		labeledLabel.setText(subDirectory.getAbsolutePath());
	}

	private void setTextFields(File startDirectory)
	{
		for(Entry<CFCAOutputType, CFCASaveLabeledLabel> cfcaSaveLabeledLabelEntry:this.cfcaSaveLabeledLabelMap.entrySet())
			setSaveDirectory(startDirectory, cfcaSaveLabeledLabelEntry.getValue(), cfcaSaveLabeledLabelEntry.getKey().name());
		validateFiles();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (log.isDebugEnabled())
			log.debug("actionPerformed "+e.getSource().getClass());
		CFCASaveFileButtonAction b = (CFCASaveFileButtonAction)e.getSource();
		b.performAction();
	}

	public void validateFiles()
	{
		boolean state = true;
		for(CFCASaveLabeledLabel label:this.cfcaSaveLabeledLabelMap.values())
		{
			String fileName = label.getFileField().getText();
			boolean directoryExists = (new File(fileName)).exists();
			label.setForegroundColor();
			if (!directoryExists)
				state = false;
		}
		this.cfcaTaskerIntf.setSavePDFsButtonEnabled(state);
		if (state)
		{
			this.cfcaTaskerIntf.setSavePDFsButtonEnabled(true);
			this.cfcaTaskerIntf.setFocusSavePDFButton();
		}
	}

	private void updateFileFieldWidth()
	{
		int fileFieldWidth = 0;
		for(CFCASaveLabeledLabel cfcaSaveLabeledLabel:this.cfcaSaveLabeledLabelMap.values())
		{
			JTextField textField = cfcaSaveLabeledLabel.getFileField();
			fileFieldWidth = Math.max(fileFieldWidth, textField.getFontMetrics(textField.getFont()).stringWidth(textField.getText()));
		}
		for(CFCASaveLabeledLabel cfcaSaveLabeledLabel:this.cfcaSaveLabeledLabelMap.values())
		{
			JTextField textField = cfcaSaveLabeledLabel.getFileField();
			FontMetrics fm = textField.getFontMetrics(textField.getFont());
			setAllSize(cfcaSaveLabeledLabel.getFileField(), 
					new Dimension(
						Math.max(fileFieldWidth, fm.stringWidth(textField.getText())) + 10, 
						cfcaSaveLabeledLabel.getFileField().getHeight()));
		}
	}

	@Override
	public void enablePanel()
	{
		int directoryLabelHeight = 45;

		int textLabelWidth = 0;
		int labelHeight = 0;
		int fileFieldWidth = 0;
		for(CFCASaveLabeledLabel cfcaSaveLabeledLabel:this.cfcaSaveLabeledLabelMap.values())
		{
			textLabelWidth = Math.max(textLabelWidth, cfcaSaveLabeledLabel.getTextLabel().getWidth());
			labelHeight = Math.max(labelHeight, cfcaSaveLabeledLabel.getTextLabel().getHeight());
			fileFieldWidth = Math.max(fileFieldWidth, cfcaSaveLabeledLabel.getFileField().getWidth());
			labelHeight = Math.max(labelHeight, cfcaSaveLabeledLabel.getFileField().getHeight());
		}
		Component previous = this;
		for(CFCASaveLabeledLabel cfcaSaveLabeledLabel:this.cfcaSaveLabeledLabelMap.values())
		{
			this.cfcaSpringLayout.putConstraint(SpringLayout.WEST, cfcaSaveLabeledLabel, 5, SpringLayout.WEST, this);
			this.cfcaSpringLayout.putConstraint(SpringLayout.EAST, cfcaSaveLabeledLabel, -5, SpringLayout.EAST, this);
			this.cfcaSpringLayout.putConstraint(SpringLayout.NORTH, cfcaSaveLabeledLabel, directoryLabelHeight + 5, SpringLayout.NORTH, previous);
			this.cfcaSpringLayout.putConstraint(SpringLayout.SOUTH, cfcaSaveLabeledLabel, directoryLabelHeight, SpringLayout.NORTH, cfcaSaveLabeledLabel);
			setAllSize(cfcaSaveLabeledLabel.getTextLabel(), new Dimension(textLabelWidth, labelHeight));
			setAllSize(cfcaSaveLabeledLabel.getFileField(), new Dimension(fileFieldWidth, labelHeight));
			previous = cfcaSaveLabeledLabel;
		}
		this.updateFileFieldWidth();

		setSaveDirectories();
		validateFiles();
		if (log.isTraceEnabled())
			log.trace("enablePanel");
	}

	public void setSaveDirectories()
	{
		File startDirectory = new File(this.cfcaTaskerIntf.getMusicCollectionFormat().getFolder());
		try
		{
			File file = null;
			String[] jListData = this.cfcaTaskerIntf.getDataPanel().tabOrderPanel.getjListData();
			if (jListData == null) // We didn't use the TabOrderPanel
				file = this.cfcaTaskerIntf.getPDFInputFiles()[0];
			else
				file = this.cfcaTaskerIntf.getFileMap().get(jListData[0]);
			startDirectory = file.getParentFile().getParentFile().getCanonicalFile();
		}
		catch (IOException e1) {}
		setSaveDirectories(startDirectory);
	}

	private void setSaveDirectories(File startDirectory)
	{
		this.cfcaTaskerIntf.getDataPanel().saveDirectoryPanel.setTextFields(startDirectory);
	}

	@Override
	public void disablePanel() {this.cfcaTaskerIntf.setSavePDFsButtonEnabled(false);}
	@Override
	public void insertUpdate(@SuppressWarnings("unused") DocumentEvent e) {updateFileFieldWidth();}
	@Override
	public void removeUpdate(@SuppressWarnings("unused") DocumentEvent e) {updateFileFieldWidth();}
	@Override
	public void changedUpdate(@SuppressWarnings("unused") DocumentEvent e) {updateFileFieldWidth();}
	@Override
	public CFCAButtonStateEntryList getButtonStateEntryList() {return defaultButtonStateEntryList;}
}
