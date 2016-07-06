package ws.daley.cfca.datapanel.fileprogressbar;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import ws.daley.cfca.progressbar.CFCAFileProgressBar;

public class CFCAFileProgressBarPanel<T extends CFCAFileProgressBar> extends JPanel
{
	private static final long serialVersionUID = 1L;

	public CFCAFileProgressBarPanel() {this.setLayout(new FlowLayout());}

	public void setList(T[] array)
	{
		for(T element:array)
			this.add(element);
	}
}
