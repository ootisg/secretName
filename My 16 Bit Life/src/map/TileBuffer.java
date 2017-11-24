package map;

import resources.Sprite;

public class TileBuffer {
	public double collisionX;
	public double collisionY;
	public Sprite spriteUsed;
	public boolean enabled;
	public TileBuffer () {
		collisionX = -1;
		collisionY = -1;
		spriteUsed = null;
		enabled = false;
	}
}