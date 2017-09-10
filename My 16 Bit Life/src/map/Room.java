package map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import main.GameObject;
import main.Hitbox;
import resources.Sprite;
import resources.Spritesheet;

public class Room {
	private Sprite[] tileList;
	private String[] tileIdList;
	private String[] objectList;
	private short[][][] tileData;
	private boolean[] collisionData;
	private int levelWidth;
	private int levelHeight;
	private int viewX;
	private int viewY;
	private int readBit;
	private byte[] inData;
	private TileAttributesList tileAttributesList;
	public Room () {
		tileAttributesList = new TileAttributesList (MapConstants.tileList);
		tileData = new short[1][32][32];
		levelWidth = 32;
		levelHeight = 32;
		viewX = 0;
		viewY = 0;
		readBit = 0;
	}
	private int readBits (int num) {
		int result = 0;
		int mask;
		while (num > 0) {
			if (num >= 8 - (readBit % 8)) {
				int numbits = 8 - (readBit % 8);
				mask = (1 << numbits) - 1;
				result = result + ((inData [readBit >> 3] & mask) << (num - numbits));
				num = num - numbits;
				readBit += numbits;
			} else {
				mask = ((1 << num) - 1) << (8 - (readBit % 8) - num);
				result = result + ((inData [readBit >> 3] & mask) >> (8 - (readBit % 8) - num));
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
		int x1 = bind (x / 16, 0, levelWidth * 16);
		int x2 = bind ((x + width) / 16, 0, levelWidth * 16);
		int y1 = bind (y / 16, 0, levelHeight * 16);
		int y2 = bind ((y + height) / 16, 0, levelHeight * 16);
		for (int i = x1; i <= x2; i ++) {
			for (int j = y1; j <= y2; j ++) {
				if (collisionData [getTileId (i, j)] == true) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean isColliding (Hitbox hitbox, String tileId) {
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = bind (x / 16, 0, levelWidth * 16);
		int x2 = bind ((x + width) / 16, 0, levelWidth * 16);
		int y1 = bind (y / 16, 0, levelHeight * 16);
		int y2 = bind ((y + height) / 16, 0, levelHeight * 16);
		for (int i = x1; i <= x2; i ++) {
			for (int j = y1; j <= y2; j ++) {
				if (tileIdList [getTileId (i, j)].equals (tileId)) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean[][] getCollidingTiles (Hitbox hitbox) {
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = bind (x / 16, 0, levelWidth * 16);
		int x2 = bind ((x + width) / 16, 0, levelWidth * 16);
		int y1 = bind (y / 16, 0, levelHeight * 16);
		int y2 = bind ((y + height) / 16, 0, levelHeight * 16);
		boolean[][] result = new boolean [(x2 - x1 + 1)][(y2 - y1 + 1)];
		for (int i = y1; i <= y2; i ++) {
			for (int j = x1; j <= x2; j ++) {
				result [j - x1][i - y1] = collisionData [getTileId (j, i)];
			}
		}
		return result;
	}
	public boolean[][] getCollidingTiles (Hitbox hitbox, String tileId) {
		int x = hitbox.x;
		int y = hitbox.y;
		int width = hitbox.width;
		int height = hitbox.height;
		int x1 = bind (x / 16, 0, levelWidth * 16);
		int x2 = bind ((x + width) / 16, 0, levelWidth * 16);
		int y1 = bind (y / 16, 0, levelHeight * 16);
		int y2 = bind ((y + height) / 16, 0, levelHeight * 16);
		boolean[][] result = new boolean [(x2 - x1 + 1)][(y2 - y1 + 1)];
		for (int i = y1; i <= y2; i ++) {
			for (int j = x1; j <= x2; j ++) {
				result [j - x1][i - y1] = tileIdList [getTileId (j, i)].equals (tileId);
			}
		}
		return result;
	}
	public short getTileId (int x, int y) {
		int chunkX = (x / 16);
		int chunkY = (y / 16);
		return tileData [0][x][y];
	}
	public void frameEvent () {
		for (int layer = tileData.length - 1; layer >= 0; layer --) {
			for (int i = 0; i < levelWidth; i ++) {
				for (int j = 0; j < levelHeight; j ++) {
					tileList [tileData [layer][i][j]].draw (i * 16 - viewX, j * 16 - viewY);
				}
			}
		}
	}
	public void loadRoom (String path) throws FileNotFoundException {
		readBit = 0;
		File file = null;
		FileInputStream stream = null;
		file = new File (path);
		inData = new byte[(int) file.length ()];
		stream = new FileInputStream (file);
		try {
			stream.read (inData);
			stream.close ();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (readBits (24) != 0x434D46) {
			System.out.println ("Error: file is corrupted or in an invalid format");
		}
		int version = readBits (8); //For future use
		int layerCount = readBits (8); //For future use
		//resizeLevel (readBits (16), readBits (16));
		levelWidth = readBits (16);
		levelHeight = readBits (16);
		tileData = new short[layerCount][levelWidth][levelHeight];
		for (int layer = 0; layer < layerCount; layer ++) {
			for (int i = 0; i < levelWidth; i ++) {
				for (int c = 0; c < levelHeight; c ++) {
					tileData [layer][i][c] = -1;
				}
			}
		}
		int tilesUsedLength = readBits (16);
		int objectsPlacedLength = readBits (32);
		int tempReadBit = readBit;
		int result = 0;
		int index = 0;
		//Parse tile set list
		while (result != 0x3B) {
			result = readBits (8);
			index ++;
		}
		readBit = tempReadBit;
		char[] tilesetNames = new char[index - 1];
		for (int i = 0; i < index - 1; i ++) {
			tilesetNames [i] = (char) readBits (8);
		}
		readBit += 8;
		String tilesetList = new String (tilesetNames);
		String[] tilesetNameArray = tilesetList.split (",");
		//Parse object list
		tempReadBit = readBit;
		index = 0;
		result = 0;
		while (result != 0x3B) {
			result = readBits (8);
			index ++;
		}
		readBit = tempReadBit;
		char[] objectNames = new char[index - 1];
		for (int i = 0; i < index - 1; i ++) {
			objectNames [i] = (char) readBits (8);
		}
		readBit += 8;
		String objectString = new String (objectNames);
		if (objectString.equals ("")) {
			objectList = new String[0];
		} else {
			objectList = objectString.split (",");
		}
		//Import tiles
		ArrayList<Sprite> tileSheet = new ArrayList<Sprite> ();
		ArrayList<String> tileIdArrList = new ArrayList<String> ();
		Spritesheet importSheet;
		for (int i = 0; i < tilesetNameArray.length; i ++) {
			//System.out.println("resources/tilesets/" + tilesetNameArray [i]);
			importSheet = new Spritesheet ("resources/tilesets/" + tilesetNameArray [i]);
			Sprite[] tempSheet = importSheet.toSpriteArray (16, 16);
			//System.out.println(tempSheet.length);
			for (int j = 0; j < tempSheet.length; j ++) {
				tileSheet.add (tempSheet [j]);
				tileIdArrList.add (tilesetNameArray [i] + ":" + String.valueOf (j));
			}
		}
		short[] tilesUsed = new short[tilesUsedLength];
		int tileBits = numBits (tilesUsedLength - 1);
		tileList = new Sprite[tilesUsed.length];
		tileIdList = new String[tileIdArrList.size ()];
		int tileSheetBits = numBits (tileSheet.size () - 1);
		for (int i = 0; i < tilesUsedLength; i ++) {
			tilesUsed [i] = (short) readBits (tileSheetBits);
		}
		for (int i = 0; i < tileList.length; i ++) {
			tileList [i] = tileSheet.get (tilesUsed [i]);
			tileIdList [i] = tileIdArrList.get (tilesUsed [i]);
		}
		for (int i = 0; i < tileIdArrList.size (); i ++) {
			tileIdList [i] = tileIdArrList.get (i);
		}
		for (int i = 0; i < tileList.length; i ++) {
			tileSheet.add (tileList [i]);
		}
		collisionData = new boolean[tileIdList.length];
		for (int i = 0; i < collisionData.length; i ++) {
			MapTile workingTile = tileAttributesList.getTile (tileIdList [i]);
			if (workingTile != null) {
				collisionData [i] = workingTile.isSolid ();
			} else {
				collisionData [i] = true;
			}
		}
		//Import object icons
		int widthBits = numBits (levelWidth - 1);
		int heightBits = numBits (levelHeight - 1);
		int objectBits = numBits (objectList.length - 1);
		int objId;
		int objX;
		int objY;
		Class<?> objectClass = null;
		Constructor<?> constructor = null;
		for (int i = 0; i < objectsPlacedLength; i ++) {
			objId = readBits (objectBits);
			objX = readBits (widthBits);
			objY = readBits (heightBits);
			try {
				objectClass = Class.forName ("gameObjects." + objectList [objId]);
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				GameObject obj = (GameObject) objectClass.newInstance ();
				obj.declare (objX * 16, objY * 16);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//objectList.add (new GameObject (readBits (objectBits), readBits (widthBits), readBits (heightBits)));
		}
		short id;
		int x1;
		int x2;
		int y1;
		int y2;
		for (int layer = 0; layer < layerCount; layer ++) {
			int fullRangesSize = readBits (32);
			int horizRangesSize = readBits (32);
			int vertRangesSize = readBits (32);
			for (int i = 0; i < fullRangesSize; i ++) {
				id = (short) readBits (tileBits);
				x1 = readBits (widthBits);
				x2 = readBits (widthBits);
				y1 = readBits (heightBits);
				y2 = readBits (heightBits);
				//System.out.println(readBit);
				for (int j = x1; j <= x2; j ++) {
					for (int k = y1; k <= y2; k ++) {
						tileData [layer][j][k] = id;
					}
				}
			}
			for (int i = 0; i < horizRangesSize; i ++) {
				id = (short) readBits (tileBits);
				x1 = readBits (widthBits);
				x2 = readBits (widthBits);
				y1 = readBits (heightBits);
				for (int j = x1; j <= x2; j ++) {
					tileData [layer][j][y1] = id;
				}
			}
			for (int i = 0; i < vertRangesSize; i ++) {
				id = (short) readBits (tileBits);
				x1 = readBits (widthBits);
				y1 = readBits (heightBits);
				y2 = readBits (heightBits);
				for (int j = y1; j <= y2; j ++) {
					tileData [layer][x1][j] = id;
				}
			}
			for (int i = 0; i < levelWidth; i ++) {
				for (int c = 0; c < levelHeight; c ++) {
					if (tileData [layer][i][c] == -1) {
						tileData [layer][i][c] = (short) readBits (tileBits);
					}
				}
			}
		}
	}
	public int numBits (int num) {
		for (int i = 31; i > 0; i --) {
			if (num >= (1 << (i - 1))) {
				return i;
			}
		}
		return 1;
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
		return levelWidth;
	}
	public int getHeight () {
		return levelHeight;
	}
	public int bind (int value, int min, int max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}
}