package ws.daley.cfca.chooser;

import java.io.File;
import java.util.TreeMap;

import ws.daley.cfca.Tuple;

public class CFCAChooserDirectory extends File
{
	private static final long serialVersionUID = 1L;

	private TreeMap<String, Tuple<String, File>> map = new TreeMap<String, Tuple<String, File>>();

	public CFCAChooserDirectory(File file)
	{
		super(file.toURI());
		for(File dirFile: this.listFiles())
		{
			String name = dirFile.getName();
			String lower = name.toLowerCase();
			this.map.put(lower, new Tuple<String, File>(name, dirFile));
		}
	}
	
	public Tuple<String, File> get(File file) {return this.map.get(file.getName().toLowerCase());}
}
