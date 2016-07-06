package ws.daley.cfca.music;

import static ws.daley.cfca.util.CFCAUtil.*;

import ws.daley.cfca.CFCAMusicFormat;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class Music
{
	protected String name;
	public String getName() {return this.name;}

	protected CFCAMusicFormat format;
	public CFCAMusicFormat getFormat() {return this.format;}

	protected Book[] book;
	public Book[] getBook() {return this.book;}

	protected Book[] gluedBook;
	public Book[] getGluedBook() {return this.gluedBook;}
	
	protected int resolutionDPI = 200;
	public int getResolutionDPI() {return this.resolutionDPI;}

	protected int start = 1;
	public int getStart() {return this.start;}

	private int end;
	public int getEnd() {return this.end;}

	private int signature;
	public int getSignature() {return this.signature;}

	protected boolean duplex;
	public boolean getDuplex() {return this.duplex;}

	protected boolean twoPage;
	public boolean getTwoPage() {return this.twoPage | CFCAMusicFormat.COPY != this.format;}

	protected boolean landscape;
	public boolean getLandscape() {return this.landscape;}
	
	@Override
	public String toString()
	{
		String ret = this.name;
		if (this.format != null)
			ret += "("+this.format+")";
		if (BOOK.equals(this.format))
			for(int i = 0; i < this.book.length; i++)
				ret += "\n\t"+this.book[i].toString();
		if (GLUED_BOOK.equals(this.format))
			for(int i = 0; i < this.gluedBook.length; i++)
				ret += "\n\t"+this.gluedBook[i].toString();
		if (COPY.equals(this.format))
			ret += "\n\tstart page="+this.start;
		return ret;
	}

	public boolean isSelected(CFCAPanelIntf cfcaPanelIntf)
	{
		if (CFCAMusicFormat.BOOK.equals(this.format))
			return cfcaPanelIntf.isBookSelected() || cfcaPanelIntf.isSelectionSelected();
		if (CFCAMusicFormat.GLUED_BOOK.equals(this.format))
			return cfcaPanelIntf.isGluedBookSelected() || cfcaPanelIntf.isSelectionSelected();
		if (CFCAMusicFormat.OCTAVO.equals(this.format))
			return cfcaPanelIntf.isOctavoSelected();
		if (CFCAMusicFormat.COPY.equals(this.format))
			return cfcaPanelIntf.isCopySelected();
		return false;
	}
}
