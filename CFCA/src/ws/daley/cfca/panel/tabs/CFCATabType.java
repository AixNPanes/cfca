package ws.daley.cfca.panel.tabs;

public enum CFCATabType
{
	TAB5("5tab"),
	TAB8("8tab");

	private final String tabType;   // in kilograms
	public String tabType() { return this.tabType; }
	public int getTabs()
	{
		switch(this)
		{
			case TAB5:
				return 5;
			case TAB8:
				return 8;
		}
		return -1;
	}
	public int getSets()
	{
		switch(this)
		{
			case TAB5:
				return 5;
			case TAB8:
				return 3;
		}
		return -1;
	}
	public static CFCATabType[] getTypes() {return new CFCATabType[] {TAB5, TAB8};}

	CFCATabType(String tabType) {this.tabType = tabType;}
}
