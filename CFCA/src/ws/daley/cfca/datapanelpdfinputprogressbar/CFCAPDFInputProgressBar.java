package ws.daley.cfca.datapanelpdfinputprogressbar;

import static ws.daley.cfca.util.CFCAUtil.BLANK;
import static ws.daley.cfca.util.CFCAUtil.PROGRESS_BAR_DONE;
import static ws.daley.cfca.util.CFCAUtil.PROGRESS_BAR_START;
import static ws.daley.cfca.util.CFCAUtil.READ;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import ws.daley.cfca.music.Music;
import ws.daley.cfca.pdf.CFCABufferedImage;
import ws.daley.cfca.pdf.CFCAImageMap;
import ws.daley.cfca.progressbar.CFCAFileProgressBar;

public class CFCAPDFInputProgressBar extends CFCAFileProgressBar
{
	private static final long serialVersionUID = 1L;

	private PDDocument pdDocument;
	List<PDPage> pdPages;
	CFCAImageMap taskerImageMap;
	public CFCAImageMap getTaskerImageMap() {return this.taskerImageMap;}

	Music music;
	SwingWorker<Boolean, String[]> worker;

	public CFCAPDFInputProgressBar(File file, Music music)
	{
		super(file);
		this.music = music;
	}
	
	@Override
	public void start()
	{
		this.worker = new SwingWorker<Boolean, String[]>()
		{
			@Override
			protected Boolean doInBackground() throws Exception
			{
				getSinglePages();
				return true;
			}

			protected void publish(Integer i)
			{
				String name = CFCAPDFInputProgressBar.this.getFile().getName();
				if (i < 0)
				{
					publish(new String[] {Integer.toString(i), "Reading " + name});
					return;
				}
				int count = CFCAPDFInputProgressBar.this.pdPages.size();
				String message;
				if (i > count)
					message = "complete";
				else
					message = String.format("page %3d of %3d pages", i, count);
				publish(new String[] {Integer.toString(i), "Reading " + name + ", " + message});
			}
			
			@Override
			protected void done()
			{
				@SuppressWarnings("unused")
				boolean status;
				try {status = get();}
				catch (InterruptedException | ExecutionException | CancellationException e) {}
				ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, PROGRESS_BAR_DONE);
				for(ActionListener l:CFCAPDFInputProgressBar.this.listeners)
					l.actionPerformed(e);
			}

			@Override
			protected void process(List<String[]> chunks)
			{
				String[] mostRecentValue = chunks.get(chunks.size() - 1);
				int page = Integer.parseInt(mostRecentValue[0]);
				CFCAPDFInputProgressBar.this.setString(mostRecentValue[1]);
				if (page < 0)
				{
					CFCAPDFInputProgressBar.this.setMinimum(0);
					CFCAPDFInputProgressBar.this.setValue(0);
					return;
				}
				int count = CFCAPDFInputProgressBar.this.pdPages.size();
				if (page == 1)
					CFCAPDFInputProgressBar.this.setMaximum(count);
				if (page > CFCAPDFInputProgressBar.this.pdPages.size())
					CFCAPDFInputProgressBar.this.setValue(count);
				else
					CFCAPDFInputProgressBar.this.setValue(page);
			}
			
			private BufferedImage getSubImage(BufferedImage bufferedImage, int x, int y, int width, int height)
			{
				BufferedImage subImage = bufferedImage.getSubimage(x, y, width, height);
				BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				newImage.createGraphics().drawRenderedImage(subImage, null);
				return newImage;
			}

			private BufferedImage rotate90DX(BufferedImage bi)
			{
				int width = bi.getWidth();
				int height = bi.getHeight();
				BufferedImage biFlip = new BufferedImage(height, width, bi.getType());
				for(int i=0; i<width; i++)
					for(int j=0; j<height; j++)
						biFlip.setRGB(j, width-1-i, bi.getRGB(i, j));
				return biFlip;
			} 

			@SuppressWarnings({ "synthetic-access" })
			private void getSinglePages() throws IOException
			{
				RandomAccessFile randomAccessFile = new RandomAccessFile(CFCAPDFInputProgressBar.this.file, READ);
				publish(-1);
				PDFParser parser = new PDFParser(randomAccessFile);
				parser.parse();
				CFCAPDFInputProgressBar.this.pdDocument = parser.getPDDocument();
				PDFRenderer pdfRenderer = new PDFRenderer(CFCAPDFInputProgressBar.this.pdDocument);
				PDDocumentCatalog pdDocumentCatalog = CFCAPDFInputProgressBar.this.pdDocument.getDocumentCatalog();
				PDPageTree pdPageTree = pdDocumentCatalog.getPages();
				CFCAPDFInputProgressBar.this.pdPages = new ArrayList<PDPage>();
				pdPageTree.iterator().forEachRemaining(CFCAPDFInputProgressBar.this.pdPages::add);
				ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, PROGRESS_BAR_START + BLANK + CFCAPDFInputProgressBar.this.pdPages.size());
				for(ActionListener l:CFCAPDFInputProgressBar.this.listeners)
					l.actionPerformed(e);
				CFCAPDFInputProgressBar.this.taskerImageMap = new CFCAImageMap(CFCAPDFInputProgressBar.this.file, CFCAPDFInputProgressBar.this.music);
				int pageCount = CFCAPDFInputProgressBar.this.pdPages.size();
				for(int i = 0; i < pageCount; i++)
				{
					publish(i+1);
					BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 200, ImageType.RGB);
					if (CFCAPDFInputProgressBar.this.music == null)
					{
						randomAccessFile.close();
						throw new RuntimeException();
					}
					if (CFCAPDFInputProgressBar.this.music.getLandscape())
						bufferedImage = rotate90DX(bufferedImage);
					int width = bufferedImage.getWidth();
					int height = bufferedImage.getHeight();
					if (CFCAPDFInputProgressBar.this.music.getTwoPage())
					{
						width /= 2;
						boolean even = i%2 == 0;
						int rightPageNo = even?i+1:pageCount*2-i;
						int leftPageNo = even?pageCount*2-i:i+1;
						putPage(bufferedImage, rightPageNo, width, 0, width, height);
						putPage(bufferedImage, leftPageNo, 0, 0, width, height);
					}
					else
					{
						int pageNo = CFCAPDFInputProgressBar.this.music.getStart() + i;
						putPage(bufferedImage, pageNo, 0, 0, width, height);
					}
				}
				publish(pageCount+1);
				CFCAPDFInputProgressBar.this.pdDocument.close();
			};

			private void putPage(BufferedImage bufferedImage, int pageNo, int x, int y, int width, int height)
			{
				CFCAPDFInputProgressBar.this.taskerImageMap.put(
						pageNo,  
						new CFCABufferedImage(
							CFCAPDFInputProgressBar.this.music,
							getSubImage(bufferedImage, x, y, width, height)));
			}
		};

		this.worker.execute();
	}
}
