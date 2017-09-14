package projectiles;

import gameObjects.Projectile;

public class Paintball extends Projectile {
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
		if (this.isColliding ("gameObjects.CreepyButterfly", xTo, yTo)) {
			this.getCollidingObject ("gameObjects.CreepyButterfly", xTo, yTo).forget ();
		}
		if (room.isColliding (getX (), getY (), xTo, yTo)) {
			this.forget ();
		}
	}
}
