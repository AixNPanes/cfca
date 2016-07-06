package ws.daley.cfca.pdf;

import java.util.TreeMap;

import ws.daley.cfca.music.MusicCollectionFormat;

public class CFCADocumentMap extends TreeMap<String, CFCAImageMap>
{
	private static final long serialVersionUID = -7925971974608207549L;

	private MusicCollectionFormat documentFormat;
	public MusicCollectionFormat getDocumentFormat() {return this.documentFormat;}

	public CFCADocumentMap(MusicCollectionFormat documentFormat) {this.documentFormat = documentFormat;}
}
