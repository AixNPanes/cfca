package ws.daley.cfca;

public class CFCAChunk
{
	public enum TYPE
	{
		INTEGER;
	}

	private TYPE type;
	public TYPE getType() {return this.type;}

	private Integer integer;
	public Integer getInteger() {return this.integer;}

	public CFCAChunk(int integer)
	{
		this.type = TYPE.INTEGER;
		this.integer = integer;
	}
}
