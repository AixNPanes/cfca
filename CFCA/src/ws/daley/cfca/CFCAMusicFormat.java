package ws.daley.cfca;

import com.google.gson.annotations.SerializedName;

import ws.daley.cfca.util.CFCAUtil;

public enum CFCAMusicFormat
{
	@SerializedName(CFCAUtil.BOOK) BOOK,
	@SerializedName(CFCAUtil.GLUED_BOOK) GLUED_BOOK,
	@SerializedName(CFCAUtil.COPY) COPY,
	@SerializedName(CFCAUtil.OCTAVO) OCTAVO,
	UNKNOWN;
}
