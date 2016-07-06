package ws.daley.cfca.buttonpanel.button;

public enum CFCAButtonType
{
	OK("OK"),
	CANCEL("Cancel"),
	UP("Move Up"),
	DOWN("Move Down"),
	SAVE("Save PDFs"),
	SELECT("Select PDF Files"),
	ALL("Select all"),
	NONE("Clear all");

	private final String buttonType;   // in kilograms
	public String buttonType() { return this.buttonType; }

	CFCAButtonType(String buttonType) {this.buttonType = buttonType;}
}
