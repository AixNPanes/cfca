package ws.daley.cfca.progressbar;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JProgressBar;

public abstract class CFCAFileProgressBar extends JProgressBar
{
	private static final long serialVersionUID = 1L;

	public abstract void start();

	protected File file;
	public ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	public CFCAFileProgressBar(File file)
	{
		this.file = file;
	}

	public File getFile() {return this.file;}

	public void addActionListener(ActionListener l) {this.listeners.add(l);}
}
