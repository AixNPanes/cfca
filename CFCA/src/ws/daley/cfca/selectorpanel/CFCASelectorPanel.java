package ws.daley.cfca.selectorpanel;

import java.awt.Component;
import java.util.Map.Entry;

import javax.swing.JCheckBox;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.button.CFCAButtonState;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntryList;
import ws.daley.cfca.buttonpanel.button.CFCAButtonType;
import ws.daley.cfca.music.MusicCollectionFormat;
import ws.daley.cfca.panel.CFCADataSubPanel;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCASelectorPanel extends CFCADataSubPanel
{
	private static final long serialVersionUID = 1L;
	
	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCASelectorPanel.class);

	private static final CFCAButtonStateEntryList defaultButtonStateEntryList = new CFCAButtonStateEntryList(
			new CFCAButtonStateEntry[]
			{
				new CFCAButtonStateEntry(CFCAButtonType.OK, CFCAButtonState.FOCUS),
				new CFCAButtonStateEntry(CFCAButtonType.CANCEL, CFCAButtonState.ENABLED),
				new CFCAButtonStateEntry(CFCAButtonType.UP, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.DOWN, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.SAVE, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.SELECT, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.ALL, CFCAButtonState.ENABLED),
				new CFCAButtonStateEntry(CFCAButtonType.NONE, CFCAButtonState.ENABLED)
			}
		);

	private CFCASelectorButtonList cfcaSelectorButtonList;

	public CFCASelectorPanel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout cfcaSpringLayout)
	{
		super(cfcaTaskerIntf, cfcaSpringLayout, true);
		
		this.cfcaSelectorButtonList = new CFCASelectorButtonList(this.cfcaTaskerIntf);
		Component previous = this;
		for(CFCASelectorButton button: this.cfcaSelectorButtonList.values())
		{
			button.setEnabled(true);
			cfcaSpringLayout.addToPanel(this, button, previous, null, true, false, true);
			previous = button;
		}
		this.cfcaButtonStateEntryList = defaultButtonStateEntryList;
		if (log.isTraceEnabled())
			log.trace("set visible");
	}

	private void enableButtons()
	{
		MusicCollectionFormat format = this.cfcaTaskerIntf.getMusicCollectionFormat();
		this.cfcaSelectorButtonList.setEnabled(CFCASelectorButtonType.BOOKS, format.hasTypes(CFCASelectorButtonType.BOOKS));
		this.cfcaSelectorButtonList.setEnabled(CFCASelectorButtonType.GLUED_BOOKS, format.hasTypes(CFCASelectorButtonType.GLUED_BOOKS));
		this.cfcaSelectorButtonList.setEnabled(CFCASelectorButtonType.BOOK_SELECTIONS, format.hasTypes(CFCASelectorButtonType.BOOK_SELECTIONS));
		this.cfcaSelectorButtonList.setEnabled(CFCASelectorButtonType.OCTAVOS, format.hasTypes(CFCASelectorButtonType.OCTAVOS));
		this.cfcaSelectorButtonList.setEnabled(CFCASelectorButtonType.COPIES, format.hasTypes(CFCASelectorButtonType.COPIES));
		this.cfcaSelectorButtonList.setEnabled(CFCASelectorButtonType.TABS, format.hasTypes(new CFCASelectorButtonType[] {
				CFCASelectorButtonType.GLUED_BOOKS,
				CFCASelectorButtonType.BOOKS,
				CFCASelectorButtonType.OCTAVOS,
				CFCASelectorButtonType.COPIES
		}));
	}

	public void selectEntries(boolean select)
	{
		for(Entry<CFCASelectorButtonType, CFCASelectorButton> entry:this.cfcaSelectorButtonList.entrySet())
			entry.getValue().setSelected(select);
	}

	public boolean hasBoxesChecked()
	{
		for(JCheckBox checkBox:this.cfcaSelectorButtonList.values())
			if (checkBox.isSelected())
				return true;
		return false;
	}

	public boolean getEntry(CFCASelectorButtonType type) {return this.cfcaSelectorButtonList.get(type).isSelected();}
	@Override
	public void enablePanel() {enableButtons();}
	@Override
	public CFCAButtonStateEntryList getButtonStateEntryList() {return defaultButtonStateEntryList;}
}
