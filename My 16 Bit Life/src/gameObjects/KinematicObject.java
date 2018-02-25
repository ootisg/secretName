package gameObjects;

import main.GameObject;

public class KinematicObject extends GameObject {
	private double vx;
	private double vy;
	private double ax;
	private double ay;
	public void physicsFrame () {
		vy += room.getGravity ();
		vy += ay;
		room.setTileBuffer (this.getHitbox (), this.getX (), this.getY () + vy);
		if (room.tileBuffer.enabled) {
			if (room.tileBuffer.collisionX == (double)(room.tileBuffer.mapTile.x * 16)) {
				
			}
			if (room.tileBuffer.collisionX == (double)(room.tileBuffer.mapTile.x * 16 - 16)) {
				
			}
			System.out.println (room.tileBuffer.collisionY);
			if (room.tileBuffer.collisionY == (double)(room.tileBuffer.mapTile.y * 16)) {
				double[] collidingCoords = room.getCollidingCoords (this.getHitbox (), this.getX (), this.getY () + vy);
				this.vy = (collidingCoords [1] - this.getX ());
			}
			if (room.tileBuffer.collisionY == (double)(room.tileBuffer.mapTile.y * 16 - 16)) {
				
			}
		} else {
			
		}
		this.setX (this.getX () + vx);
		this.setY (this.getY () + vy);
	}
	public double getVelocityX () {
		return vx;
	}
	public double getVelocityY () {
		return vy;
	}
	public double getAccelerationX () {
		return ax;
	}
	public double getAccelerationY () {
		return ay;
	}
	public void setVelocityX (double vx) {
		this.vx = vx;
	}
	public void setVelocityY (double vy) {
		this.vy = vy;
	}
	public void setAccelerationX (double ax) {
		this.ax = ax;
	}
	public void setAccelerationY (double ay) {
		this.ay = ay;
	}
}
