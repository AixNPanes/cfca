package ws.daley.cfca.datapanelpdfinputprogressbar;

import static ws.daley.cfca.util.CFCAUtil.setAllSize;

import java.awt.Dimension;
import java.io.File;
import java.util.concurrent.CountDownLatch;

import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.music.Music;
import ws.daley.cfca.panel.CFCAPanelIntf;
import ws.daley.cfca.panel.CFCAProgressBarSubPanel;
import ws.daley.cfca.progressbar.CFCAFileProgressBar;

public class CFCAPDFInputProgressBars extends CFCAProgressBarSubPanel
{
	private static final long serialVersionUID = 1L;

	public CFCAPDFInputProgressBars(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout layout)
	{
		super(cfcaTaskerIntf, layout);
	}

	private void setProgressBars(File[] files)
	{
		CFCAFileProgressBar previousBar = null;
		for(File file:files)
		{
			System.out.printf("%s - setProgressBars", file.getName());
			Music music = this.cfcaTaskerIntf.getMusicCollectionFormat().getMusicEntry(file);
			if (music.isSelected(this.cfcaTaskerIntf))
			{
				CFCAPDFInputProgressBar progressBar = new CFCAPDFInputProgressBar(file, music);
				previousBar = updateProgressBar(progressBar, 8, file.getName());
			}
		}
		setAllSize(this.progressBars, new Dimension(this.getSize().width, (previousBar.getMinimumSize().height + 5) * (files.length + 1) + 5));
		this.allDone = new CountDownLatch(this. progressBarList.size());
		for(CFCAFileProgressBar progressBar:this.progressBarList)
			progressBar.start();
	}

	@Override
	public void setProgressBars() {setProgressBars(this.cfcaTaskerIntf.getPDFInputFiles());}
}