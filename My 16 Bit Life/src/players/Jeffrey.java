package players;

import gameObjects.AimableWeapon;
import main.GameObject;
import main.GameWindow;
import main.MainLoop;
import projectiles.Paintball;
import resources.Sprite;

public class Jeffrey extends GameObject {
	private double fallSpeed;
	private boolean isWalking;
	private boolean isJumping;
	private Sprite standSprite;
	private Sprite walkSprite;
	private Sprite redblack_gun;
	private TubeRaster raster;
	private AimableWeapon wpn;
	private int cooldown;
	private int specialCooldown;
	public Jeffrey () {
		this.declare (0, 0);
		this.standSprite = getSprites ().jeffreyIdle;
		this.walkSprite = getSprites ().jeffreyWalking;
		setSprite (standSprite);
		getAnimationHandler ().setAnimationSpeed (.7);
		createHitbox (4, 4, 7, 27);
		raster = new TubeRaster (0xFF0000FF, 1, 0, 16, 16, 0, 0, 15, 16);
		raster.declare(0, 0);
		redblack_gun = new Sprite ("resources/sprites/redblack_gun.png");
		wpn = new AimableWeapon (redblack_gun);
		wpn.declare (0, 0);
		this.cooldown = 0;
		this.specialCooldown = 0;
	}
	@Override
	public void frameEvent () {
		if (this.cooldown > 0) {
			this.cooldown --;
		}
		//I honestly have no idea why this section only works here instead of at the end
		if (getFlipHorizontal ()) {
			wpn.setX (this.getX () - 5);
			wpn.setY (this.getY () + 16);
			wpn.setFlipHorizontal (true);
		} else {
			wpn.setX (this.getX () + 11);
			wpn.setY (this.getY () + 16);
			wpn.setFlipHorizontal (false);
		}
		double wpnX = wpn.getX () - room.getViewX ();
		double wpnY = wpn.getY () - room.getViewY ();
		int mouseX = this.getMouseX ();
		int mouseY = this.getMouseY ();
		/*int wpnX = 32;
		int wpnY = 32;
		int mouseX = 64;
		int mouseY = 48;*/
		if (wpnX - mouseX != 0) {
			double ang = Math.atan ((wpnY - mouseY) / (wpnX - mouseX));
			GameWindow wind = MainLoop.getWindow ();
			if (mouseX < wpnX) {
				ang *= -1;
				if (ang < -Math.PI / 4) {
					ang = - Math.PI / 4;
				}
				if (ang > Math.PI / 4) {
					ang = Math.PI / 4;
				}
			} else {
				if (ang < -Math.PI / 4) {
					ang = - Math.PI / 4;
				}
				if (ang > Math.PI / 4) {
					ang = Math.PI / 4;
				}
			}
			wpn.setRotation (ang);
		}
		if (mouseClicked () && cooldown == 0) {
			wpn.shoot (new Paintball ());
			cooldown = 5;
		}
		//End of super mysterious section
		if (keyPressed (0x57) && !isJumping && fallSpeed == 0) {
			isJumping = true;
			fallSpeed = -10.15625;
			setSprite (walkSprite);
			getAnimationHandler ().setAnimationSpeed (0);
			getAnimationHandler ().setFrame (3);
		}
		if (fallSpeed == 0) {
			getAnimationHandler ().setAnimationSpeed (.7);
		}
		fallSpeed += .65625;
		if (fallSpeed > 15) {
			fallSpeed = 15;
		}
		setY (getY () + (int) Math.ceil (fallSpeed));
		if (room.isColliding (this.getHitbox ())) {
			fallSpeed = 0;
			boolean[][] collidingTiles = room.getCollidingTiles (this.getHitbox ());
			int tileY = 0;
			for (int i = 0; i < collidingTiles.length; i ++) {
				if (collidingTiles [i][collidingTiles [0].length - 1]) {
					tileY = (((int) getY () + this.getHitboxYOffset ()) / 16) + collidingTiles [0].length - 1;
					this.setY (tileY * 16 - 32);
					this.fallSpeed = 0;
					isJumping = false;
					break;
				}
				if (collidingTiles [i][0]) {
					this.setY (((getY () + this.getHitboxYOffset ()) / 16) * 16 + 16 - this.getHitboxYOffset ());
					break;
				}
			}
		}
		if (keyCheck (0x41)) {
			setX (getX () - 3);
			setFlipHorizontal (true);
			if (fallSpeed == 0 && !isWalking) {
				isWalking = true;
				setSprite (walkSprite);
			}
		} else if (keyCheck (0x44)) {
			setX (getX () + 3);
			setFlipHorizontal (false);
			if (fallSpeed == 0 && !isWalking) {
				isWalking = true;
				setSprite (walkSprite);
			}
		} else {
			if (isWalking) {
				isWalking = false;
				setSprite (standSprite);
			}
			if (!isJumping && fallSpeed == 0) {
				isJumping = false;
				setSprite (standSprite);
			}
		}
		if (room.isColliding (this.getHitbox ())) {
			this.backstepX ();
		}
		double x = this.getX ();
		double y = this.getY ();
		int viewX = room.getViewX ();
		int viewY = room.getViewY ();
		if (y - viewY >= 320 && y - 320 < room.getHeight () * 16 - 480) {
			viewY = (int) y - 320;
			room.setView (room.getViewX (), viewY);
		}
		if (y - viewY <= 160 && y - 160 > 0) {
			viewY = (int) y - 160;
			room.setView (room.getViewX (), viewY);
		}
		if (x - viewX >= 427 && x - 427 < room.getWidth () * 16 - 640) {
			viewX = (int) x - 427;
			room.setView (viewX, room.getViewY ());
		}
		if (x - viewX <= 213 && x - 213 > 0) {
			viewX = (int) x - 213;
			room.setView (viewX, room.getViewY ());
		}
	}
}