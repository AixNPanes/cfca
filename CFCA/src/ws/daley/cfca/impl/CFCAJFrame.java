package ws.daley.cfca.impl;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import ws.daley.cfca.CFCAChunk;

public abstract class CFCAJFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;

	protected SwingWorker<Boolean, CFCAChunk> worker;

	public CFCAJFrame(String title)
	{
		super(title);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void start()
	{
		this.worker = new SwingWorker<Boolean, CFCAChunk>()
		{
			@Override
			protected Boolean doInBackground() throws Exception {return true;}

			@Override
			protected void done()
			{
				try {get();}
				catch (InterruptedException | ExecutionException | CancellationException e) {}
			}

			@Override
			protected void process(@SuppressWarnings("unused") List<CFCAChunk> chunks) {}
		};

		this.worker.execute();
	}

	@Override
	public void windowClosed(@SuppressWarnings("unused") WindowEvent e) {this.worker.cancel(true);}
	@Override
	public void windowOpened(@SuppressWarnings("unused") WindowEvent e) {}
	@Override
	public void windowClosing(@SuppressWarnings("unused") WindowEvent e) {}
	@Override
	public void windowIconified(@SuppressWarnings("unused") WindowEvent e) {}
	@Override
	public void windowDeiconified(@SuppressWarnings("unused") WindowEvent e) {}
	@Override
	public void windowActivated(@SuppressWarnings("unused") WindowEvent e) {}
	@Override
	public void windowDeactivated(@SuppressWarnings("unused") WindowEvent e) {}
}
