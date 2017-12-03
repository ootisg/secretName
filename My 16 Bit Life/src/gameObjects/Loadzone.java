package gameObjects;

import java.io.FileNotFoundException;

import main.GameObject;
import main.MainLoop;
import main.ObjectMatrix;

public class Loadzone extends GameObject {
	String destination;
	public Loadzone () {
		this.createHitbox (0, 0, 16, 16);
		this.destination = "";
	}
	public void loadMap () {
		try {
			room.loadRoom (destination);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
