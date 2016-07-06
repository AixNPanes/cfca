/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ws.daley.cfca.pdf;

import static ws.daley.cfca.util.CFCAUtil.COPY;
import static ws.daley.cfca.util.CFCAUtil.READ;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ws.daley.cfca.music.Music;

public class CFCADocumentConverter
{
	public final static Logger log = (Logger)LoggerFactory.getLogger(CFCADocumentConverter.class);

	public CFCADocumentConverter() {}

	public static CFCAImageMap getSinglePages(String in, Music music) throws IOException {return CFCADocumentConverter.getSinglePages(new File(in), music);}

	private static BufferedImage rotate90DX(BufferedImage bi)
	{
	    int width = bi.getWidth();
	    int height = bi.getHeight();
	    BufferedImage biFlip = new BufferedImage(height, width, bi.getType());
	    for(int i=0; i<width; i++)
	        for(int j=0; j<height; j++)
	            biFlip.setRGB(j, width-1-i, bi.getRGB(i, j));
	    return biFlip;
	} 
	
	private static CFCAImageMap getSinglePages(File file, Music music) throws IOException
	{
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, READ);
		PDFParser parser = new PDFParser(randomAccessFile);
		parser.parse();
		PDDocument doc = parser.getPDDocument();
		PDDocumentCatalog cat = doc.getDocumentCatalog();
		PDPageTree pdPageTree = cat.getPages();
		List<PDPage> pdPages = new ArrayList<PDPage>();
		pdPageTree.iterator().forEachRemaining(pdPages::add);
		
		CFCAImageMap pixelMaps = new CFCAImageMap(file, music);
		int pageCount = pdPages.size();
		for(int i = 0; i < pageCount; i++)
		{
			PDPage pdPage = pdPages.get(i);
			PDResources pdResources = pdPage.getResources();
			Map<COSName, PDXObject> images = new TreeMap<COSName, PDXObject>();
			for(COSName cosName:pdResources.getXObjectNames())
				images.put(cosName, pdResources.getXObject(cosName));
			for(Entry<COSName, PDXObject> objectImageEntry:images.entrySet())
			{
				PDXObject pdXObject = objectImageEntry.getValue();
				if (pdXObject instanceof PDImageXObject)
				{
					PDImageXObject pdXObjectImage= ((PDImageXObject)pdXObject);
					BufferedImage bufferedImage = pdXObjectImage.getImage();
					if (music == null)
						throw new RuntimeException();
					if (!COPY.equals(music.getFormat()) ^ music.getLandscape())
						bufferedImage = rotate90DX(bufferedImage);
					int width = bufferedImage.getWidth();
					int height = bufferedImage.getHeight();
					if (music.getDuplex())
						width /= 2;
					boolean even = i%2 == 0;
					int rightPageNo = even?i+1:pageCount*2-i;
					int leftPageNo = even?pageCount*2-i:i+1;
					if (music.getDuplex())
					{
						pixelMaps.put(rightPageNo,  new CFCABufferedImage(music, getSubImage(bufferedImage, width, 0, width, height)));
						pixelMaps.put(leftPageNo,  new CFCABufferedImage(music, getSubImage(bufferedImage, 0, 0, width, height)));
					}
					else
						pixelMaps.put(i,  new CFCABufferedImage(music, getSubImage(bufferedImage, 0, 0, width, height)));
				}
			}
			String message = String.format("%s - Reading page %03s of %03s pages", file.getName(), i+1, pageCount);
			if (log.isDebugEnabled())
				log.debug(message);
		}
		doc.close();
 		return pixelMaps;
	}
		
	private static BufferedImage getSubImage(BufferedImage bufferedImage, int x, int y, int width, int height)
	{
		BufferedImage subImage = bufferedImage.getSubimage(x, y, width, height);
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		newImage.createGraphics().drawRenderedImage(subImage, null);
		return newImage;
	}
}
