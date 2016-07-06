package ws.daley.cfca;

public enum CFCAOutputType
{
	TABLET("tablet"),
	SIMPLEX("simplex"),
	DUPLEX("duplex"),
	TABS("tabs");

	private final String type;
	public String type() {return this.type;}

	CFCAOutputType(String type) {this.type = type;}
}
