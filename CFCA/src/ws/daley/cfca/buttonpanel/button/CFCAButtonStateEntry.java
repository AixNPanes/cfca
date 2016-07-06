package ws.daley.cfca.buttonpanel.button;

public class CFCAButtonStateEntry
{
	private CFCAButtonType buttonType;
	public CFCAButtonType getButtonType() {return this.buttonType;}

	private CFCAButtonState state;
	public CFCAButtonState getState() {return this.state;}
	
	public CFCAButtonStateEntry(CFCAButtonType buttonType, CFCAButtonState state)
	{
		this.buttonType = buttonType;
		this.state = state;
	}
}
