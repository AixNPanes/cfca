package ws.daley.cfca.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;

import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import ws.daley.cfca.chooser.CFCAFileChooserTitle;

public class CFCAUtil
{
	public static final int PICAS_PER_INCH = 72;
//	public static final int DEFAULT_SCREEN_RESOLUTION = ImageIOUtil.DEFAULT_SCREEN_RESOLUTION;
	public static final int DEFAULT_PAGE_RESOLUTION = 200;
	public static final float DEFAULT_PAGE_SCALE = (float)PICAS_PER_INCH / (float)DEFAULT_PAGE_RESOLUTION;
	public static final float HALF_INCH = .5f;
	public static final float TAB_SIZE = 1.5f;
	public static final int HALF_INCH_PICAS = (int)(HALF_INCH * PICAS_PER_INCH);
	public static final int TAB_SIZE_PICAS = (int)(TAB_SIZE * PICAS_PER_INCH);
	public static final int LETTER_PAGE_WIDTH_PICAS = (int)PDRectangle.LETTER.getWidth();
	public static final int LETTER_PAGE_HEIGHT_PICAS = (int)PDRectangle.LETTER.getHeight();
	public static final float LETTER_MARGIN_LEFT_INCHES = 1f;
	public static final float LETTER_MARGIN_RIGHT_INCHES = .5f;
	public static final float LETTER_MARGIN_TOP_INCHES = .5f;
	public static final float LETTER_MARGIN_BOTTOM_INCHES = .5f;
	public static final int LETTER_MARGIN_LEFT_PICAS = (int)(LETTER_MARGIN_LEFT_INCHES * PICAS_PER_INCH);
	public static final int LETTER_MARGIN_RIGHT_PICAS = LETTER_PAGE_WIDTH_PICAS - (int)(LETTER_MARGIN_RIGHT_INCHES * PICAS_PER_INCH);
	public static final int LETTER_MARGIN_TOP_PICAS = (int)(LETTER_MARGIN_TOP_INCHES * PICAS_PER_INCH);
	public static final int LETTER_MARGIN_BOTTOM_PICAS = LETTER_PAGE_HEIGHT_PICAS - (int)(LETTER_MARGIN_BOTTOM_INCHES * PICAS_PER_INCH);
	public static final BoundingBox LETTER_BOUNDING_BOX = new BoundingBox(LETTER_MARGIN_LEFT_PICAS, LETTER_MARGIN_TOP_PICAS, LETTER_MARGIN_RIGHT_PICAS, LETTER_MARGIN_BOTTOM_PICAS);
	public static final float LETTER_HEIGHT_PICAS = LETTER_BOUNDING_BOX.getHeight();
	public static final float LETTER_WIDTH_PICAS = LETTER_BOUNDING_BOX.getWidth();
	public static final float LETTER_LOWER_LEFT_X_PICAS = LETTER_BOUNDING_BOX.getLowerLeftX();
	public static final float LETTER_LOWER_LEFT_Y_PICAS = LETTER_BOUNDING_BOX.getLowerLeftY();
	public static final float LETTER_UPPER_RIGHT_X_PICAS = LETTER_BOUNDING_BOX.getUpperRightX();
	public static final float LETTER_UPPER_RIGHT_Y_PICAS = LETTER_BOUNDING_BOX.getUpperRightY();
	public static final String NEW_LINE = "\n";
	public static final String CANCEL_SELECTION = "CancelSelection";
	public static final String APPROVE_SELECTION = "ApproveSelection";
	public static final String SELECT_INPUT_PDF_FILES = "Select input PDF files";
	public static final String PDF_FILE = "PDF file";
	public static final String PDF_FILES = "PDF files";
	public static final String PDF = "pdf";
	public static final String PDF_FILE_TYPE = "." + PDF;
	public static final String JPEG = "jpg";
	public static final String SELECT_JSON_CONTROL_FILE = "Select JSON Control File";
	public static final String JSON_FILE = "JSON file";
	public static final String JSON_FILES = "JSON files";
	public static final String JSON = "json";
	public static final String PROGRESS_BAR_START = "ProgressBarStart";
	public static final String PROGRESS_BAR_DONE = "ProgressBarDone";
	public static final String TEMP_PDF = "temp.pdf";
	public static final String COPY = "copy";
	public static final String BOOK = "book";
	public static final String GLUED_BOOK = "gluedbook";
	public static final String OCTAVO = "octavo";
	public static final String MINIMUM_SIZE = "minimumSize";
	public static final String PREFERRED_SIZE = "preferredSize";
	public static final String MAXIMUM_SIZE = "maximumSize";
	public static final String CONFIG_FOLDER = "config";
	public static final String EQUALS = "=";
	public static final String SPACED_EQUALS = " " + EQUALS + " ";
	public static final String EMPTY = "";
	public static final String BLANK = " ";
	public static final String MINUS = "-";
	public static final String READ = "r";
	public static final String UTF8 = "UTF-8";
	public static final String CFCA = "CFCA";
	public static final String HASH = "#";
//	public static final String EIGHT_TAB_PDF = "8tab" + PDF_FILE_TYPE;
//	public static final String FIVE_TAB_PDF = "5tab" + PDF_FILE_TYPE;
	public static final CFCAFileChooserTitle SELECT_INPUT_PDF_FILES_CHOOSER = new CFCAFileChooserTitle(SELECT_INPUT_PDF_FILES);
	public static final CFCAFileChooserTitle SELECT_JSON_CONTROL_FILES_CHOOSER = new CFCAFileChooserTitle(SELECT_JSON_CONTROL_FILE);
	public static final Border border;

	static {
		border = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
	}

	public static String nameSplitter(String name)
	{
		String s1 = name.replaceAll(CFCA, EMPTY).replaceAll("([A-Z])", "#$1").replaceAll(HASH,  BLANK).trim();
		return s1;
	}

	public static void setAllSize(Component c, Dimension size)
	{
		setSize(c, size, size, size, size);
	}

	private static void setSize(Component c, Dimension minSize, Dimension preferredSize, Dimension size, Dimension maxSize)
	{
		c.setSize(minSize);
		c.setPreferredSize(preferredSize);
		c.setMinimumSize(size);
		c.setMaximumSize(maxSize);
	}

	public static JFrame getFrame(Component me)
	{
		Container container = me.getParent();
		while (container != null && !(container instanceof JFrame))
			container = container.getParent();
		return (JFrame) container;
	}

	public static void cancel(Component me)
	{
		JFrame jFrame = getFrame(me);
		if (jFrame != null)
			jFrame.dispose();
	}
}
