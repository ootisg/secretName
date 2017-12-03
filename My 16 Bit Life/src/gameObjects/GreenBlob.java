package gameObjects;

public class GreenBlob extends Enemy {
	boolean moveRight;
	public GreenBlob () {
		this.setSprite (sprites.blob);
		this.getAnimationHandler ().setAnimationSpeed (.2);
		this.moveRight = true;
		this.createHitbox (1, 3, 13, 12);
		this.health = 2;
	}
	@Override
	public void enemyFrame () {
		if (this.moveRight) {
			setX (getX () + 17);
			setY (getY () + 16);
			if (!room.isColliding (this.getHitbox ())) {
				this.moveRight = false;
			}
			setX (getX () - 16);
			setY (getY () - 16);
			if (room.isColliding (this.getHitbox())) {
				this.moveRight = false;
				setX (getX () - 1);
			}
		} else {
			setX (getX () - 17);
			setY (getY () + 16);
			if (!room.isColliding (this.getHitbox ())) {
				this.moveRight = true;
			}
			setX (getX () + 16);
			setY (getY () - 16);
			if (room.isColliding (this.getHitbox())) {
				this.moveRight = true;
				setX (getX () + 1);
			}
		}
	}
	@Override
	public void attackEvent () {
		player.damage (this.baseDamage);
	}
}
