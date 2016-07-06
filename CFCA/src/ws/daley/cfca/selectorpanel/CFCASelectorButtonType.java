package ws.daley.cfca.selectorpanel;

public enum CFCASelectorButtonType
{
	BOOKS("Books"),
	GLUED_BOOKS("Glued Books"),
	BOOK_SELECTIONS("Selections Inside Books"),
	OCTAVOS("Octavos"),
	COPIES("Copies"),
	TABS("Indexes");

	private final String title;   // in kilograms
	public String title() { return this.title; }

	CFCASelectorButtonType(String title) {this.title = title;}
}
