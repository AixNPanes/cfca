package ws.daley.cfca.chooser.pdffilechooser;

import static ws.daley.cfca.util.CFCAUtil.PDF;
import static ws.daley.cfca.util.CFCAUtil.PDF_FILE;

import ws.daley.cfca.chooser.CFCAFileNameExtensionFilter;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCAPdfFileNameExtensionFilter extends CFCAFileNameExtensionFilter
{
	public CFCAPdfFileNameExtensionFilter(@SuppressWarnings("unused") CFCAPanelIntf cfcaTaskerIntf)
	{
		super(PDF_FILE, PDF);
	}
}
