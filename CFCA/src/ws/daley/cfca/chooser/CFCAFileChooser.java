package ws.daley.cfca.chooser;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class CFCAFileChooser extends JFileChooser
{
	private static final long serialVersionUID = 1L;

	public CFCAFileChooser(FileFilter filter, int model, boolean multiSelection)
	{
		setName(super.getClass().getSimpleName());
		setFileFilter(filter);
		setFileSelectionMode(model);
		setMultiSelectionEnabled(multiSelection);
	}

	protected File[] getAllSelectedFiles()
	{
		List<File> selectedFiles = new ArrayList<File>();
		selectedFiles.addAll(Arrays.asList(getSelectedFiles()));
		File selectedFile = super.getSelectedFile();
		if (selectedFile != null && !selectedFiles.contains(selectedFile))
			selectedFiles.add(selectedFile);
		return selectedFiles.stream().toArray(File[]::new);
	}

	@Override
	public File getSelectedFile()
	{
		File[] selectedFiles = getAllSelectedFiles();
		switch(selectedFiles.length)
		{
			case 0:
				return null;
			case 1:
				return selectedFiles[0];
			default:
				throw new RuntimeException();
		}
	}

	@Override
	protected void fireActionPerformed(final String command) {fireActionPerformed(this, command);}

	protected void fireActionPerformed(Component c, final String command)
	{
		Object[] listeners = this.listenerList.getListenerList();
		long mostRecentEventTime = EventQueue.getMostRecentEventTime();
		AWTEvent currentEvent = EventQueue.getCurrentEvent();
		int modifiers = currentEvent instanceof InputEvent?
				((InputEvent)currentEvent).getModifiers():
					currentEvent instanceof ActionEvent?((ActionEvent)currentEvent).getModifiers():0;
		ActionEvent e = null;
		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==ActionListener.class)
			{
				if (e == null)
				{
					e = new ActionEvent(c, ActionEvent.ACTION_PERFORMED,
						command, mostRecentEventTime,
						modifiers);
				}
				((ActionListener)listeners[i+1]).actionPerformed(e);
			}
		}
	}
}
