package ws.daley.cfca.datapanel.savedirectorypanel;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

class CFCASaveTextLabel extends JLabel
{
	private static final long serialVersionUID = 1L;

	public CFCASaveTextLabel(String text)
	{
		super(text, TRAILING);
		setHorizontalAlignment(SwingConstants.RIGHT);
		setVisible(true);
	}
}