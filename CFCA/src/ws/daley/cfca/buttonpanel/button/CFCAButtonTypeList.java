package ws.daley.cfca.buttonpanel.button;

import java.util.ArrayList;

public class CFCAButtonTypeList extends ArrayList<CFCAButtonType>
{
	private static final long serialVersionUID = 1L;

	public CFCAButtonTypeList()
	{
		this.add(CFCAButtonType.OK);
		this.add(CFCAButtonType.CANCEL);
		this.add(CFCAButtonType.UP);
		this.add(CFCAButtonType.DOWN);
		this.add(CFCAButtonType.SAVE);
		this.add(CFCAButtonType.SELECT);
		this.add(CFCAButtonType.ALL);
		this.add(CFCAButtonType.NONE);
	}
}
