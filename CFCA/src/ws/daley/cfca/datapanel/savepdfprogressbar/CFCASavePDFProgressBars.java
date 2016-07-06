package ws.daley.cfca.datapanel.savepdfprogressbar;

import static ws.daley.cfca.util.CFCAUtil.MINUS;
import static ws.daley.cfca.util.CFCAUtil.setAllSize;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import ws.daley.cfca.CFCAOutputType;
import ws.daley.cfca.CFCAOutputTypeList;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.datapanelpdfinputprogressbar.CFCAPDFInputProgressBar;
import ws.daley.cfca.panel.CFCAPanelIntf;
import ws.daley.cfca.panel.CFCAProgressBarSubPanel;
import ws.daley.cfca.pdf.CFCAImageMap;
import ws.daley.cfca.progressbar.CFCAFileProgressBar;

public class CFCASavePDFProgressBars extends CFCAProgressBarSubPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public CFCASavePDFProgressBars(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout layout)
	{
		super(cfcaTaskerIntf, layout);
	}

	@Override
	public void setProgressBars()
	{
		List<CFCAImageMap> imageMapList = new ArrayList<CFCAImageMap>();
		for(CFCAFileProgressBar progressBar: this.cfcaTaskerIntf.getPDFInputProgressBars())
			imageMapList.add(((CFCAPDFInputProgressBar)progressBar).getTaskerImageMap());
//		for(CFCATabType tabType:CFCATabType.getTypes())
//			imageMapList.add(new CFCAImageMap(tabType));
		setProgressBars(imageMapList);
	}

	private void setProgressBars(List<CFCAImageMap> imageMapList)
	{
		CFCAFileProgressBar previousBar = null;
		for(CFCAImageMap imageMap:imageMapList)
			for(CFCAOutputType type: CFCAOutputTypeList.cfcaNonTabOutputTypes)
			{
				File file = new File(new File(
						this.cfcaTaskerIntf.getSaveDirectoryPanelLabel(type).getText()), 
						imageMap.getName());
				CFCASavePDFProgressBar progressBar = new CFCASavePDFProgressBar(type, file);
				progressBar.setTaskerImageMap(imageMap);
				previousBar = updateProgressBar(progressBar, 18, type.name() + MINUS + file.getName());
			}
		int height = (previousBar.getMinimumSize().height + 5) * (imageMapList.size() * 3 + 1) + 5;
		
		Dimension d = new Dimension(this.getSize().width, height);
		setAllSize(this.progressBars, d);
		this.allDone = new CountDownLatch(this. progressBarList.size());
		for(CFCAFileProgressBar progressBar:this.progressBarList)
			progressBar.start();
	}
}