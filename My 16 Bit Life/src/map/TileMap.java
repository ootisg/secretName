//This class is depricated, and it's use is no longer required. It, however, shall not be deleted so that bits of code from it may be used in other places.

package map;

import resources.Sprite;
import main.GameObject;
import main.Hitbox;

public class TileMap extends GameObject {
	short width;
	short height;
	Sprite wallSprite;
	byte[] data;
	public TileMap () {
		wallSprite = new Sprite ("resources/sprites/wall.png");
		/*data = new byte[]{(byte)0x7D, (byte)0xF4, (byte)0x1F, (byte)0x10, (byte)0x44, (byte)0x10, (byte)0x10, (byte)0x44, (byte)0x1C, (byte)0x10, (byte)0x44, (byte)0x10, (byte)0x11, (byte)0xF7, (byte)0xDF};*/
		data = new byte[] {(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x07, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
		width = 40;
		height = 30;
	}
	private short bind (short val, short upBound) {
		if (val < 0) {
			return 0;
		}
		if (val > upBound) {
			return upBound;
		}
		return val;
	}
	public void render (int x, int y) {
		for (short i = 0; i < data.length * 8; i ++) {
			if (readBit (i) && Math.floor (i / width) < height) {
				wallSprite.draw ((i % width) * 16, ((short)Math.floor (i / width)) * 16);
			}
		}
	}
	public boolean readBit (short bit) {
		if (bit < data.length * 8) {
			short mask = (short) (0x80 >> (bit % 8));
			if ((data [(short)Math.floor(bit / 8)] & mask) == mask) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	public boolean isColliding (GameObject gameObject) {
		Hitbox hitbox = gameObject.getHitbox ();
		if (hitbox == null) {
			return false;
		}
		short xOffset = (short) gameObject.getHitboxXOffset ();
		short yOffset = (short) gameObject.getHitboxYOffset ();
		short x1 = (short) (gameObject.getX () + xOffset);
		short x2 = (short) (x1 + hitbox.width);
		short y1 = (short) (gameObject.getY () + yOffset);
		short y2 = (short) (y1 + hitbox.height);
		x1 = bind ((short) (Math.floor (x1 / 16)), this.width);
		y1 = bind ((short) (Math.floor (y1 / 16)), this.height);
		x2 = bind ((short) (Math.floor (x2 / 16)), this.width);
		y2 = bind ((short) (Math.floor (y2 / 16)), this.height);
		for (short i = y1; i <= y2; i ++) {
			for (short c = x1; c <= x2; c ++) {
				if (readBit ((short) (i * width + c))) {
					return true;
				}
			}
		}
		return false;
	}
	public short[] getColliding (GameObject gameObject) {
		Hitbox hitbox = gameObject.getHitbox ();
		if (hitbox == null) {
			return new short[] {-1, -1};
		}
		short xOffset = (short) gameObject.getHitboxXOffset ();
		short yOffset = (short) gameObject.getHitboxYOffset ();
		short x1 = (short) (gameObject.getX () + xOffset);
		short x2 = (short) (x1 + hitbox.width);
		short y1 = (short) (gameObject.getY () + yOffset);
		short y2 = (short) (y1 + hitbox.height);
		x1 = bind ((short) (Math.floor (x1 / 16)), this.width);
		y1 = bind ((short) (Math.floor (y1 / 16)), this.height);
		x2 = bind ((short) (Math.floor (x2 / 16)), this.width);
		y2 = bind ((short) (Math.floor (y2 / 16)), this.height);
		for (short i = y1; i <= y2; i ++) {
			for (short c = x1; c <= x2; c ++) {
				if (readBit ((short) (i * width + c))) {
					return new short[] {c, i};
				}
			}
		}
		return new short[] {-1, -1};
	}
	@Override
	public void frameEvent () {
		render (0,0);
	}
}