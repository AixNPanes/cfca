package ws.daley.cfca.panel.tabs;

import static ws.daley.cfca.util.CFCAUtil.LETTER_HEIGHT_PICAS;
import static ws.daley.cfca.util.CFCAUtil.LETTER_LOWER_LEFT_X_PICAS;
import static ws.daley.cfca.util.CFCAUtil.LETTER_LOWER_LEFT_Y_PICAS;
import static ws.daley.cfca.util.CFCAUtil.LETTER_UPPER_RIGHT_X_PICAS;
import static ws.daley.cfca.util.CFCAUtil.LETTER_UPPER_RIGHT_Y_PICAS;
import static ws.daley.cfca.util.CFCAUtil.LETTER_WIDTH_PICAS;
import static ws.daley.cfca.util.CFCAUtil.TAB_SIZE_PICAS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;

import ws.daley.cfca.CFCAOutputType;
import ws.daley.cfca.panel.CFCAPanelIntf;
import ws.daley.cfca.pdf.CFCAImageMap;

public class CFCATabs
{
	protected PDDocument doc;
	protected PDPage page = new PDPage();
	protected CFCATabType tabType;
	protected int sets;
	protected float guide;

	@SuppressWarnings("unused")
	private CFCAPanelIntf taskerPanelIntf;

