package enemies;

public class Slimelet extends Enemy {
	public Slimelet () {
		this.setSprite (sprites.slimeletHorizontal);
		this.getAnimationHandler ().setAnimationSpeed (.1);
		this.createHitbox (0, 0, 16, 16);
	}
	@Override
	public void enemyFrame () {
		this.setX (this.getX () + .7);
		new Slime ().declare (this.getX () + 2, this.getY () + 15);
	}
}
