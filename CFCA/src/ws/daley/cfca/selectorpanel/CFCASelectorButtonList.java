package ws.daley.cfca.selectorpanel;

import java.util.TreeMap;

import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCASelectorButtonList extends TreeMap<CFCASelectorButtonType, CFCASelectorButton>
{
	private static final long serialVersionUID = 1L;

	private CFCAPanelIntf cfcaTaskerIntf;
	
	public CFCASelectorButtonList(CFCAPanelIntf cfcaTaskerIntf)
	{
		this.cfcaTaskerIntf = cfcaTaskerIntf;
		this.put(CFCASelectorButtonType.BOOKS, new CFCASelectorButton(this.cfcaTaskerIntf, CFCASelectorButtonType.BOOKS, true));
		this.put(CFCASelectorButtonType.GLUED_BOOKS, new CFCASelectorButton(this.cfcaTaskerIntf, CFCASelectorButtonType.GLUED_BOOKS, true));
		this.put(CFCASelectorButtonType.BOOK_SELECTIONS, new CFCASelectorButton(this.cfcaTaskerIntf, CFCASelectorButtonType.BOOK_SELECTIONS, true));
		this.put(CFCASelectorButtonType.OCTAVOS, new CFCASelectorButton(this.cfcaTaskerIntf, CFCASelectorButtonType.OCTAVOS, true));
		this.put(CFCASelectorButtonType.COPIES, new CFCASelectorButton(this.cfcaTaskerIntf, CFCASelectorButtonType.COPIES, true));
		this.put(CFCASelectorButtonType.TABS, new CFCASelectorButton(this.cfcaTaskerIntf, CFCASelectorButtonType.TABS, true));
	}

	public void setEnabled(CFCASelectorButtonType type, boolean b) {this.get(type).setEnabled(b);}
}