	@SuppressWarnings("resource")
	private CFCATabs(CFCAPanelIntf taskerPanelIntf, File folder, CFCATabType tabType, List<String> songs, List<String> books)
	{
		this.taskerPanelIntf = taskerPanelIntf;
		this.tabType = tabType;
		this.sets = tabType.getSets();
		this.guide = LETTER_WIDTH_PICAS / this.sets;
		this.page.setMediaBox(PDRectangle.LETTER);
		String fileName = tabType.tabType();
		int tabs = tabType.getTabs();
		File tabsDirectory = folder;
		if (!tabsDirectory.exists())
			tabsDirectory.mkdir();

		this.doc.addPage(this.page);
		
		PDPageContentStream contentStream;
		try {contentStream = new PDPageContentStream(this.doc, this.page, PDPageContentStream.AppendMode.OVERWRITE, false);}
		catch (IOException e) {throw new RuntimeException(e);}

		float totalTabSize = tabs * TAB_SIZE_PICAS;
		float totalOverlap = totalTabSize - LETTER_HEIGHT_PICAS;
		float tabOverlap = totalOverlap / (tabs - 1);
		float tabBand = TAB_SIZE_PICAS - tabOverlap;
		float guideBand = (tabOverlap<0?TAB_SIZE_PICAS:(tabBand - tabOverlap)) / this.sets;
		try{contentStream.setNonStrokingColor(240, 240, 240);}
		catch (IOException e) {throw new RuntimeException(e);}
		for(float y = LETTER_UPPER_RIGHT_Y_PICAS; y >= LETTER_LOWER_LEFT_Y_PICAS; y-= TAB_SIZE_PICAS - tabOverlap)
		{
			try{contentStream.setLineWidth(1f);}
			catch (IOException e) {throw new RuntimeException(e);}
			if (y != LETTER_UPPER_RIGHT_Y_PICAS && y > LETTER_LOWER_LEFT_Y_PICAS + guideBand)
			{
				try{contentStream.addRect(LETTER_LOWER_LEFT_X_PICAS, y - tabOverlap, LETTER_UPPER_RIGHT_X_PICAS - LETTER_LOWER_LEFT_X_PICAS, tabOverlap);}
				catch (IOException e) {throw new RuntimeException(e);}
			}
			if (y > LETTER_LOWER_LEFT_Y_PICAS + guideBand || tabOverlap < 0)
			{
				float yy = y - tabOverlap;
				try
				{
					contentStream.moveTo(LETTER_LOWER_LEFT_X_PICAS, yy);
					contentStream.lineTo(LETTER_UPPER_RIGHT_X_PICAS, yy);
					contentStream.stroke();
				}
				catch (IOException e) {throw new RuntimeException(e);}
			}
			if (y != LETTER_UPPER_RIGHT_Y_PICAS || tabOverlap < 0)
				try
				{
					contentStream.moveTo(LETTER_LOWER_LEFT_X_PICAS, y);
					contentStream.lineTo(LETTER_UPPER_RIGHT_X_PICAS, y);
					contentStream.stroke();
				}
				catch (IOException e) {throw new RuntimeException(e);}
			try{contentStream.setLineWidth(.5f);}
			catch (IOException e) {throw new RuntimeException(e);}
			if (tabOverlap < 0)
				for(float deltaX = LETTER_LOWER_LEFT_X_PICAS + this.guide, deltaY = y - guideBand * (this.sets - 1); deltaX< LETTER_UPPER_RIGHT_X_PICAS; deltaX += this.guide, deltaY += guideBand)
					try
					{
						contentStream.moveTo(deltaX, deltaY);
						contentStream.lineTo(LETTER_UPPER_RIGHT_X_PICAS, deltaY);
						contentStream.stroke();
					}
					catch (IOException e) {throw new RuntimeException(e);}
			else
				if (y > LETTER_LOWER_LEFT_Y_PICAS + guideBand)
					for(float deltaX = LETTER_LOWER_LEFT_X_PICAS + this.guide, deltaY = y - guideBand * this.sets; deltaX< LETTER_UPPER_RIGHT_X_PICAS; deltaX += this.guide, deltaY += guideBand)
						try
						{
							contentStream.moveTo(deltaX, deltaY);
							contentStream.lineTo(LETTER_UPPER_RIGHT_X_PICAS, deltaY);
							contentStream.stroke();
						}
						catch (IOException e) {throw new RuntimeException(e);}
		}
		if (tabOverlap < 0)
		{
			try
			{
				contentStream.setLineWidth(1f);
				contentStream.moveTo(LETTER_LOWER_LEFT_X_PICAS, LETTER_LOWER_LEFT_Y_PICAS);
				contentStream.lineTo(LETTER_UPPER_RIGHT_X_PICAS, LETTER_LOWER_LEFT_Y_PICAS);
				contentStream.stroke();
			}
			catch (IOException e) {throw new RuntimeException(e);}
		}
		try{contentStream.beginText();}
		catch (IOException e) {throw new RuntimeException(e);}
		float fontSize = 16.0f;
		PDFont font = PDType1Font.HELVETICA;
		try {
			contentStream.setFont( font, fontSize );
			contentStream.setNonStrokingColor(0, 0, 0);
		}
		catch (IOException e) {throw new RuntimeException(e);}
		int i = 0;
		ArrayList<String> titleList = new ArrayList<String>();
		titleList.addAll(songs);
		titleList.addAll(books);
		String[] titles = titleList.toArray(new String[]{});
		for(String songTitle:titles)
		{
			String title = FilenameUtils.getName(songTitle);
			int group = i / tabs;
			int offset = i % tabs;
			float y;
			if (tabOverlap < 0)
				y= LETTER_UPPER_RIGHT_Y_PICAS - (offset * (tabBand)) - (group + 1) * guideBand + 4f;
			else
				y= LETTER_UPPER_RIGHT_Y_PICAS - (offset * (tabBand)) + (2 - group) * guideBand - tabBand + 4f;
			float stringWidth;
			try{stringWidth = font.getStringWidth( title );}
			catch (IOException e) {throw new RuntimeException(e);}
			float stringX = LETTER_UPPER_RIGHT_X_PICAS - stringWidth / 62;
			try {
				contentStream.setTextMatrix(Matrix.getRotateInstance(0f, stringX, y));
				contentStream.showText(title);
			}
			catch (IOException e) {throw new RuntimeException(e);}
			String text = "Set " + (group + 1) + ", tab " + (offset + 1);
			try {
				contentStream.setTextMatrix(Matrix.getRotateInstance(0f, LETTER_LOWER_LEFT_X_PICAS, y));
				contentStream.showText(text);
			}
			catch (IOException e) {throw new RuntimeException(e);}
			i++;
		}
		try {
			contentStream.endText();
			contentStream.close();
			
			this.doc.save( tabsDirectory.getAbsoluteFile()+File.separator+fileName );
			this.doc.close();
		}
		catch (IOException e) {throw new RuntimeException(e);}
	}

	public static void buildTabs(CFCAPanelIntf taskerPanelIntf)
	{
		CFCATabType[] tabTypes = CFCATabType.getTypes();
		File tabsFolder = new File(taskerPanelIntf.getSaveDirectoryPanelLabel(CFCAOutputType.SIMPLEX).getText());
		List<String> octavoList = new ArrayList<String>();
		List<String> bookList = new ArrayList<String>();
		for(Map.Entry<String, CFCAImageMap> entry:taskerPanelIntf.getDocumentMap().entrySet())
		{
			String fileName = FilenameUtils.removeExtension(entry.getValue().getFile().getAbsolutePath());
			int pages = entry.getValue().size();
			if (pages > 50)
				bookList.add(fileName);
			else
				octavoList.add(fileName);
		}
		for(CFCATabType tab:tabTypes)
		{
			@SuppressWarnings("unused")
			CFCATabs cfcaTabs = new CFCATabs(taskerPanelIntf, tabsFolder, tab, octavoList, bookList);
		}
	}
}
