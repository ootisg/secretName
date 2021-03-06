package gameObjects;

import main.GameCode;
import main.GameObject;
import map.MapTile;
import map.MapTileCollision;
import players.Jeffrey;

public class Ladder extends GameObject {
	MapTile barrier;
	MapTileCollision barrier2EB;
	public Ladder () {
		this.setSprite(sprites.ladder);
		this.createHitbox(0, 0, 8, 8);
	}
	public  void frameEvent () {
		barrier = new MapTile ("", (int)this.getX(), (int)this.getY());
		barrier2EB = new MapTileCollision (barrier, 16, 1);
		if (this.isColliding(GameCode.testJeffrey) && keyPressed('W')) {
			Jeffrey.onLadder = true;
			Jeffrey.vy = 0;
			Jeffrey.vx = 0;
			GameCode.testJeffrey.setX(this.getX());
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

