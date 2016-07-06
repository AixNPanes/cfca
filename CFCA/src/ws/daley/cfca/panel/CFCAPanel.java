package ws.daley.cfca.panel;

import static ws.daley.cfca.util.CFCAUtil.CONFIG_FOLDER;
import static ws.daley.cfca.util.CFCAUtil.getFrame;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JPanel;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCAOutputType;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.CFCAButtonPanel;
import ws.daley.cfca.buttonpanel.button.CFCAButtonMap;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.buttonpanel.button.CFCAButtonType;
import ws.daley.cfca.datapanel.savedirectorypanel.CFCASaveLabeledLabel;
import ws.daley.cfca.music.MusicCollectionFormat;
import ws.daley.cfca.pdf.CFCADocumentConverter;
import ws.daley.cfca.pdf.CFCADocumentMap;
import ws.daley.cfca.progressbar.CFCAFileProgressBar;
import ws.daley.cfca.selectorpanel.CFCASelectorButtonType;
import ws.daley.cfca.selectorpanel.CFCASelectorPanel;

public class CFCAPanel extends JPanel implements CFCAPanelIntf
{
	private static final long serialVersionUID = 1L;

	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCAPanel.class);

	private CFCADocumentMap documentMap;

	private CFCADataPanel dataPanel;
	private CFCAButtonPanel buttonPanel;

	private TreeMap<String, File> fileMap = new TreeMap<String, File>();
	private MusicCollectionFormat musicCollectionFormat;

	public CFCAPanel()
	{
		this(new CFCASpringLayout());
	}

	public CFCAPanel(CFCASpringLayout layout)
	{
		super(layout);
		this.setName(this.getClass().getSimpleName());
		Component c = this;
		this.setLocation(100, 100);
		this.setPreferredSize(new Dimension(1000, 800));
		this.setMinimumSize(new Dimension(1000, 800));

		this.dataPanel = new CFCADataPanel(this, new CFCASpringLayout());
		c = layout.addToPanel(this, this.dataPanel, c, new Dimension(550, 750), true, false, true);

		this.buttonPanel = new CFCAButtonPanel(this);
		c = layout.addToPanel(this, this.buttonPanel, c, new Dimension(2000, 40), true, false, true);

		this.setVisible(true);
		if (log.isTraceEnabled())
			log.trace("Set visible");
	}

	@Override
	public void setupSelectedFiles(File[] selectedFiles)
	{
		for (File file : selectedFiles)
		{
			log.trace("SetSelectedFile "+file.getName());
			try {this.documentMap.put(file.getAbsolutePath(), CFCADocumentConverter.getSinglePages(file.getAbsolutePath(), this.musicCollectionFormat.getMusicEntry(file)));}
			catch (IOException e) {throw new RuntimeException(e);}
		}
	}

	@Override
	public void setOKButtonState(boolean state)
	{
		if (log.isTraceEnabled())
			log.trace(this.getName() + " setOKButtonState "+state);
		CFCAButtonMap buttonMap = this.buttonPanel.buttonMap;
		buttonMap.getButton(CFCAButtonType.OK).setVisible(true);
		buttonMap.getButton(CFCAButtonType.OK).setEnabled(state);
	}

	@Override
	public MusicCollectionFormat getMusicCollectionFormat()
	{
		if (this.musicCollectionFormat == null)
			this.musicCollectionFormat = MusicCollectionFormat.getCollection(this.getJsonFile());
		return this.musicCollectionFormat;
	}

	@Override
	public File getPDFSourceFolder()
	{
		getMusicCollectionFormat();
		return new File(new File(this.musicCollectionFormat.getFolder()), this.musicCollectionFormat.getSourcefolder());
	}

	@Override
	public void setButtonState(CFCAButtonStateEntry[] buttonStates)
	{
		if (this.buttonPanel != null)
			this.buttonPanel.setButtonState(buttonStates);
	}

	@Override
	public CFCAButtonPanel getButtonPanel() {return this.buttonPanel;}
	@Override
	public TreeMap<String, File> getFileMap() {return this.fileMap;}
	@Override
	public File getConfigFolder() {return new File(CONFIG_FOLDER);}
	@Override
	public File getJsonFile() {return this.dataPanel.jsonFileChooser.getJsonFile();}
	@Override
	public File[] getPDFInputFiles() {return this.dataPanel.pdfFileChooser.getSelectedFiles();}
	@Override
	public CFCASaveLabeledLabel getSaveDirectoryPanelLabel(CFCAOutputType type) {return this.dataPanel.saveDirectoryPanel.getLabel(type);}
	@Override
	public void setFocusOKButton() {getFrame(this).getRootPane().setDefaultButton(this.buttonPanel.buttonMap.getButton(CFCAButtonType.OK));}
	@Override
	public void setFocusCancelButton() {getFrame(this).getRootPane().setDefaultButton(this.buttonPanel.buttonMap.getButton(CFCAButtonType.CANCEL));}
	@Override
	public void setFocusSavePDFButton() {getFrame(this).getRootPane().setDefaultButton(this.buttonPanel.buttonMap.getButton(CFCAButtonType.SAVE));}
	@Override
	public void nextState() {this.dataPanel.nextState();}
	@Override
	public void validateFiles() {this.dataPanel.saveDirectoryPanel.validateFiles();}
	@Override
	public void setSavePDFsButtonEnabled(boolean enable) {this.buttonPanel.setSavePDFsButtonEnabled(enable);}
	@Override
	public List<CFCAFileProgressBar> getPDFInputProgressBars() {return this.dataPanel.inputProgressBars.progressBarList;}
	@Override
	public boolean isBookSelected() {return this.dataPanel.selectorPanel.getEntry(CFCASelectorButtonType.BOOKS);}
	@Override
	public boolean isGluedBookSelected() {return this.dataPanel.selectorPanel.getEntry(CFCASelectorButtonType.GLUED_BOOKS);}
	@Override
	public boolean isOctavoSelected() {return this.dataPanel.selectorPanel.getEntry(CFCASelectorButtonType.OCTAVOS);}
	@Override
	public boolean isCopySelected() {return this.dataPanel.selectorPanel.getEntry(CFCASelectorButtonType.COPIES);}
	@Override
	public boolean isTabsSelected() {return this.dataPanel.selectorPanel.getEntry(CFCASelectorButtonType.TABS);}
	@Override
	public boolean isSelectionSelected() {return this.dataPanel.selectorPanel.getEntry(CFCASelectorButtonType.BOOK_SELECTIONS);}
	@Override
	public CFCASelectorPanel getSelectorPanel() {return this.dataPanel == null?null:this.dataPanel.selectorPanel;}
	@Override
	public CFCADataPanel getDataPanel() {return this.dataPanel;}
	@Override
	public CFCADocumentMap getDocumentMap() {return this.documentMap;}
}
