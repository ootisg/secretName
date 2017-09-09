package main;

import map.Room;
import resources.SpriteContainer;

public abstract class GameAPI {
	protected static Room room = new Room ();
	protected static SpriteContainer sprites = MainLoop.getSprites ();
	public boolean keyCheck (int keyCode) {
		return MainLoop.getWindow ().keyCheck (keyCode);
	}
	public boolean keyPressed (int keyCode) {
		return MainLoop.getWindow ().keyPressed (keyCode);
	}
	public boolean keyReleased (int keyCode) {
		return MainLoop.getWindow ().keyReleased (keyCode);
	}
	public String[] parseFilePaths (String fileList) {
		char[] stringChars = fileList.toCharArray ();
		int stringCharsLength = stringChars.length;
		int fileCount = 1;
		for (int i = 0; i < stringCharsLength; i ++) {
			if (stringChars [i] == ',') {
				fileCount ++;
			}
		}
		String[] fileArray = new String[fileCount];
		String parsedFile = "";
		int fileArrayPosition = 0;
		for (int i = 0; i < stringCharsLength; i ++) {
			if (stringChars [i] != ',') {
				parsedFile += stringChars [i];
			} else {
				fileArray [fileArrayPosition] = parsedFile;
				parsedFile = "";
				fileArrayPosition ++;
			}
		}
		fileArray [fileArrayPosition] = parsedFile;
		return fileArray;
	}
	public SpriteContainer getSprites () {
		return MainLoop.getSprites ();
	}
	public GameObject getObject (int x, int y) {
		return MainLoop.getObjectMatrix ().get (x, y);
	}
	public int getTypeId (String objectName) {
		return MainLoop.getObjectMatrix ().getTypeId (objectName);
	}
	public int getMouseX () {
		return MainLoop.getWindow ().getMouseX ();
	}
	public int getMouseY () {
		return MainLoop.getWindow ().getMouseY ();
	}
	public int[] getClick () {
		return MainLoop.getWindow ().getClick ();
	}
	public boolean mouseClicked () {
		if (MainLoop.getWindow ().getClick () == null) {
			return false;
		}
		return true;
	}
}