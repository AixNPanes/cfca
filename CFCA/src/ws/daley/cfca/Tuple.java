package ws.daley.cfca;

public class Tuple<N, V>
{
	private N name;
	public N getName() {return this.name;}

	private V value;
	public V getValue() {return this.value;}
	
	public Tuple(N name, V value)
	{
		this.name = name;
		this.value = value;
	}
}