package players;

import main.GameObject;

public class TopDown extends GameObject {
	public TopDown () {
		this.declare (0, 0);
		this.createHitbox (0, 0, 16, 16);
	}
	@Override
	public void frameEvent () {
		double xprev = getX ();
		double yprev = getY ();
		if (!(keyCheck ('W') || keyCheck ('A') || keyCheck ('S') || keyCheck ('D'))) {
			if (keyReleased ('W')) {
				this.setSprite (sprites.tdIdle);
				this.getAnimationHandler ().setFrame (0);
				this.getAnimationHandler ().setAnimationSpeed (0);
			}
			if (keyReleased ('A')) {
				this.setSprite (sprites.tdIdle);
				this.getAnimationHandler ().setFrame (1);
				this.getAnimationHandler ().setAnimationSpeed (0);
			}
			if (keyReleased ('S')) {
				this.setSprite (sprites.tdIdle);
				this.getAnimationHandler ().setFrame (2);
				this.getAnimationHandler ().setAnimationSpeed (0);
			}
			if (keyReleased ('D')) {
				this.setSprite (sprites.tdIdle);
				this.getAnimationHandler ().setFrame (3);
				this.getAnimationHandler ().setAnimationSpeed (0);
			}
		} else {
			if (keyCheck ('W')) {
				if (this.getAnimationHandler ().getSprite () != sprites.tdWalk [0]) {
					this.setSprite (sprites.tdWalk [0]);
				}
				this.setX (this.getX ());
				this.setY (this.getY () - 3);
			}
			if (keyCheck ('S')) {
				if (this.getAnimationHandler ().getSprite () != sprites.tdWalk [2]) {
					this.setSprite (sprites.tdWalk [2]);
				}
				this.setX (this.getX ());
				this.setY (this.getY () + 3);
			}
			if (keyCheck ('A')) {
				if (this.getAnimationHandler ().getSprite () != sprites.tdWalk [1]) {
					this.setSprite (sprites.tdWalk [1]);
				}
				this.setX (this.getX () - 3);
				this.setY (this.getY ());
			}
			if (keyCheck ('D')) {
				if (this.getAnimationHandler ().getSprite () != sprites.tdWalk [3]) {
					this.setSprite (sprites.tdWalk [3]);
				}
				this.setX (this.getX () + 3);
				this.setY (this.getY ());
			}
			this.getAnimationHandler ().setAnimationSpeed (.25);
		}
		if (room.isColliding (this.getHitbox ())) {
			this.setX (xprev);
			this.setY (yprev);
		}
	}
}