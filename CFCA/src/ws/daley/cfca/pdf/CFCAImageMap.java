package ws.daley.cfca.pdf;

import java.io.File;
import java.util.TreeMap;

import ws.daley.cfca.music.Music;
import ws.daley.cfca.panel.tabs.CFCATabType;

public class CFCAImageMap extends TreeMap<Integer, CFCABufferedImage>
{
	private static final long serialVersionUID = 1L;

	private File file;
	public File getFile() {return this.file;}

	private Music music;
	public Music getMusic() {return this.music;}

	private int startPage = -1;
	public int getStartPage() {return this.startPage;}

	private int endPage = 0;
	public int getEndPage() {return this.endPage;}
	
	private CFCATabType tabType = null;
	public CFCATabType getTabType() {return this.tabType;}
	public String getName()
	{
		if (this.tabType == null)
			return this.file.getName();
		return this.tabType.name();
	}

	public CFCAImageMap(CFCATabType tabType)
	{
		this.tabType = tabType;
	}

	public CFCAImageMap(File file, Music music)
	{
		this.file = file;
		this.music = music;
	}

	@Override
	public CFCABufferedImage put(Integer key, CFCABufferedImage value)
	{
		if (this.startPage == -1)
			this.startPage = key;
		if (key > this.endPage)
			this.endPage = key;
		return super.put(key, value);
	}
}
