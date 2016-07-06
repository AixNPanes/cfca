package ws.daley.cfca.panel;

public class CFCAStateChange
{
	private CFCADataSubPanel panel;
	public CFCADataSubPanel getPanel() {return this.panel;}

	private CFCANextStateValidatorIntf nextStateValid;
	public CFCANextStateValidatorIntf getNextStateValid() {return this.nextStateValid;}

	public CFCAStateChange(CFCADataSubPanel panel, CFCANextStateValidatorIntf nextStateValid)
	{
		this.panel = panel;
		this.nextStateValid = nextStateValid;
	}
}
