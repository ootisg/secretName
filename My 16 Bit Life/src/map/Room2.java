package map;

import main.GameObject;
import main.Hitbox;
import resources.Sprite;

public class Room2 {
	private Sprite[] images;
	private MapChunk[] mapData;
	private boolean[] collisionData;
	private boolean[] flags;
	private int width;
	private int height;
	private int viewX;
	private int viewY;
	private int readBit;
	private byte[] data = new byte[] {(byte)0xF3, (byte)0x77, (byte)0xA9, (byte)0xB2};
	public Room2 () {
		images = new Sprite[] {new Sprite ("resources/sprites/grass.png"), new Sprite ("resources/sprites/stone.png")};
		mapData = new MapChunk[] {new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk (), new MapChunk ()};
		width = 4;
		height = 4;
		viewX = 0;
		viewY = 0;
		readBit = 0;
		collisionData = new boolean[] {false, true};
	}
	private int readBits (int num) {
		int result = 0;
		int mask;
		while (num > 0) {
			if (num >= 8 - (readBit % 8)) {
				int numbits = 8 - (readBit % 8);
				mask = (1 << numbits) - 1;
				result = result + ((data [readBit >> 3] & mask) << (num - numbits));
				num = num - numbits;
				readBit += numbits;
			} else {
				mask = ((1 << num) - 1) << (8 - (readBit % 8) - num);
				result = result + ((data [readBit >> 3] & mask) >> (8 - (readBit % 8) - num));
				readBit += num;
				num = 0;
			}
		}
		return result;
	}
	public boolean isColliding (Hitbox hitbox) {
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = x / 16;
		int x2 = (x + hitbox.width) / 16;
		int y1 = y / 16;
		int y2 = (y + hitbox.height) / 16;
		for (int i = y1; i <= y2; i ++) {
			for (int j = x1; j <= x2; j ++) {
				if (collisionData [getTileId (j, i)] == true) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean[] getCollidingTiles (Hitbox hitbox) {
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = x / 16;
		int x2 = (x + width) / 16;
		int y1 = y / 16;
		int y2 = (y + height) / 16;
		boolean[] result = new boolean[(x2 - x1 + 1) * (y2 - y1 + 1)];
		for (int i = y1; i <= y2; i ++) {
			for (int j = x1; j <= x2; j ++) {
				result [(i - y1) * (x2 - x1 + 1) + (j - x1)] = collisionData [getTileId (j, i)];
			}
		}
		return result;
	}
	public short getTileId (int x, int y) {
		int chunkX = (x / 16);
		int chunkY = (y / 16);
		return mapData [chunkY * width + chunkX].getData () [(y % 16) * 16 + (x % 16)];
	}
	public void frameEvent () {
		for (int i = 0; i < height; i ++) {
			for (int j = 0; j < width; j ++) {
				for (int k = 0; k < 16; k ++) {
					for (int l = 0; l < 16; l ++) {
						images [mapData [i * width + j].getData () [k * 16 + l]].draw (l * 16 + j * 256 - viewX, k * 16 + i * 256 - viewY);
					}
				}
			}
		}
	}
	public void setView (int x, int y) {
		this.viewX = x;
		this.viewY = y;
	}
	public int getViewX () {
		return viewX;
	}
	public int getViewY () {
		return viewY;
	}
	public int getWidth () {
		return width;
	}
	public int getHeight () {
		return height;
	}
}