package map;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import main.GameWindow;
import main.GameObject;
import main.Hitbox;
import main.MainLoop;
import resources.Sprite;
import resources.Spritesheet;

public class Room {
	private Sprite[] tileList;
	private String[] tileIdList;
	private String[] objectList;
	private short[][][] tileData;
	private boolean[] collisionData;
	private double gravity = .65625;
	private int levelWidth;
	private int levelHeight;
	private int viewX;
	private int viewY;
	private int readBit;
	private byte[] inData;
	private static double[] hitboxCorners = new double[] {0, 0, 1, 0, 1, 1, 0, 1, 0, 0};
	private TileAttributesList tileAttributesList;
	private ArrayList<Background> backgroundList;
	public TileBuffer tileBuffer = new TileBuffer ();
	public Room () {
		//A fairly generic constructor
		tileAttributesList = new TileAttributesList (MapConstants.tileList);
		tileData = new short[1][32][32];
		levelWidth = 32;
		levelHeight = 32;
		viewX = 0;
		viewY = 0;
		readBit = 0;
		backgroundList = new ArrayList<Background> ();
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
		if (getCollidingCoords (x1, y1, x2, y2) != null) {
			return true;
		}
		return false;
	}
	public double[] getCollidingCoords (double x1, double y1, double x2, double y2) {
		if (x1 < 0 || x1 > levelWidth * 16 || x2 < 0 || x2 > levelWidth * 16 || y1 < 0 || y1 > levelHeight * 16 || y2 < 0 || y2 > levelHeight * 16) {
			return null;
		}
		int xdir = 1;
		int ydir = 1;
		double xcheck1 = 0;
		double ycheck1 = 0;
		double xcheck2 = 0;
		double ycheck2 = 0;
		double xstep = x1;
		double ystep = y1;
		byte tileXOffset = 0;
		byte tileYOffset = 0;
		if (x1 > x2) {
			xdir = -1;
		}
		if (y1 > y2) {
			ydir = -1;
		}
		if (collisionData [getTileId ((int) x1 / 16, (int) y1 / 16)]) {
			return new double[] {x1, y1};
		}
		if (x1 == x2) {
			while (true) {
				tileYOffset = 0;
				ystep = snap16 (ystep, ydir);
				if (ydir == -1 && ystep % 16 == 0) {
					tileYOffset = -1;
				}
				if (!isBetween (ystep, y1, y2)) {
					return null;
				}
				if (collisionData [getTileId ((int) x1 / 16, (int) ystep / 16 + tileYOffset)]) {
					return new double[] {x1, ystep};
				}
			}
		}

		if (y1 == y2) {
			while (true) {
				tileXOffset = 0;
				xstep = snap16 (xstep, xdir);
				if (xdir == -1 && xstep % 16 == 0) {
					tileXOffset = -1;
				}
				if (!isBetween (xstep, x1, x2)) {
					return null;
				}
				if (collisionData [getTileId ((int) x1 / 16 + tileXOffset, (int) ystep / 16)]) {
					return new double[] {xstep, y1};
				}
			}
		}
		double m = (y1 - y2) / (x1 - x2);
		double b = y1 - m * x1;
		while (true) {
			tileXOffset = 0;
			tileYOffset = 0;
			xcheck1 = snap16 (xstep, xdir);
			ycheck1 = m * xcheck1 + b;
			ycheck2 = snap16 (ystep, ydir);
			xcheck2 = (ycheck2 - b) / m;
			if (Math.abs (x1 - xcheck1) > Math.abs (x1 - xcheck2)) {
				double temp = xcheck1;
				xcheck1 = xcheck2;
				xcheck2 = temp;
				temp = ycheck1;
				ycheck1 = ycheck2;
				ycheck2 = temp;
			}
			xstep = xcheck1;
			ystep = ycheck1;
			if (!isBetween (xstep, x1, x2) || !isBetween (ystep, y1, y2)) {
				return null;
			}
			if (xdir == -1 && xstep % 16 == 0) {
				tileXOffset = -1;
			}
			if (ydir == -1 && ystep % 16 == 0) {
				tileYOffset = -1;
			}
			if (collisionData [getTileId ((int) xstep / 16 + tileXOffset, (int) ystep / 16 + tileYOffset)]) {
				return new double[] {xstep, ystep};
			}
		}
	}
	public void setTileBuffer (double x1, double y1, double x2, double y2) {
		//MainLoop.getWindow ().getBuffer ().setColor (new Color (0xFF0000));
		if ((x1 < 0 && x2 < 0) || (x1 > levelWidth * 16 && x2 > levelWidth * 16) || (y1 < 0 && y2 < 0) || (y1 > levelWidth * 16 && y2 > levelWidth * 16)) {
			tileBuffer.enabled = false;
			return;
		} else {
			tileBuffer.enabled = true;
		}
		int xdir = 1;
		int ydir = 1;
		double xcheck1 = 0;
		double ycheck1 = 0;
		double xcheck2 = 0;
		double ycheck2 = 0;
		double xstep = x1;
		double ystep = y1;
		byte tileXOffset = 0;
		byte tileYOffset = 0;
		if (x1 > x2) {
			xdir = -1;
		}
		if (y1 > y2) {
			ydir = -1;
		}
		/*if (collisionData [getTileId ((int) x1 / 16, (int) y1 / 16)]) {
			tileBuffer.collisionX = x1;
			tileBuffer.collisionY = y2;
			tileBuffer.spriteUsed = tileList [getTileId ((int) x1 / 16, (int) y1 / 16)];
			return;
		}*/
		if (x1 == x2) {
			while (true) {
				tileYOffset = 0;
				ystep = snap16 (ystep, ydir);
				if (ydir == -1 && ystep % 16 == 0) {
					tileYOffset = -1;
				}
				if (!isBetween (ystep, y1, y2)) {
					tileBuffer.enabled = false;
					return;
				}
				int tileFinalX = (int) x1 / 16;
				int tileFinalY = (int) ystep / 16 + tileYOffset;
				if (tileFinalX < 0 || tileFinalX >= levelWidth || tileFinalY < 0 || tileFinalY >= levelHeight) {
					tileBuffer.enabled = false;
					return;
				}
				if (collisionData [getTileId (tileFinalX, tileFinalY)]) {
					tileBuffer.collisionX = x1;
					tileBuffer.collisionY = y2;
					tileBuffer.spriteUsed = tileList [getTileId ((int) x1 / 16, (int) ystep / 16 + tileYOffset)];
					tileBuffer.mapTile.tileId = tileIdList [getTileId ((int) x1 / 16, (int) ystep / 16 + tileYOffset)];
					tileBuffer.mapTile.x = (int) x1 / 16;
					tileBuffer.mapTile.y = (int) ystep / 16 + tileYOffset;
					return;
				}
			}
		}

		if (y1 == y2) {
			while (true) {
				tileXOffset = 0;
				xstep = snap16 (xstep, xdir);
				if (xdir == -1 && xstep % 16 == 0) {
					tileXOffset = -1;
				}
				if (!isBetween (xstep, x1, x2)) {
					tileBuffer.enabled = false;
					return;
				}
				int tileFinalX = (int) x1 / 16 + tileXOffset;
				int tileFinalY = (int) ystep / 16;
				if (tileFinalX < 0 || tileFinalX >= levelWidth || tileFinalY < 0 || tileFinalY >= levelHeight) {
					tileBuffer.enabled = false;
					return;
				}
				if (collisionData [getTileId (tileFinalX, tileFinalY)]) {
					tileBuffer.collisionX = xstep;
					tileBuffer.collisionY = y1;
					tileBuffer.spriteUsed = tileList [getTileId ((int) x1 / 16 + tileXOffset, (int) ystep / 16)];
					tileBuffer.mapTile.tileId = tileIdList [getTileId ((int) x1 / 16 + tileXOffset, (int) ystep / 16)];
					tileBuffer.mapTile.x = (int) x1 / 16 + tileXOffset;
					tileBuffer.mapTile.y = (int) ystep / 16;
					return;
				}
			}
		}
		double m = (y1 - y2) / (x1 - x2);
		double b = y1 - m * x1;
		while (true) {
			tileXOffset = 0;
			tileYOffset = 0;
			xcheck1 = snap16 (xstep, xdir);
			ycheck1 = m * xcheck1 + b;
			ycheck2 = snap16 (ystep, ydir);
			xcheck2 = (ycheck2 - b) / m;
			if (Math.abs (x1 - xcheck1) > Math.abs (x1 - xcheck2)) {
				double temp = xcheck1;
				xcheck1 = xcheck2;
				xcheck2 = temp;
				temp = ycheck1;
				ycheck1 = ycheck2;
				ycheck2 = temp;
			}
			xstep = xcheck1;
			ystep = ycheck1;
			//MainLoop.getWindow ().getBuffer ().fillRect ((int)xstep, (int)ystep, 1, 1);
			if (!isBetween (xstep, x1, x2) || !isBetween (ystep, y1, y2)) {
				tileBuffer.enabled = false;
				return;
			}
			if (xdir == -1 && xstep % 16 == 0) {
				tileXOffset = -1;
			}
			if (ydir == -1 && ystep % 16 == 0) {
				tileYOffset = -1;
			}
			int tileFinalX = (int) xstep / 16 + tileXOffset;
			int tileFinalY = (int) ystep / 16 + tileYOffset;
			if (tileFinalX < 0 || tileFinalX >= levelWidth || tileFinalY < 0 || tileFinalY >= levelHeight) {
				tileBuffer.enabled = false;
				return;
			}
			if (collisionData [getTileId (tileFinalX, tileFinalY)]) {
				tileBuffer.collisionX = xstep;
				tileBuffer.collisionY = ystep;
				tileBuffer.spriteUsed = tileList [getTileId ((int) xstep / 16 + tileXOffset, (int) ystep / 16 + tileYOffset)];
				tileBuffer.mapTile.tileId = tileIdList [getTileId ((int) xstep / 16 + tileXOffset, (int) ystep / 16 + tileYOffset)];
				tileBuffer.mapTile.x = (int) xstep / 16 + tileXOffset;
				tileBuffer.mapTile.y = (int) ystep / 16 + tileYOffset;
				return;
			}
		}
	}
	public double snap16 (double num, int direction) {
		if (num % 16 == 0) {
			if (direction == 1) {
				return num + 16;
			} else {
				return num - 16;
			}
		} else {
			if (direction == 1) {
				return Math.ceil (num / 16) * 16;
			} else {
				return Math.floor (num / 16) * 16;
			}
		}
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
	public boolean isColliding (Hitbox hitbox, double xTo, double yTo) {
		for (int i = 0; i <= 6; i += 2) {
			if (isColliding (hitbox.x + hitboxCorners [i] * hitbox.width, hitbox.y + hitboxCorners [i + 1] * hitbox.height, xTo + hitboxCorners [i] * hitbox.width, yTo + hitboxCorners [i + 1] * hitbox.height)) {
				return true;
			}
		}
		return false;
	}
	public double[] getCollidingCoords (Hitbox hitbox, double xTo, double yTo) {
		double[][] collisionData = new double [4][2];
		for (int i = 0; i <= 6; i += 2) {
			collisionData [i / 2] = getCollidingCoords (hitbox.x + hitboxCorners [i] * hitbox.width, hitbox.y + hitboxCorners [i + 1] * hitbox.height, xTo + hitboxCorners [i] * hitbox.width, yTo + hitboxCorners [i + 1] * hitbox.height);
			if (collisionData [i / 2] != null) {
				if (collisionData [i / 2][0] >= hitbox.x + hitboxCorners [i]) {
					collisionData [i / 2][0] -= hitboxCorners [i] * hitbox.width;
				}
				if (collisionData [i / 2][1] >= hitbox.y * hitboxCorners [i + 1]) {
					collisionData [i / 2][1] -= hitboxCorners [i + 1] * hitbox.height;
				}
			}
		}
		double[] returnData = null;
		double minDist = -1;
		double currentDist;
		for (int i = 0; i < 4; i ++) {
			//System.out.println(collisionData[i][1]);
			if (collisionData [i] != null) {
				currentDist = (collisionData [i][0] - hitbox.x) * (collisionData [i][0] - hitbox.x) + (collisionData [i][1] - hitbox.y) * (collisionData [i][1] - hitbox.y);
				if (currentDist <= minDist || minDist == -1) {
					minDist = currentDist;
					returnData = collisionData [i];
				}
			}
		}
		return returnData;
	}
	public boolean setTileBuffer (Hitbox hitbox, double xTo, double yTo) {
		MapTile[] collidingTiles = new MapTile [4];
		double[][] collisionData = new double [4][2];
		MapTile[] tileList = new MapTile[4];
		for (int i = 0; i <= 6; i += 2) {
			setTileBuffer (hitbox.x + hitboxCorners [i] * hitbox.width, hitbox.y + hitboxCorners [i + 1] * hitbox.height, xTo + hitboxCorners [i] * hitbox.width, yTo + hitboxCorners [i + 1] * hitbox.height);
			if (tileBuffer.enabled) {
				collisionData [i / 2][0] = this.tileBuffer.collisionX;
				collisionData [i / 2][1] = this.tileBuffer.collisionY;
				if (collisionData [i / 2][0] >= hitbox.x + hitboxCorners [i]) {
					collisionData [i / 2][0] -= hitboxCorners [i] * hitbox.width;
				}
				if (collisionData [i / 2][1] >= hitbox.y * hitboxCorners [i + 1]) {
					collisionData [i / 2][1] -= hitboxCorners [i + 1] * hitbox.height;
				}
				MainLoop.getWindow ().getBuffer ().fillRect ((int)tileBuffer.collisionX, (int)tileBuffer.collisionY, 2, 2);
				collidingTiles [i / 2] = new MapTile (tileBuffer.mapTile.tileId, tileBuffer.mapTile.x, tileBuffer.mapTile.y);
			}
		}
		int closestTile = -1;
		double minDist = -1;
		double currentDist;
		for (int i = 0; i < 4; i ++) {
			//System.out.println(collisionData[i][1]);
			if (collidingTiles [i] != null) {
				currentDist = (collisionData [i][0] - hitbox.x) * (collisionData [i][0] - hitbox.x) + (collisionData [i][1] - hitbox.y) * (collisionData [i][1] - hitbox.y);
				if (currentDist <= minDist || minDist == -1) {
					minDist = currentDist;
					closestTile = i;
				}
			}
		}
		if (closestTile != -1) {
			this.tileBuffer.collisionX = collisionData [closestTile][0];
			this.tileBuffer.collisionY = collisionData [closestTile][1];
			this.tileBuffer.mapTile.x  = collidingTiles [closestTile].x;
			this.tileBuffer.mapTile.y = collidingTiles [closestTile].y;
			this.tileBuffer.mapTile.tileId = collidingTiles [closestTile].tileId;
			this.tileBuffer.spriteUsed = null; //Because I'm lazy
			return true;
			//Returns true for a successful collision
		} else {
			return false;
			//Returns false for no collision detected
		}
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
		int windowWidth = MainLoop.getWindow ().getResolution ()[0];
		int windowHeight = MainLoop.getWindow ().getResolution()[1];
		for (int i = 0; i < backgroundList.size (); i ++) {
			backgroundList.get (i).draw (viewX, viewY);
		}
		for (int layer = tileData.length - 1; layer >= 0; layer --) {
			for (int i = -(viewX % 16); i < windowWidth && i < levelWidth * 16; i += 16) {
				for (int j = (-viewY % 16); j < windowHeight && j < levelHeight * 16; j += 16) {
					tileList [tileData [layer][(viewX + i) / 16][(viewY + j) / 16]].draw (i, j);
				}
			}
		}
	}
	public void loadRoom (String path) throws FileNotFoundException {
		//Purges the gameObjects
		ArrayList<ArrayList<GameObject>> objList = MainLoop.getObjectMatrix ().objectMatrix;
		for (int i = 0; i < objList.size (); i ++) {
			if (objList.get (i) != null) {
				int listSize = objList.get (i).size ();
				for (int j = 0; j < listSize; j ++) {
					if (objList.get (i).get (j) != null) {
						objList.get (i).get (j).forget ();
					}
				}
			}
		}
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
	public boolean isBetween (double num, double bound1, double bound2) {
		//Returns true if num is between bound1 and bound2
		if (bound1 >= bound2) {
			double temp = bound2;
			bound2 = bound1;
			bound1 = temp;
		}
		return (num >= bound1 && num <= bound2);
	}
	public ArrayList<Background> getBackgroundList () {
		return backgroundList;
	}
	public double getGravity () {
		return this.gravity;
	}
	public void setGravity (double gravity) {
		this.gravity = gravity;
	}
}