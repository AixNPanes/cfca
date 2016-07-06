package ws.daley.cfca.panel;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

import ws.daley.cfca.CFCAOutputType;
import ws.daley.cfca.buttonpanel.CFCAButtonPanel;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.datapanel.savedirectorypanel.CFCASaveLabeledLabel;
import ws.daley.cfca.music.MusicCollectionFormat;
import ws.daley.cfca.pdf.CFCADocumentMap;
import ws.daley.cfca.progressbar.CFCAFileProgressBar;
import ws.daley.cfca.selectorpanel.CFCASelectorPanel;

public interface CFCAPanelIntf
{
	public abstract CFCADocumentMap				getDocumentMap();
	public abstract CFCAButtonPanel				getButtonPanel();
	public abstract File						getConfigFolder();
	public abstract CFCADataPanel				getDataPanel();
	public abstract TreeMap<String, File>		getFileMap();
	public abstract File						getJsonFile();
	public abstract MusicCollectionFormat		getMusicCollectionFormat();
	public abstract File[]						getPDFInputFiles();
	public abstract List<CFCAFileProgressBar>	getPDFInputProgressBars();
	public abstract File						getPDFSourceFolder();
	public abstract CFCASaveLabeledLabel		getSaveDirectoryPanelLabel(CFCAOutputType type);
	public abstract CFCASelectorPanel			getSelectorPanel();
	public abstract boolean						isBookSelected();
	public abstract boolean						isGluedBookSelected();
	public abstract boolean						isCopySelected();
	public abstract boolean						isOctavoSelected();
	public abstract boolean						isSelectionSelected();
	public abstract boolean						isTabsSelected();
	public abstract void						nextState();
	public abstract void						setButtonState(CFCAButtonStateEntry[] buttonStates);
	public abstract void						setFocusCancelButton();
	public abstract void						setFocusOKButton();
	public abstract void						setFocusSavePDFButton();
	public abstract void						setOKButtonState(boolean state);
	public abstract void						setSavePDFsButtonEnabled(boolean enable);
	public abstract void						setupSelectedFiles(File[] files);
	public abstract void						validateFiles();
}
