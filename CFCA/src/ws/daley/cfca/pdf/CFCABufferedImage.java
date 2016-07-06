package ws.daley.cfca.pdf;

import java.awt.image.BufferedImage;

import ws.daley.cfca.music.Music;

public class CFCABufferedImage
{
	private BufferedImage bufferedImage;
	public BufferedImage getBufferedImage() {return this.bufferedImage;}

	private Music music;
	public Music getMusic() {return this.music;}

	public CFCABufferedImage(Music music, BufferedImage bufferedImage)
	{
		this.music = music;
		this.bufferedImage = bufferedImage;
	}
}
