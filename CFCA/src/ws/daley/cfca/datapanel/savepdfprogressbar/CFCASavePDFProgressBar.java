package ws.daley.cfca.datapanel.savepdfprogressbar;

import static ws.daley.cfca.util.CFCAUtil.BLANK;
import static ws.daley.cfca.util.CFCAUtil.DEFAULT_PAGE_SCALE;
import static ws.daley.cfca.util.CFCAUtil.EMPTY;
import static ws.daley.cfca.util.CFCAUtil.LETTER_UPPER_RIGHT_Y_PICAS;
import static ws.daley.cfca.util.CFCAUtil.LETTER_WIDTH_PICAS;
import static ws.daley.cfca.util.CFCAUtil.PROGRESS_BAR_DONE;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.CFCAOutputType;
import ws.daley.cfca.music.Music;
import ws.daley.cfca.pdf.CFCABufferedImage;
import ws.daley.cfca.pdf.CFCAImageMap;
import ws.daley.cfca.progressbar.CFCAFileProgressBar;

public class CFCASavePDFProgressBar extends CFCAFileProgressBar
{
	private static final long serialVersionUID = 1L;

	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCASavePDFProgressBar.class);

	static List<String> wordsToLines(String[] words, PDFont font, int scaledFontWidth) throws IOException
	{
		List<String> lines = new ArrayList<String>();
		String line = EMPTY;
		for(String word:words)
		{
			String newLine = line + (line.length() == 0?"":BLANK) + word;
			float stringWidth;
			try {stringWidth = font.getStringWidth(newLine);}
			catch(NullPointerException e1)
			{
				// sometimes the font.getStringWidth fails with NPE on a HashMap.get for no apparent reason
				// Just wait 1 second and try it again
				synchronized(newLine)
				{
					try{newLine.wait(1000);}
					catch(InterruptedException e)
					{
						throw new RuntimeException(e);
					}
				}
				try {stringWidth = font.getStringWidth(newLine);}
				catch(NullPointerException e2)
				{
					stringWidth = font.getStringWidth(newLine);
				}
			}
			if (stringWidth <= scaledFontWidth)
				line = newLine;
			else
			{
				lines.add(line);
				line = word;
			}
		}
		if (line.length() > 0)
			lines.add(line);
		return lines;
	}

	class PDFGenerationException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;

		private int error;
		public int getError() {return this.error;}

		public PDFGenerationException(int error, Throwable t)
		{
			super(t);
			this.error = error;
		}
	}

	public CFCAImageMap getTaskerImageMap() {return this.taskerImageMap;}
	public void setTaskerImageMap(CFCAImageMap taskerImageMap) {this.taskerImageMap = taskerImageMap;}
	CFCAOutputType type;
	Border border;

	CFCAImageMap taskerImageMap;
	SwingWorker<Boolean, String[]> worker;

	public CFCASavePDFProgressBar(CFCAOutputType type, File file)
	{
		super(file);
		this.type = type;
		this.border = BorderFactory.createLineBorder(new Color(240, 240, 240), 4);
		this.setBorder(this.border);
	}

	@Override
	public void start()
	{
		this.worker = new SwingWorker<Boolean, String[]>()
		{
			private String name;
			private int startPage;
			private int endPage;

			@Override
			protected Boolean doInBackground() throws Exception
			{
				try
				{
					this.name = CFCASavePDFProgressBar.this.getFile().getName();
					this.startPage = CFCASavePDFProgressBar.this.taskerImageMap.getStartPage();
					this.endPage = CFCASavePDFProgressBar.this.taskerImageMap.getEndPage();
					writeDocument();
				}
				catch(Throwable t)
				{
					t.printStackTrace();
					throw new RuntimeException(t);
				}
				return true;
			}

			protected void publish(Integer i)
			{
				String message;
				if (i == -1)
				{
					CFCASavePDFProgressBar.this.border = BorderFactory.createLineBorder(Color.RED, 4);
					message = "Error Adding Document Page";
					RuntimeException e = new RuntimeException();
					log.error(message, e);
					throw e;
				}
				else if (i == -2)
				{
					CFCASavePDFProgressBar.this.border = BorderFactory.createLineBorder(Color.RED, 4);
					message = "Error Writing Document";
					(new RuntimeException(message)).printStackTrace();
				}
				else if (i > this.endPage)
				{
					CFCASavePDFProgressBar.this.border = BorderFactory.createLineBorder(Color.GREEN, 4);
					message = "Writing complete";
				}
				else if (i == this.endPage)
					message = "Writing file";
				else
				{
					message = String.format("%s - Creating page %3d of %3d pages", this.name, i, this.endPage);
					if (log.isDebugEnabled())
						log.debug(message);
				}
				CFCASavePDFProgressBar.this.setBorder(CFCASavePDFProgressBar.this.border);
				publish(new String[] {Integer.toString(i), CFCASavePDFProgressBar.this.type.type() + "-" + this.name + ", " + message});
			}

			@Override
			protected void done()
			{
				@SuppressWarnings("unused")
				boolean status;
				try {status = get();}
				catch (InterruptedException | ExecutionException | CancellationException e) {}
				ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, PROGRESS_BAR_DONE);
				for(ActionListener l:CFCASavePDFProgressBar.this.listeners)
					l.actionPerformed(e);
			}

			@Override
			protected void process(List<String[]> chunks)
			{
				String[] mostRecentValue = chunks.get(chunks.size() - 1);
				int page = Integer.parseInt(mostRecentValue[0]);
				CFCASavePDFProgressBar.this.setString(mostRecentValue[1]);
				if (page < 0)
				{
					CFCASavePDFProgressBar.this.setMinimum(0);
					CFCASavePDFProgressBar.this.setValue(0);
					return;
				}
				CFCASavePDFProgressBar.this.setForeground(Color.YELLOW);
				if (page == this.startPage)
					CFCASavePDFProgressBar.this.setMaximum(this.endPage);
				if (page > this.endPage)
					CFCASavePDFProgressBar.this.setValue(this.endPage);
				else
					CFCASavePDFProgressBar.this.setValue(page);
			}
			private void writeDocument()
			{
				CFCAImageMap tImageMap = CFCASavePDFProgressBar.this.taskerImageMap;
				if (tImageMap.getTabType() != null)
				{
					publish(-1);
					return;
				}
				Music music = tImageMap.getMusic();
				PDDocument document = new PDDocument();
				int fontSize = 32;
				float widthFactor = 1000f / fontSize;
				int scaledFontWidth = (int)(LETTER_WIDTH_PICAS * widthFactor);
				if (music.getStart() % 2 == 0 && CFCAOutputType.DUPLEX == CFCASavePDFProgressBar.this.type)
				{
					try
					{
						publishHeader(document, fontSize, scaledFontWidth, tImageMap);
					}
					catch (IOException e)
					{
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
				try
				{
					publishPages(document, tImageMap);
				}
				catch (PDFGenerationException e)
				{
					e.printStackTrace();
					publish(e.getError());
				}
			}

			private void publishHeader(PDDocument document, int fontSize, int scaledFontWidth, CFCAImageMap tImageMap) throws IOException
			{
				PDPage page = new PDPage();
				BufferedImage bufferedImage = tImageMap.firstEntry().getValue().getBufferedImage();
				int w = (int)(bufferedImage.getWidth()*DEFAULT_PAGE_SCALE);
				int h = (int)(bufferedImage.getHeight()*DEFAULT_PAGE_SCALE);
				page.setMediaBox(new PDRectangle(w, h));
				document.addPage(page);
				PDFont font = PDType1Font.HELVETICA_BOLD;
				PDPageContentStream contentStream = new PDPageContentStream(document, page);
				contentStream.setFont( font, fontSize );
				String[] words = FilenameUtils.removeExtension(this.name).split(BLANK);
				List<String> lines = wordsToLines(words, font, scaledFontWidth);
				contentStream.beginText();
				contentStream.newLineAtOffset( 100, LETTER_UPPER_RIGHT_Y_PICAS-50);
				for(String newLine:lines)
				{
					contentStream.newLineAtOffset(0, -fontSize );
					contentStream.showText(newLine);
				}
				contentStream.endText();
				contentStream.close();
			}

			private void publishPages(PDDocument document, CFCAImageMap tImageMap)
			{
				for (Map.Entry<Integer, CFCABufferedImage> entry : tImageMap.entrySet())
					publishPage(document, entry);
				try
				{
					document.save(CFCASavePDFProgressBar.this.getFile());
					document.close();
				}
				catch (IOException  e)
				{
					publish(-1);
					e.printStackTrace();
				}
				catch (ConcurrentModificationException e)
				{
					throw new RuntimeException(e);
				}
				publish(tImageMap.size() + 1);
			}

			private void publishPage(PDDocument document, Map.Entry<Integer, CFCABufferedImage> entry)
			{
				CFCABufferedImage image = entry.getValue();
				BufferedImage bufferedImage = image.getBufferedImage();
				int pageNo = entry.getKey();
				boolean oddPage = (pageNo % 2) == 1;
				PDPage page = new PDPage();
				PDImageXObject imageXObject;
				try {imageXObject = LosslessFactory.createFromImage(document, bufferedImage);}
				catch (IOException e) {throw new RuntimeException(e);}
				try {document.addPage(page);}
				catch(NullPointerException e) {throw new PDFGenerationException(-1, e);}
				int sourceResolution = image.getMusic().getResolutionDPI();
				int targetResolution = CFCASavePDFProgressBar.this.type == CFCAOutputType.TABLET ? 72 : 200;
				float scale = (float) targetResolution / (float) sourceResolution;
				float picaScale = 72f / sourceResolution;
				float picaWidth = bufferedImage.getWidth() * picaScale;
				float picaHeight = bufferedImage.getHeight() * picaScale;
				PDRectangle mediaBox = CFCASavePDFProgressBar.this.type == CFCAOutputType.TABLET ? new PDRectangle(
						bufferedImage.getWidth() * scale, bufferedImage.getHeight() * scale) : PDRectangle.LETTER;
				float mediaWidth = mediaBox.getWidth();
				float mediaHeight = mediaBox.getHeight();
				page.setMediaBox(mediaBox);
				float x = CFCASavePDFProgressBar.this.type == CFCAOutputType.SIMPLEX ? (mediaWidth - picaWidth)
						: (oddPage ? (mediaWidth - picaWidth) : 0f);
				float y = mediaHeight - picaHeight;
				try
				{
					PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
					contentStream.drawImage(imageXObject, x, y, picaWidth, picaHeight);
					contentStream.close();
				}
				catch (IOException e)
				{						
					throw new RuntimeException(e);
				}
				publish(pageNo);
			}
		};

		this.worker.execute();
	}
}
