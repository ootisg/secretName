package gameObjects;

import main.GameCode;
import main.GameObject;
import players.Jeffrey;

public class LaunchPad extends GameObject {
	boolean onTop;
	public LaunchPad () {
		this.setSprite(sprites.ladder);
		this.createHitbox(0, 0, 8, 8);
		onTop = false;
	}
	
	public  void frameEvent () {
		if (this.isColliding(GameCode.testJeffrey) && keyPressed('W')) {
			Jeffrey.onLadder = true;
			Jeffrey.vy = 0;
			Jeffrey.vx = 0;
			GameCode.testJeffrey.setX(this.getX());
		}
		if (GameCode.testJeffrey.getY() <= this.getY()  && this.isColliding(GameCode.testJeffrey)) {
			Jeffrey.onLadder = false;
			Jeffrey.standingOnPlatform = true;
		}
		if (Jeffrey.standingOnPlatform && (keyPressed (32))) {
			Jeffrey.standingOnPlatform = false;
		}
		if (Jeffrey.onLadder && keyCheck ('W') && this.isColliding (GameCode.testJeffrey)) {
			GameCode.testJeffrey.setY(GameCode.testJeffrey.getY() -3);
		}
		if (Jeffrey.onLadder && keyCheck ('S') && this.getY() >= GameCode.testJeffrey.getY()) {
			GameCode.testJeffrey.setY(GameCode.testJeffrey.getY() + 3);
		}
		if (Jeffrey.onLadder && (keyPressed (32))){
			Jeffrey.onLadder = false;
		}
	}
}
