package ws.daley.cfca.buttonpanel.button;

import java.util.ArrayList;
import java.util.Arrays;

public class CFCAButtonStateEntryList extends ArrayList<CFCAButtonStateEntry>
{
	private static final long serialVersionUID = 1L;
	
	public CFCAButtonStateEntryList(CFCAButtonStateEntry[] entry) {this.addAll(Arrays.asList(entry));}
}
