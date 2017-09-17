package gameObjects;

import resources.Sprite;

public class DuoflyMinus extends Enemy {
	//This class is not yet commented
	int deathcount;
	Sprite deathSprite;
	public DuoflyMinus () {
		deathcount = -60;
		this.deathSprite = getSprites ().duoflyMinusDeath;
		getAnimationHandler ().setAnimationSpeed (.5);
		setSprite (getSprites ().duoflyMinus);
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
			setX (getX () + 3);
		} else if (deathcount == 9) {
			forget ();
		}
		if (deathcount > -1) {
			deathcount ++;
		}
		if (isColliding ("DuoflyPlus")) {
			deathcount = 0;
		}
	}
}