package ws.daley.cfca.chooser;

import static ws.daley.cfca.util.CFCAUtil.APPROVE_SELECTION;
import static ws.daley.cfca.util.CFCAUtil.CANCEL_SELECTION;
import static ws.daley.cfca.util.CFCAUtil.cancel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCASpringLayout;
import ws.daley.cfca.buttonpanel.button.CFCAButtonStateEntry;
import ws.daley.cfca.panel.CFCADataSubPanel;
import ws.daley.cfca.panel.CFCAPanelIntf;

public abstract class CFCAFileChooserPanel extends CFCADataSubPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCAFileChooserPanel.class);

	protected CFCAFileChooserLabel label;
	protected CFCAFileChooser fileChooser;
	private String labelTitle;

	public CFCAFileChooserPanel(CFCAPanelIntf cfcaTaskerIntf, CFCASpringLayout cfcaSpringLayout, CFCAFileChooserTitle fileChooserTitle, FileFilter filter, int model, boolean multiSelection)
	{
		super(cfcaTaskerIntf, cfcaSpringLayout, true);
		this.setName(this.getClass().getSimpleName());
		this.setLayout(cfcaSpringLayout);
		this.labelTitle = fileChooserTitle.getTitle();

		this.label = new CFCAFileChooserLabel(this.labelTitle);
		cfcaSpringLayout.addToPanel(this, this.label, this, null, true, false, true);

		this.fileChooser = new CFCAFileChooser(filter, model, multiSelection);
		cfcaSpringLayout.addToPanel(this, this.fileChooser, this.label, null, true, false, true);
		this.fileChooser.addActionListener(this);

		this.setVisible(false);
		this.setEnabled(false);
		this.addActionListener(this);
		this.cfcaButtonStateEntryList = getButtonStateEntryList();
		if (log.isTraceEnabled())
			log.trace("set visible");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (log.isDebugEnabled())
			log.debug("actionPerformed "+e.getSource().getClass());
		if (e.getSource() == this.getFileChooser())
			switch(e.getActionCommand())
			{
				case CANCEL_SELECTION:
					cancel(this);
					break;
				case APPROVE_SELECTION:
					this.cfcaTaskerIntf.nextState();
			}
	}

	public CFCAButtonStateEntry[] getButtonStates()
	{
		return this.cfcaButtonStateEntryList.stream().toArray(CFCAButtonStateEntry[]::new);
	}

	public File getSelectedFile() {return this.fileChooser.getSelectedFile();}
	public File[] getSelectedFiles() {return this.fileChooser.getAllSelectedFiles();}
	public JFileChooser getFileChooser() {return this.fileChooser;}
	public void addActionListener(ActionListener l) {this.listenerList.add(ActionListener.class, l);}
	public void enablePanel(File file) {this.fileChooser.setCurrentDirectory(file);}
	@Override
	public boolean isPanelValid() {return true;}
	@Override
	public void disablePanel() {}
}