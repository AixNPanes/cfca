package ws.daley.cfca.datapanel.savedirectorypanel;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CFCASaveTextField extends JTextField
{
	private static final long serialVersionUID = 1L;

	public CFCASaveTextField(String title)
	{
		super(title);
		setHorizontalAlignment(SwingConstants.LEFT);
		setVisible(true);
	}
}
