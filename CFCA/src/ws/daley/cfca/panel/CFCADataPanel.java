package ws.daley.cfca.panel;

import static ws.daley.cfca.util.CFCAUtil.SELECT_INPUT_PDF_FILES_CHOOSER;
import static ws.daley.cfca.util.CFCAUtil.SELECT_JSON_CONTROL_FILES_CHOOSER;
import static ws.daley.cfca.util.CFCAUtil.cancel;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.chooser.CFCAMusicFileFilter;
import ws.daley.cfca.chooser.pdffilechooser.CFCAPDFFileChooser;
import ws.daley.cfca.datapanel.jsonfilechooser.CFCAJsonFileChooser;
import ws.daley.cfca.datapanel.jsonfilechooser.CFCAJsonFileNameExtensionFilter;
import ws.daley.cfca.datapanel.savedirectorypanel.CFCASaveDirectoryPanel;
import ws.daley.cfca.datapanel.savepdfprogressbar.CFCASavePDFProgressBars;
import ws.daley.cfca.datapanel.taborder.CFCATabOrderPanel;
import ws.daley.cfca.datapanelpdfinputprogressbar.CFCAPDFInputProgressBars;
import ws.daley.cfca.selectorpanel.CFCASelectorPanel;

public class CFCADataPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCADataPanel.class);

	public CFCAJsonFileChooser jsonFileChooser;
	public CFCAPDFFileChooser pdfFileChooser;
	public CFCASelectorPanel selectorPanel;
	public CFCATabOrderPanel tabOrderPanel;
	public CFCASaveDirectoryPanel saveDirectoryPanel;
	public CFCAPDFInputProgressBars inputProgressBars;
	public CFCASavePDFProgressBars saveProgressBars;

	private CFCAPanelIntf cfcaTaskerIntf;
	private List<CFCADataSubPanel> stateChange = new ArrayList<CFCADataSubPanel>();

	public CFCADataPanel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout layout)
	{
		super(layout);
		this.setName(this.getClass().getSimpleName());
		this.cfcaTaskerIntf = cfcaTaskerIntf;
		Dimension dimension = new Dimension(550, 400);
		CFCAMusicFileFilter musicFileFilter = new CFCAMusicFileFilter(cfcaTaskerIntf);
		CFCAJsonFileNameExtensionFilter jsonFileNameExtensionFilter = new CFCAJsonFileNameExtensionFilter(cfcaTaskerIntf);

		this.jsonFileChooser = new CFCAJsonFileChooser(this.cfcaTaskerIntf, layout, SELECT_JSON_CONTROL_FILES_CHOOSER, jsonFileNameExtensionFilter, JFileChooser.FILES_ONLY, false);
		layout.addToPanel(this, this.jsonFileChooser, this, dimension, true, true, true);

		this.pdfFileChooser = new CFCAPDFFileChooser(this.cfcaTaskerIntf, layout, SELECT_INPUT_PDF_FILES_CHOOSER, musicFileFilter, JFileChooser.FILES_ONLY, true);
		layout.addToPanel(this, this.pdfFileChooser, this, dimension, false, true, true);

		this.selectorPanel = new CFCASelectorPanel(this.cfcaTaskerIntf, layout);
		layout.addToPanel(this, this.selectorPanel, this, dimension, false, true, true);

		this.tabOrderPanel = new CFCATabOrderPanel(this.cfcaTaskerIntf, layout);
		layout.addToPanel(this, this.tabOrderPanel, this, dimension, false, true, true);
		
		this.saveDirectoryPanel = new CFCASaveDirectoryPanel(this.cfcaTaskerIntf, layout, true);
		layout.addToPanel(this, this.saveDirectoryPanel, this, dimension, false, true, true);

		this.inputProgressBars = new CFCAPDFInputProgressBars(this.cfcaTaskerIntf, layout);
		layout.addToPanel(this, this.inputProgressBars, this, dimension, false, true, true);

		this.saveProgressBars = new CFCASavePDFProgressBars(this.cfcaTaskerIntf, layout);
		layout.addToPanel(this, this.saveProgressBars, this, dimension, false, true, true);

		this.stateChange = new ArrayList<CFCADataSubPanel>();
		this.stateChange.addAll(
			Arrays.asList(
				new CFCADataSubPanel[] {
					null, 
					this.jsonFileChooser,
					this.selectorPanel,
					this.pdfFileChooser, 
					this.tabOrderPanel,
					this.inputProgressBars, 
					this.saveDirectoryPanel, 
					this.saveProgressBars}));
		this.nextState();
	}

	public void nextState()
	{
		if (this.stateChange.size() > 0)
		{
			CFCADataSubPanel subPanel = this.stateChange.remove(0);
			if (subPanel != null)
			{
				subPanel.disablePanel();
				subPanel.setEnabled(false);
				subPanel.setVisible(false);
			}
			if (this.stateChange.size() == 0)
				cancel(this);
			else
			{
				while(true)
				{
					subPanel = this.stateChange.get(0);
					if (subPanel == null)
						break;
					else if (subPanel.isPanelValid())
					{
						if (log.isTraceEnabled())
							log.trace("next panel - "+subPanel.getName());
						this.cfcaTaskerIntf.setButtonState(subPanel.getButtonStateEntryList().stream().toArray(CFCAButtonStateEntry[]::new));
						subPanel.enablePanel();
						subPanel.setVisible(true);
						subPanel.setEnabled(true);
						break;
					}
					else
						this.stateChange.remove(0);
				}
			}
		}
	}

	public void enableInputProgressBars()
	{
		if (log.isTraceEnabled())
			log.trace("enableInputProgressBars");
		Dimension d = new Dimension(this.getSize().width-5, this.getSize().height-5);
		this.saveDirectoryPanel.setVisible(false);
		this.saveDirectoryPanel.setEnabled(false);
		this.inputProgressBars.setMinimumSize(d);
		this.inputProgressBars.setPreferredSize(d);
		this.inputProgressBars.setSize(d);
		this.inputProgressBars.setMaximumSize(d);
		this.inputProgressBars.setVisible(true);
		this.inputProgressBars.setEnabled(true);
	}
}
