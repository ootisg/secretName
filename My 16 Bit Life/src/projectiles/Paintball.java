package projectiles;

import gameObjects.Projectile;
import gameObjects.Enemy;

public class Paintball extends Projectile {
	//This class is not yet commented
	public Paintball () {
		this.setSprite (getSprites ().paintball);
		this.setSpeed (20);
		this.declare (0, 0);
		this.createHitbox (0, 0, 4, 4);
	}
	@Override
	public void projectileFrame () {
		double xTo = this.getX () + Math.cos (direction) * speed;
		double yTo = this.getY () + Math.sin (direction) * speed;
		for (int i = 0; i < Enemy.enemyList.length; i ++) {
			if (this.isColliding (Enemy.enemyList [i], xTo, yTo)) {
				Enemy target = (Enemy) this.getCollidingObject (Enemy.enemyList [i], xTo, yTo);
				target.damage (1);
				this.forget ();
			}
		}
		if (room.isColliding (this.getHitbox (), xTo, yTo)) {
			this.forget ();
		}
	}
}
