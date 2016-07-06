package ws.daley.cfca.chooser;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class CFCAFileChooserLabel extends JLabel
{
	private static final long serialVersionUID = 1L;

	public CFCAFileChooserLabel(String title)
	{
		super(title);
		setName("CFCA FileChooser");
		setHorizontalAlignment(SwingConstants.CENTER);
	}
}
