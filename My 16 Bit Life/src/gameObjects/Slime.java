package gameObjects;

import main.GameObject;

public class Slime extends GameObject {
	private int lifespan;
	public Slime () {
		this.lifespan = 200;
		this.setSprite (sprites.slime);
		this.createHitbox (0, 0, 2, 2);
	}
	@Override
	public void frameEvent () {
		lifespan --;
		if (lifespan <= 0) {
			this.forget ();
		}
	}
}