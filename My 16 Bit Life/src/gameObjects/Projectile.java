package gameObjects;

import main.GameObject;
import resources.Sprite;

public abstract class Projectile extends GameObject {
	protected double direction = 0;
	protected double speed = 0;
	@Override
	public void frameEvent () {
		projectileFrame ();
		this.setX (this.getX () + Math.cos (direction) * speed);
		this.setY (this.getY () + Math.sin (direction) * speed);
	}
	public double getDirection () {
		return this.direction;
	}
	public double getSpeed () {
		return this.speed;
	}
	public void setDirection (double direction) {
		this.direction = direction;
	}
	public void setSpeed (double speed) {
		this.speed = speed;
	}
	public void setAttributes (double x, double y, double direction, double speed) {
		this.setX (x);
		this.setY (y);
		this.direction = direction;
		this.speed = speed;
	}
	public void setAttributes (double x, double y, double direction) {
		this.setX (x);
		this.setY (y);
		this.direction = direction;
	}
	public void projectileFrame () {
		
	}
}