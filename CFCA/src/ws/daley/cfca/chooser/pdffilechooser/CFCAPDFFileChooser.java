package ws.daley.cfca.chooser.pdffilechooser;

import java.awt.event.ActionListener;

import javax.swing.filechooser.FileFilter;

import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.button.CFCAButtonState;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntryList;
import ws.daley.cfca.buttonpanel.button.CFCAButtonType;
import ws.daley.cfca.chooser.CFCAFileChooserPanel;
import ws.daley.cfca.chooser.CFCAFileChooserTitle;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCAPDFFileChooser extends CFCAFileChooserPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	public  CFCAPDFFileChooser(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout cfcaSpringLayout, CFCAFileChooserTitle cfcaFileChoosertitle, FileFilter filter, int model, boolean multiSelection)
	{
		super(cfcaTaskerIntf, cfcaSpringLayout, cfcaFileChoosertitle, filter, model, multiSelection);
	}

	@Override
	public void enablePanel()
	{
		enablePanel(this.cfcaTaskerIntf.getPDFSourceFolder());
		this.cfcaTaskerIntf.setSavePDFsButtonEnabled(false);
	}

	@Override
	public CFCAButtonStateEntryList getButtonStateEntryList()
	{
		return new CFCAButtonStateEntryList(
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
	}
}