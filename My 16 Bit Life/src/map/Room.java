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
		//A fairly generic constructor
		tileAttributesList = new TileAttributesList (MapConstants.tileList);
		tileData = new short[1][32][32];
		levelWidth = 32;
		levelHeight = 32;
		viewX = 0;
		viewY = 0;
		readBit = 0;
	}
	private int readBits (int num) {
		//Reads a number of bits from the byte[] inData equal to num and returns them as an int
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
	public boolean isColliding (double x1, double y1, double x2, double y2) {
		//Returns true if the vector (x1, y1) -> (x2, y2) is colliding with a solid tile
		if (collisionData [getTileId (((int) x1) / 16, ((int) y1) / 16)]) {
			return true;
		}
		if (x1 > x2) {
			double temp = x1;
			x1 = x2;
			x2 = temp;
			temp = y1;
			y1 = y2;
			y2 = temp;
		}
		if (x1 == x2) {
			//Vertical case
			double ycheck = y1;
			double ystep = y1;
			while (true) {
				if (ycheck % 16 != 0) {
					ycheck = ((int) ycheck / 16) * 16 + 16;
				} else {
					ycheck += 16;
				}
				if (ycheck > y2) {
					break;
				}
				ystep = ycheck;
				if (collisionData [getTileId ((int) x1 / 16, (int) ystep / 16)]) {
					return true;
				}
			}
			return false;
		} else if (y1 == y2) {
			//Horizontal case
			double xcheck = x1;
			double xstep = x1;
			while (true) {
				if (xcheck % 16 != 0) {
					xcheck = ((int) xcheck / 16) * 16 + 1;
				} else {
					xcheck += 16;
				}
				if (xcheck > y2) {
					break;
				}
				xstep = xcheck;
				if (x1 > 0 && x1 <= this.getWidth () && y1 > 0 && y1 <= this.getHeight ()) {
					if (collisionData [getTileId ((int) x1 / 16, (int) xstep / 16)]) {
						return true;
					}
				}
			}
			return false;
		} else {
			//Default case
			//y = mx + b
			//x = (y - b) / m
			//b = y - mx
			double m = (y1 - y2) / (x1 - x2);
			double b = y1 - m * x1;
			double xcheck1 = x1;
			double ycheck1 = y1;
			double xcheck2 = x1;
			double ycheck2 = y1;
			double stepx = x1;
			double stepy = y1;
			boolean checkUp = false;
			int tileX;
			int tileY;
			while (true) {
				checkUp = false;
				if (m < 0) {
					if (stepy % 16 != 0) {
						ycheck1 = (((int) stepy) / 16) * 16;
						checkUp = true;
					} else {
						ycheck1 -= 16;
						checkUp = true;
					}
					xcheck1 = (ycheck1 - b) / m;
				} else {
					if (stepy % 16 != 0) {
						ycheck1 = (((int) stepy) / 16) * 16 + 16;
					} else {
						ycheck1 += 16;
					}
					xcheck1 = (ycheck1 - b) / m;
				}
				if (xcheck2 % 16 != 0) {
					xcheck2 = (((int) xcheck2) / 16) * 16 + 16;
				} else {
					xcheck2 = xcheck2 + 16;
				}
				tileX = (int) xcheck1 / 16;
				tileY = (int) ycheck1 / 16;
				if (xcheck1 > xcheck2) {
					double temp;
					temp = xcheck1;
					xcheck1 = xcheck2;
					xcheck2 = temp;
					temp = ycheck1;
					ycheck1 = ycheck2;
					ycheck2 = temp;
					tileX ++;
				} else if (checkUp) {
					tileY --;
				}
				/*System.out.print ((int) xcheck1 / 16);
				System.out.print (", ");
				System.out.println ((int) ycheck1 / 16);
				//System.out.print (", ");
				//System.out.println ((int) y1 / 16);*/
				if (xcheck1 >= x1 && xcheck2 <= x2) {
					if (tileX > 0 && tileX <= this.getWidth () / 16 && tileY > 0 && tileY <= this.getHeight () / 16) {
						if (collisionData [getTileId (tileX, tileY)]) {
							return true;
						}
					}
					stepx = xcheck1;
					stepy = ycheck1;
				} else {
					break;
				}
			}
		}
		return false;
	}
	public boolean isColliding (Hitbox hitbox) {
		//Returns true if the given Hitbox is colliding with a solid tile
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
		//Returns true if the given Hitbox is colliding with a tile of type tileId
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
		//Returns a matrix of tiles that are being collided with by the given Hitbox
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
		//Returns a matrix of tiles that are under the given Hitbox and have the given tileId
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
		//Returns the numerical tile ID of a give object
		return tileData [0][x][y];
	}
	public void frameEvent () {
		//Renders the room
		for (int layer = tileData.length - 1; layer >= 0; layer --) {
			for (int i = 0; i < levelWidth; i ++) {
				for (int j = 0; j < levelHeight; j ++) {
					tileList [tileData [layer][i][j]].draw (i * 16 - viewX, j * 16 - viewY);
				}
			}
		}
	}
	public void loadRoom (String path) throws FileNotFoundException {
		//Loads the CMF file at the given filepath
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
			TileData workingTile = tileAttributesList.getTile (tileIdList [i]);
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
		//Returns the number of bits needed to represent a given number
		for (int i = 31; i > 0; i --) {
			if (num >= (1 << (i - 1))) {
				return i;
			}
		}
		return 1;
	}
	public void setView (int x, int y) {
		//Sets the top-right coordinate of the viewport of the room to (x, y)
		this.viewX = x;
		this.viewY = y;
	}
	public int getViewX () {
		//Returns the x-coordinate of the viewport of the room
		return viewX;
	}
	public int getViewY () {
		//Returns the y-coordinate of the viewport of the room
		return viewY;
	}
	public int getWidth () {
		//Returns the width of the room in tiles
		return levelWidth;
	}
	public int getHeight () {
		//Returns the height of the room in tiles
		return levelHeight;
	}
	public int bind (int value, int min, int max) {
		//Binds a value to within the range min, max
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}
}