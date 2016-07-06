package ws.daley.cfca.datapanel.taborder;

import static ws.daley.cfca.util.CFCAUtil.setAllSize;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.CFCAButtonPanelButtonState;
import ws.daley.cfca.buttonpanel.button.CFCAButtonMap;
import ws.daley.cfca.buttonpanel.button.CFCAButtonState;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntryList;
import ws.daley.cfca.buttonpanel.button.CFCAButtonType;
import ws.daley.cfca.panel.CFCADataSubPanel;
import ws.daley.cfca.panel.CFCAPanelIntf;

public class CFCATabOrderPanel extends CFCADataSubPanel implements ListSelectionListener, KeyListener
{
	private static final long serialVersionUID = 1L;

	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCATabOrderPanel.class);

	private static final CFCAButtonStateEntryList defaultButtonStateEntryList = new CFCAButtonStateEntryList(
			new CFCAButtonStateEntry[]
			{
				new CFCAButtonStateEntry(CFCAButtonType.OK, CFCAButtonState.FOCUS),
				new CFCAButtonStateEntry(CFCAButtonType.CANCEL, CFCAButtonState.ENABLED),
				new CFCAButtonStateEntry(CFCAButtonType.UP, CFCAButtonState.VISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.DOWN, CFCAButtonState.VISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.SAVE, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.SELECT, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.ALL, CFCAButtonState.INVISIBLE),
				new CFCAButtonStateEntry(CFCAButtonType.NONE, CFCAButtonState.INVISIBLE)
			}
		);

	private JList<String> jList;
	private String[] jListData;

	public CFCATabOrderPanel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout cfcaSpringLayout)
	{
		super(cfcaTaskerIntf, cfcaSpringLayout, true);
		this.setjList(new JList<String>(new String[]{}));
		this.getjList().setVisible(true);
		this.getjList().setEnabled(true);
		this.getjList().addListSelectionListener(this);
		this.add(this.getjList());
		this.cfcaButtonStateEntryList = defaultButtonStateEntryList;
		this.jList.addKeyListener(this);
		if (log.isTraceEnabled())
			log.trace("set visible");
	}

	public void showOrderPanel()
	{
		this.cfcaTaskerIntf.getButtonPanel().setButtonState(CFCAButtonPanelButtonState.ORDER_PANEL);
		
		File[] files = this.cfcaTaskerIntf.getPDFInputFiles();
		this.setjListData(new String[files.length]);
		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];
			this.getjListData()[i] = file.getAbsolutePath();
			this.cfcaTaskerIntf.getFileMap().put(file.getAbsolutePath(), file);
		}
		this.getjList().setListData(this.getjListData());
		if (this.getjListData().length > 0)
			this.getjList().setSelectedIndex(0);
		else
			this.cfcaTaskerIntf.getButtonPanel().setDownButtonEnabled(false);
		this.cfcaTaskerIntf.getButtonPanel().setUpButtonEnabled(false);
		this.getjList().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (log.isDebugEnabled())
			log.debug("showOrderPanel");
	}

	@Override
    public void valueChanged(ListSelectionEvent e)
    {
		if (e.getSource().equals(this.getjList()))
			setEnabledButton(this.getjList().getSelectedIndex());
	}
	
	public void setEnabledButton(int i)
	{
		CFCAButtonMap buttonMap = this.cfcaTaskerIntf.getButtonPanel().buttonMap;
		buttonMap.getButton(CFCAButtonType.UP).setEnabled(i != 0);
		buttonMap.getButton(CFCAButtonType.DOWN).setEnabled(i != (this.getjListData().length - 1));
	}

	@Override
    public void enablePanel()
    {
		showOrderPanel();
		setAllSize(this.getjList(), new Dimension(this.getWidth()-5, this.getHeight()-5));
		this.cfcaTaskerIntf.setFocusOKButton();
    }

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.isAltDown())
		{
			switch (e.getKeyCode())
			{
				case KeyEvent.VK_UP:
					this.cfcaTaskerIntf.getButtonPanel().buttonMap.get(CFCAButtonType.UP).doClick();
					e.consume();
					break;
				case KeyEvent.VK_DOWN:
					this.cfcaTaskerIntf.getButtonPanel().buttonMap.get(CFCAButtonType.DOWN).doClick();
					e.consume();
					break;
			}
		}
	}

	private void setjListData(String[] jListData) {this.jListData = jListData;}
	private void setjList(JList<String> jList) {this.jList = jList;}
	public String[] getjListData() {return this.jListData;}
	public JList<String> getjList() {return this.jList;}
	@Override
	public CFCAButtonStateEntryList getButtonStateEntryList() {return defaultButtonStateEntryList;}
	@Override
    public boolean isPanelValid() {return this.cfcaTaskerIntf.getPDFInputFiles().length > 1;}
	@Override
	public void keyTyped(@SuppressWarnings("unused") KeyEvent e) {}
	@Override
	public void keyReleased(@SuppressWarnings("unused") KeyEvent e) {}
}
