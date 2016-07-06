package ws.daley.cfca.panel;

import static ws.daley.cfca.util.CFCAUtil.PROGRESS_BAR_DONE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JPanel;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.button.CFCAButtonState;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntryList;
import ws.daley.cfca.buttonpanel.button.CFCAButtonType;
import ws.daley.cfca.datapanel.fileprogressbar.CFCAFileProgressBarPanel;
import ws.daley.cfca.datapanel.savepdfprogressbar.CFCASavePDFProgressBarPanel;
import ws.daley.cfca.progressbar.CFCAFileProgressBar;

public abstract class CFCAProgressBarSubPanel extends CFCADataSubPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCAProgressBarSubPanel.class);

	public abstract void setProgressBars();

	private static final CFCAButtonStateEntryList defaultButtonStateEntryList = new CFCAButtonStateEntryList(
		new CFCAButtonStateEntry[]
				{
					new CFCAButtonStateEntry(CFCAButtonType.OK, CFCAButtonState.INVISIBLE),
					new CFCAButtonStateEntry(CFCAButtonType.CANCEL, CFCAButtonState.FOCUS),
					new CFCAButtonStateEntry(CFCAButtonType.UP, CFCAButtonState.INVISIBLE),
					new CFCAButtonStateEntry(CFCAButtonType.DOWN, CFCAButtonState.INVISIBLE),
					new CFCAButtonStateEntry(CFCAButtonType.SAVE, CFCAButtonState.INVISIBLE),
					new CFCAButtonStateEntry(CFCAButtonType.SELECT, CFCAButtonState.INVISIBLE),
					new CFCAButtonStateEntry(CFCAButtonType.ALL, CFCAButtonState.INVISIBLE),
					new CFCAButtonStateEntry(CFCAButtonType.NONE, CFCAButtonState.INVISIBLE)
				}
	);

	protected JPanel panel = new JPanel();
	protected CountDownLatch allDone;
	protected ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	public List<CFCAFileProgressBar> progressBarList = new ArrayList<CFCAFileProgressBar>();
	protected CFCAFileProgressBarPanel<CFCAFileProgressBar> progressBars  = new CFCASavePDFProgressBarPanel();

	public CFCAProgressBarSubPanel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout layout)
	{
		this(cfcaTaskerIntf, layout, true);
	}

	public CFCAProgressBarSubPanel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout cfcaSpringLayout, boolean isDoubleBuffered)
	{
		super(cfcaTaskerIntf, cfcaSpringLayout, isDoubleBuffered);
		this.setLayout(new BorderLayout());
		this.add(this.panel, BorderLayout.CENTER);
		this.progressBars.setLayout(new GridLayout(0, 1));
		this.add(this.progressBars, BorderLayout.SOUTH);
		this.setEnabled(false);
		this.setVisible(false);
		if (log.isTraceEnabled())
			log.trace("set visible");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (log.isDebugEnabled())
			log.debug("actionPerformed "+this.getName());
		if (e.getActionCommand().equals(PROGRESS_BAR_DONE))
		{
			this.allDone.countDown();
			if (this.allDone.getCount() == 0)
				this.cfcaTaskerIntf.nextState();
		}
	}
	
	protected CFCAFileProgressBar updateProgressBar(CFCAFileProgressBar progressBar, int height, String string)
	{
		if (log.isDebugEnabled())
			log.debug("updateProgressBar "+progressBar.getName());
		this.progressBarList.add(progressBar);
		progressBar.setString(string);
		progressBar.setMinimumSize(new Dimension(900, height));
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.YELLOW);
		progressBar.addActionListener(this);
		progressBar.setVisible(true);
		progressBar.setEnabled(true);
		this.progressBars.add(progressBar);
		return progressBar;
	}

	public CFCAFileProgressBarPanel<CFCAFileProgressBar> getInputPDFProgressBars() {return this.progressBars;}
	@Override
	public CFCAButtonStateEntryList getButtonStateEntryList() {return defaultButtonStateEntryList;}
	@Override
	public void enablePanel() {setProgressBars();}
}