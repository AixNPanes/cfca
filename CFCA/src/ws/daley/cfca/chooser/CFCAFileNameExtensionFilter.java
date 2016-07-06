package ws.daley.cfca.chooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CFCAFileNameExtensionFilter extends FileFilter
{
	private FileNameExtensionFilter fileNameExtensionFilter = null;
	
	public CFCAFileNameExtensionFilter(String description, String... extensions)
	{
		this.fileNameExtensionFilter = new FileNameExtensionFilter(description, extensions);
	}

	@Override
	public boolean accept(File f) {return this.fileNameExtensionFilter.accept(f);}
	@Override
	public String getDescription() {return this.fileNameExtensionFilter.getDescription();}
    @Override
	public String toString() {return this.fileNameExtensionFilter.toString();}
}
