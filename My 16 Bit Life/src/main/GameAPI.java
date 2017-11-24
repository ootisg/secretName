package main;

import graphics3D.RayCamera;
import map.Room;
import resources.SpriteContainer;

public abstract class GameAPI {
	protected static Room room = new Room (); //Makes the static instance of the room object
	protected static RayCamera rayCamera = new RayCamera (160, 160, 0);
	protected static SpriteContainer sprites = MainLoop.getSprites (); //Makes the static instance of the sprite container object
	public boolean keyCheck (int keyCode) {
		//Returns true if the key with an ASCII code of keyCode is pressed down
		return MainLoop.getWindow ().keyCheck (keyCode);
	}
	public boolean keyPressed (int keyCode) {
		//Returns true if the key with an ASCII code of keyCode was pressed down this frame
		return MainLoop.getWindow ().keyPressed (keyCode);
	}
	public boolean keyReleased (int keyCode) {
		//Returns true if the key with an ASCII code of keyCode was released this frame
		return MainLoop.getWindow ().keyReleased (keyCode);
	}
	public SpriteContainer getSprites () {
		//Returns the sprite container
		return MainLoop.getSprites ();
	}
	public GameObject getObject (int x, int y) {
		//Gets the object from the object matrix with the position (x, y)
		return MainLoop.getObjectMatrix ().get (x, y);
	}
	public int getTypeId (String objectName) {
		//Gets the x index for the object matrix corresponding to a specific object name
		return MainLoop.getObjectMatrix ().getTypeId (objectName);
	}
	public int getMouseX () {
		//Get the current mouse x-coordinate on the screen
		return MainLoop.getWindow ().getMouseX ();
	}
	public int getMouseY () {
		//Get the current mouse y-coordinate on the screen
		return MainLoop.getWindow ().getMouseY ();
	}
	public int[] getClick () {
		//Returns the coordinates of the most recent click in the format [x, y]. Returns null if there was not a click this frame.
		return MainLoop.getWindow ().getClick ();
	}
	public boolean mouseClicked () {
		//Returns true if the mouse was clicked this frame
		if (MainLoop.getWindow ().getClick () == null) {
			return false;
		}
		return true;
	}
}