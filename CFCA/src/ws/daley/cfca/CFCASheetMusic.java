package ws.daley.cfca;

import static ws.daley.cfca.util.CFCAUtil.nameSplitter;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.impl.CFCAJFrame;
import ws.daley.cfca.panel.CFCAPanel;

public class CFCASheetMusic extends CFCAJFrame
{
	private static final long serialVersionUID = 1L;

	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCASheetMusic.class);

	private CFCAPanel taskerPanel = new CFCAPanel();

	public CFCASheetMusic(String title)
	{
		super(title);
		this.setName(this.getClass().getSimpleName());
		this.add(this.taskerPanel);
		pack();
		this.taskerPanel.setButtonState(this.taskerPanel.getDataPanel().jsonFileChooser.getButtonStates());
		if(log.isTraceEnabled())
			log.trace("Cconstructor complete");
	}

	public static void main(String[] args)
	{
		if(log.isTraceEnabled())
			log.trace(CFCASheetMusic.class.getName()+" entered");
		SwingUtilities.invokeLater(
			new Runnable()
			{
				@Override
				public void run()
				{
					CFCASheetMusic sheetMusicTasker = new CFCASheetMusic(nameSplitter(CFCASheetMusic.class.getSimpleName()));
					sheetMusicTasker.addWindowListener(sheetMusicTasker);
					if (log.isTraceEnabled())
						log.trace("Started");
					sheetMusicTasker.start();
				}
			}
		);
	}

	public static void displayBufferedImage(BufferedImage bufferedImage)
	{
	    JDialog dialog = new JDialog();
		dialog.setUndecorated(false);
		dialog.add(new JLabel(new ImageIcon(bufferedImage)));
		dialog.pack();
		dialog.setVisible(true);
		dialog.dispose();
	}
}
