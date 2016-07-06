package ws.daley.cfca.datapanel.jsonfilechooser;

import static ws.daley.cfca.util.CFCAUtil.JSON;
import static ws.daley.cfca.util.CFCAUtil.JSON_FILE;

import ws.daley.cfca.chooser.CFCAFileNameExtensionFilter;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCAJsonFileNameExtensionFilter extends CFCAFileNameExtensionFilter
{
	public CFCAJsonFileNameExtensionFilter(@SuppressWarnings("unused") CFCAPanelIntf cfcaTaskerIntf)
	{
		super(JSON_FILE, JSON);
	}
}
