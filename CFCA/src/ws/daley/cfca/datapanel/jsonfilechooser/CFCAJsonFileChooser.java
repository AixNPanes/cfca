package ws.daley.cfca.datapanel.jsonfilechooser;

import java.awt.event.ActionListener;
import java.io.File;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.button.CFCAButtonState;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntryList;
import ws.daley.cfca.buttonpanel.button.CFCAButtonType;
import ws.daley.cfca.chooser.CFCAFileChooserPanel;
import ws.daley.cfca.chooser.CFCAFileChooserTitle;
import ws.daley.cfca.chooser.CFCAFileNameExtensionFilter;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCAJsonFileChooser extends CFCAFileChooserPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("hiding")
	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCAJsonFileChooser.class);

	private static final CFCAButtonStateEntryList defaultButtonStateEntryList = new CFCAButtonStateEntryList(
		new CFCAButtonStateEntry[]
			{
				new CFCAButtonStateEntry(CFCAButtonType.OK, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.CANCEL, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.UP, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.DOWN, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.SAVE, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.SELECT, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.ALL, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.NONE, CFCAButtonState.INVISIBLE)
			}
	);

	public CFCAJsonFileChooser(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout cfcaSpringLayout, CFCAFileChooserTitle cfcaFileChooserTitle, CFCAFileNameExtensionFilter filter, int model, boolean multiSelection)
	{
		super(cfcaTaskerIntf, cfcaSpringLayout, cfcaFileChooserTitle, filter, model, multiSelection);
	}

	public File getJsonFile()
	{
		if (log.isDebugEnabled())
			log.debug("selected " + super.getSelectedFile());
		return super.getSelectedFile();
	}
	@Override
	public void enablePanel() {enablePanel(this.cfcaTaskerIntf.getConfigFolder());}
	@Override
	public CFCAButtonStateEntryList getButtonStateEntryList() {return defaultButtonStateEntryList;}
}