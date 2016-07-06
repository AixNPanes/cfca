package ws.daley.cfca.panel;

import java.awt.Component;

import javax.swing.JPanel;

import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntryList;

public abstract class CFCADataSubPanel extends JPanel implements CFCANextStateValidatorIntf
{
	private static final long serialVersionUID = 1L;

	public abstract void enablePanel();
	public abstract CFCAButtonStateEntryList getButtonStateEntryList();

	protected CFCAPanelIntf cfcaTaskerIntf;
	protected CFCASpringLayout cfcaSpringLayout;
//	private CFCADataSubPanelType cfcaDataSubPanelType;
	protected CFCAButtonStateEntryList cfcaButtonStateEntryList;

	public CFCADataSubPanel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout cfcaSpringLayout, boolean isDoubleBuffered)
	{
		super(cfcaSpringLayout, isDoubleBuffered);
		this.cfcaTaskerIntf = cfcaTaskerIntf;
//		this.cfcaDataSubPanelType = cfcaDataSubPanelType;
		this.cfcaSpringLayout = cfcaSpringLayout;
		this.setName(this.getClass().getSimpleName());
	}

	public void disablePanel() {}
//	public void setTaskerPanel(CFCAPanelIntf cfcaTaskerIntf) {this.cfcaTaskerIntf = cfcaTaskerIntf;}
	@Override
	public boolean isPanelValid() {return true;}
	@Override
	public Component add(Component comp) {return this.add(comp, -1);}
	@Override
	public void add(Component comp, Object constraints) {this.add(comp, constraints, -1);}
}
