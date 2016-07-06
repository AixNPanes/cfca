package ws.daley.cfca.buttonpanel;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ws.daley.cfca.buttonpanel.button.CFCAButton;
import ws.daley.cfca.buttonpanel.button.CFCAButtonMap;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.buttonpanel.button.CFCAButtonType;
import ws.daley.cfca.buttonpanel.button.CFCAButtonTypeList;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCAButtonPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public CFCAButtonTypeList buttonTypeList = new CFCAButtonTypeList();
	public CFCAButtonMap buttonMap = new CFCAButtonMap(this.buttonTypeList);

	private CFCAPanelIntf cfcaPanelIntf;
	public CFCAPanelIntf getPanelIntf() {return this.cfcaPanelIntf;}

	public CFCAButtonPanel(CFCAPanelIntf cfcaPanelIntf)
	{
		this.cfcaPanelIntf = cfcaPanelIntf;
		this.setName(this.getClass().getSimpleName());

		for(CFCAButtonType buttonType:this.buttonTypeList)
		{
			CFCAButton button = new CFCAButton(buttonType);
			this.buttonMap.put(buttonType, button);
			button.addToPanel(this);
		}

		this.setEnabled(false);
		this.setVisible(false);
	}

	@Override
	public void setEnabled(boolean b)
	{
		for(Component c:this.getComponents())
			if (c instanceof JButton)
				c.setEnabled(b);
		this.buttonMap.get(CFCAButtonType.CANCEL).setEnabled(true);
	}

	public void setButtonState(CFCAButtonStateEntry[] buttonStates)
	{
		for(JButton jButton:this.buttonMap.values())
		{
			jButton.setEnabled(false);
			jButton.setVisible(false);
		}
		for(CFCAButtonStateEntry state:buttonStates)
		{
			JButton jButton = this.buttonMap.get(state.getButtonType());
			switch(state.getState())
			{
				case INVISIBLE:
					jButton.setVisible(false);
					jButton.setEnabled(false);
					break;
				case VISIBLE:
					jButton.setVisible(true);
					jButton.setEnabled(false);
					break;
				case ENABLED:
					jButton.setVisible(true);
					jButton.setEnabled(true);
					break;
				case FOCUS:
					jButton.setVisible(true);
					jButton.setEnabled(true);
					getFrame().getRootPane().setDefaultButton(jButton);
					break;
			}
		}
	}

	private JFrame getFrame()
	{
		Container container = this.getParent();
		while (!(container instanceof JFrame))
			container = container.getParent();
		return (JFrame) container;
	}

	public void setButtonState(CFCAButtonPanelButtonState buttonState)
	{
		switch(buttonState)
		{
			case SELECTOR_PANEL:
				this.buttonMap.get(CFCAButtonType.OK).setVisible(true);
				break;
			case ORDER_PANEL:
				this.buttonMap.get(CFCAButtonType.SELECT).setVisible(false);
				this.buttonMap.get(CFCAButtonType.UP).setVisible(true);
				this.buttonMap.get(CFCAButtonType.DOWN).setVisible(true);
				this.buttonMap.get(CFCAButtonType.OK).setVisible(true);
				break;
			case NONE:
				break;
		}
	}

	public void setSavePDFsButtonEnabled(boolean b) {this.buttonMap.get(CFCAButtonType.SAVE).setEnabled(b);this.setSavePDFsButtonVisible(b);}
	public void setSavePDFsButtonVisible(boolean b) {this.buttonMap.get(CFCAButtonType.SAVE).setVisible(b);}
	public void setUpButtonEnabled(boolean b) {this.buttonMap.get(CFCAButtonType.UP).setEnabled(b);}
	public void setDownButtonEnabled(boolean b) {this.buttonMap.get(CFCAButtonType.DOWN).setEnabled(b);}
	@Override
	public void setVisible(@SuppressWarnings("unused") boolean b) {this.buttonMap.get(CFCAButtonType.CANCEL).setVisible(true);}
}
