package ws.daley.cfca.selectorpanel;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCASelectorButton extends JCheckBox implements ItemListener
{
	private static final long serialVersionUID = 1L;

	private CFCAPanelIntf cfcaTaskerIntf;

	public CFCASelectorButton(CFCAPanelIntf cfcaTaskerIntf, CFCASelectorButtonType selectorButtonType, boolean b)
	{
		super(selectorButtonType.title(), b);
		this.cfcaTaskerIntf = cfcaTaskerIntf;
		this.addItemListener(this);
	}

	@Override
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		super.setSelected(b);
	}

	@Override
	public void itemStateChanged(@SuppressWarnings("unused") ItemEvent e)
	{
		if (this.cfcaTaskerIntf.getSelectorPanel() == null)
			return;
		boolean state = this.cfcaTaskerIntf.getSelectorPanel().hasBoxesChecked();
		this.cfcaTaskerIntf.setOKButtonState(state);
		if (state)
			this.cfcaTaskerIntf.setFocusOKButton();
		else
			this.cfcaTaskerIntf.setFocusCancelButton();
			
	}
}
