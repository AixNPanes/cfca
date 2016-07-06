package ws.daley.cfca.buttonpanel.button;

import java.util.TreeMap;

public class CFCAButtonMap extends TreeMap<CFCAButtonType, CFCAButton>
{
	private static final long serialVersionUID = 1L;

	public CFCAButtonMap(CFCAButtonTypeList list)
	{
		for(CFCAButtonType type:list)
			this.put(type, new CFCAButton(type));
	}

	public CFCAButton getButton(CFCAButtonType buttonType) {return this.get(buttonType);}
}
