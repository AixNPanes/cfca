package ws.daley.cfca.chooser;

public enum CFCAFileSelectionMode
{
	FILES_ONLY (0),
	DIRECTORIES_ONLY (1),
	FILES_AND_DIRECTORIES (2);
	
	private final int modeValue;
	public int mode() {return this.modeValue;}
	
	CFCAFileSelectionMode(int modeValue) {this.modeValue = modeValue;}
}
