package gameObjects;

import main.GameCode;
import main.GameObject;
import players.Jeffrey;

public class Ladder extends GameObject {
	public Ladder () {
		this.setSprite(sprites.ladder);
		this.createHitbox(0, 0, 8, 8);
	}
	
	public  void frameEvent () {
		if (this.isColliding(GameCode.testJeffrey) && keyPressed('W')) {
			Jeffrey.onLadder = true;
			Jeffrey.vy = 0;
			Jeffrey.vx = 0;
		}
		if (Jeffrey.onLadder && keyPressed ('W') && this.isColliding (GameCode.testJeffrey)) {
			GameCode.testJeffrey.setY(GameCode.testJeffrey.getY() -3);
		}
		if (Jeffrey.onLadder && keyPressed ('S')) {
			GameCode.testJeffrey.setY(GameCode.testJeffrey.getY() + 3);
		}
		if (Jeffrey.onLadder && (keyPressed (32))){
			Jeffrey.onLadder = false;
		}
	}
}

