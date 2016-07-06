package ws.daley.cfca.buttonpanel.button;

import static ws.daley.cfca.util.CFCAUtil.cancel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ws.daley.cfca.buttonpanel.CFCAButtonPanel;
import ws.daley.cfca.datapanel.taborder.CFCATabOrderPanel;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCAButton extends JButton implements ActionListener
{
	private static final long serialVersionUID = 1L;

	protected CFCAPanelIntf cfcaPanelIntf;
	protected CFCAButtonType buttonType;
	public CFCAButtonType getButtonType() {return this.buttonType;}

	public CFCAButton(CFCAButtonType buttonType)
	{
		this.buttonType = buttonType;
		this.setText(this.buttonType.buttonType());
		this.setEnabled(true);
		this.setVisible(true);
	}

	public void addToPanel(CFCAButtonPanel buttonPanel)
	{
		buttonPanel.add(this);
		this.cfcaPanelIntf = buttonPanel.getPanelIntf();
		this.addActionListener(this);
	}

	public void moveTabEntry(boolean up)	// For Up & Down buttons
	{
		CFCATabOrderPanel tabOrderPanel = this.cfcaPanelIntf.getDataPanel().tabOrderPanel;
		int i = tabOrderPanel.getjList().getSelectedIndex();
		int i1 = i+(up?-1:1);
		String save = tabOrderPanel.getjListData()[i];
		tabOrderPanel.getjListData()[i] = tabOrderPanel.getjListData()[i1];
		tabOrderPanel.getjListData()[i1] = save;
		tabOrderPanel.getjList().setListData(tabOrderPanel.getjListData());
		tabOrderPanel.setEnabledButton(i);
		tabOrderPanel.getjList().setSelectedIndex(i1);
	}
	
	private void selectEntries(boolean select) // for Select All & Clear All buttons
	{
		this.cfcaPanelIntf.getDataPanel().selectorPanel.selectEntries(select);
	}

	@Override
	public void actionPerformed(@SuppressWarnings("unused") ActionEvent e)
	{
		switch (this.buttonType)
		{
			case OK:
			case SAVE:
				this.cfcaPanelIntf.nextState();
				break;
			case CANCEL:
			case SELECT:
				cancel(this);
				break;
			case DOWN:
				moveTabEntry(false);
				break;
			case UP:
				moveTabEntry(true);
				break;
			case ALL:
				selectEntries(true);
				break;
			case NONE:
				selectEntries(false);
				break;
		}
	}
}
