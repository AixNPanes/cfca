package ws.daley.cfca.chooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;

import static ws.daley.cfca.util.CFCAUtil.*;

import ws.daley.cfca.CFCAMusicFormat;
import ws.daley.cfca.music.Music;
import ws.daley.cfca.panel.CFCAPanelIntf;
import ws.daley.cfca.selectorpanel.CFCASelectorButtonType;
import ws.daley.cfca.selectorpanel.CFCASelectorPanel;

public class CFCAMusicFileFilter extends FileFilter
{
	private CFCAPanelIntf cfcaTaskerIntf;
	
	public CFCAMusicFileFilter(CFCAPanelIntf cfcaTaskerIntf) {this.cfcaTaskerIntf = cfcaTaskerIntf;}

	@Override
	public boolean accept(File f)
	{
		if (f.isDirectory())
			return true;
		if (PDF.equals(FilenameUtils.getExtension(f.getName().toLowerCase())))
		{
			Music music = this.cfcaTaskerIntf.getMusicCollectionFormat().getMusicEntry(f);
			if (music != null)
			{
				CFCAMusicFormat format = music.getFormat();
				CFCASelectorPanel selectorPanel = this.cfcaTaskerIntf.getSelectorPanel();
				if ((format.equals(CFCAMusicFormat.BOOK) && selectorPanel.getEntry(CFCASelectorButtonType.BOOKS)) ||
					(format.equals(CFCAMusicFormat.GLUED_BOOK) && selectorPanel.getEntry(CFCASelectorButtonType.GLUED_BOOKS)) ||
					(format.equals(CFCAMusicFormat.COPY) && selectorPanel.getEntry(CFCASelectorButtonType.COPIES)) ||
					(format.equals(CFCAMusicFormat.OCTAVO) && selectorPanel.getEntry(CFCASelectorButtonType.OCTAVOS)))
					return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {return PDF_FILES;}
}
