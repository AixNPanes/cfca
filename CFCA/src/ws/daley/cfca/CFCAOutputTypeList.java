package ws.daley.cfca;

import java.util.ArrayList;

public class CFCAOutputTypeList extends ArrayList<CFCAOutputType>
{
	private static final long serialVersionUID = 1L;
	
	public static final CFCAOutputTypeList cfcaOutputTypes = new CFCAOutputTypeList(
			new CFCAOutputType[] {CFCAOutputType.SIMPLEX, CFCAOutputType.DUPLEX, CFCAOutputType.TABLET, CFCAOutputType.TABS});
	
	public static final CFCAOutputTypeList cfcaNonTabOutputTypes = new CFCAOutputTypeList(
			new CFCAOutputType[] {CFCAOutputType.SIMPLEX, CFCAOutputType.DUPLEX, CFCAOutputType.TABLET});

	public CFCAOutputTypeList(CFCAOutputType[] types)
	{
		for(CFCAOutputType type:types)
			this.add(type);
	}
}
