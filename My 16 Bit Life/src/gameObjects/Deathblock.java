package gameObjects;

import main.GameObject;
import main.MainLoop;
import players.Jeffrey;

public class Deathblock extends GameObject {
	public static Jeffrey player = (Jeffrey) MainLoop.getObjectMatrix ().get (MainLoop.getObjectMatrix ().getTypeId ("players.Jeffrey"), 0);
public Deathblock (){
	this.createHitbox(0, 0, 16, 16);
	}
	@Override
	public void frameEvent () {
		if (player.isColliding(this)){
			player.damage(Double.POSITIVE_INFINITY);
		}
	}
}