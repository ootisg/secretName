package gameObjects;

import players.Jeffrey;

public class CreepyButterfly extends Enemy {
	//This class is not yet commented
	private double x;
	private double y;
	public CreepyButterfly () {
		setSprite (sprites.butterflySprite);
		createHitbox (0, 0, 16, 16);
		getAnimationHandler ().setAnimationSpeed (.6);
		player = (Jeffrey) getObject (getTypeId ("players.Jeffrey"), 0);
		x = -1;
		y = -1;
		this.health = 1;
		this.defence = 0;
	}
	@Override
	public void enemyFrame () {
		if (x == -1) {
			x = this.getX ();
			y = this.getY ();
		}
		double targetX = player.getX ();
		double targetY = player.getY ();
		if (Math.sqrt ((x - targetX) * (x - targetX) + (y - targetY) * (y - targetY)) <= 128) {
			double slope = (y - targetY) / (x - targetX);
			double angle = Math.atan (slope);
			if (x > targetX) {
				angle -= Math.PI;
			}
			x = x + Math.cos (angle) * 3;
			y = y + Math.sin (angle) * 3;
			setX ((int) x);
			setY ((int) y);
		}
	}
}