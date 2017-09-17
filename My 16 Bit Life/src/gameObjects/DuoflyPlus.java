package gameObjects;

import resources.Sprite;

public class DuoflyPlus extends Enemy {
	//This class is not yet commented
	int deathcount;
	Sprite deathSprite;
	public DuoflyPlus () {
		deathcount = -1;
		this.deathSprite = getSprites ().duoflyPlusDeath;
		getAnimationHandler ().setAnimationSpeed (.5);
		setSprite (getSprites ().duoflyPlus);
		createHitbox (0, 0, 32, 32);
	}
	@Override
	public void frameEvent () {
		if (deathcount == 0) {
			setSprite (deathSprite);
			getAnimationHandler ().setFrame (0);
			destroyHitbox ();
		}
		if (deathcount <= 0) {
			setX (getX () - 3);
		} else if (deathcount == 9) {
			forget ();
		}
		if (deathcount > -1) {
			deathcount ++;
		}
		if (isColliding ("DuoflyMinus")) {
			deathcount = 0;
			
		}
	}
}