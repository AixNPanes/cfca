package ws.daley.cfca.music;

import static ws.daley.cfca.util.CFCAUtil.NEW_LINE;
import static ws.daley.cfca.util.CFCAUtil.PDF_FILE_TYPE;
import static ws.daley.cfca.util.CFCAUtil.UTF8;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ws.daley.cfca.CFCAMusicFormat;
import ws.daley.cfca.selectorpanel.CFCASelectorButtonType;

public class MusicCollectionFormat
{
	private Music[] music;
	public Music[] getMusic() {return this.music;}

	private String folder;
	public String getFolder() {return this.folder;}

	private String sourcefolder;
	public String getSourcefolder() {return this.sourcefolder;}
	
	@Override
	public String toString()
	{
		String[] musicRet = new String[this.music.length];
		for(int i = 0; i < this.music.length; i++)
			musicRet[i] = this.music[i].toString();
		return StringUtils.join(musicRet, NEW_LINE);
	}
	
	private boolean hasType(CFCAMusicFormat type)
	{
		for(Music musicSelection:this.music)
			if (musicSelection != null && type.equals(musicSelection.format))
					return true;
		return false;
	}

	public boolean hasTypes(CFCASelectorButtonType[] types)
	{
		for(CFCASelectorButtonType type:types)
		{
			if (hasTypes(type))
				return true;
		}
		return false;
	}

	public boolean hasTypes(CFCASelectorButtonType type)
	{
		if (type == CFCASelectorButtonType.GLUED_BOOKS || type == CFCASelectorButtonType.BOOK_SELECTIONS)
			return hasType(CFCAMusicFormat.BOOK);
		if (type == CFCASelectorButtonType.BOOKS || type == CFCASelectorButtonType.BOOK_SELECTIONS)
			return hasType(CFCAMusicFormat.BOOK);
		if (type == CFCASelectorButtonType.COPIES)
			return hasType(CFCAMusicFormat.COPY);
		if (type == CFCASelectorButtonType.OCTAVOS)
			return hasType(CFCAMusicFormat.OCTAVO);
		return false;
	}

	public Music getMusicEntry(File file)
	{
		String simpleName = file.getName();
		for(Music m:this.music)
		{
			if (m == null)
				continue;
			File test = new File(m.getName() + PDF_FILE_TYPE);
			String testName = test.getName();
			if (testName.equals(simpleName))
				return m;
		}
		return null;
	}

	private static MusicCollectionFormat getCollection(InputStream inputStream)
	{
		InputStreamReader inputStreamReader;
		try {inputStreamReader = new InputStreamReader(inputStream, UTF8);}
		catch (UnsupportedEncodingException e) {throw new RuntimeException(e);}
		Gson gson = new GsonBuilder().create();
		MusicCollectionFormat documentFormat = gson.fromJson(inputStreamReader, MusicCollectionFormat.class);
		return documentFormat;
	}

	public static MusicCollectionFormat getCollection(File file)
	{
		InputStream inputStream;
		try {inputStream = new FileInputStream(file);}
		catch (FileNotFoundException e){throw new RuntimeException(e);}
		return getCollection(inputStream);
	}
}
