package players;

import weapons.AimableWeapon;
import items.Inventory;
import items.RedBlackPaintBall;
import main.GameObject;
import main.GameWindow;
import main.MainLoop;
import projectiles.Paintball;
import resources.Sprite;

public class Jeffrey extends GameObject {
	public double health;
	public double maxHealth;
	private boolean isWalking;
	public  boolean isJumping;
	public Sprite standSprite;
	public Sprite walkSprite;
	private Sprite redblack_gun;
	private AimableWeapon wpn;
	private int cooldown;
	private int invulTimer;
	private int specialCooldown;
	public static double vx;
	public static double vy;
	private double ax;
	private double ay;
	public static boolean onLadder;
	public Inventory inventory;
	public RedBlackPaintBall testball;
	private int textTimer;
	public static boolean standingOnPlatform;
	public Jeffrey () {
		//This class is not yet commented
		this.declare (0, 0);
		testball = new RedBlackPaintBall ();
		inventory = new Inventory();
		standingOnPlatform = false;
		this.standSprite = getSprites ().jeffreyIdle;
		this.walkSprite = getSprites ().jeffreyWalking;
		setSprite (standSprite);
		getAnimationHandler ().setAnimationSpeed (.7);
		createHitbox (4, 4, 7, 27);
		redblack_gun = new Sprite ("resources/sprites/redblack_gun.png");
		wpn = new AimableWeapon (redblack_gun);
		wpn.declare (0, 0);
		this.cooldown = 0;
		this.specialCooldown = 0;
		this.health = 100;
		this.maxHealth = 100;
		this.invulTimer = 0;
		this.vx = 0;
		this.vy = 0;
		this.ax = 0;
		this.ay = 0;
		textTimer = 0;
	}
	@Override
	public void frameEvent () {
		//Handles weapon usage
		if (this.cooldown > 0) {
			this.cooldown --;
		}
		if (mouseClicked () && cooldown == 0 ) {
			if (inventory.checkAmmo(testball)) {
			wpn.shoot (new Paintball ());
			inventory.removeItem(testball);
			cooldown = 5;
			} else {
				textTimer = 10;
				//sprites.outtaAmmo.draw((int)this.getX() - room.getViewX(), (int)this.getY() - 10);
			}
		}
		if (textTimer > 0) {
			//sprites.outtaAmmo.draw((int)this.getX() - room.getViewX(), (int)this.getY() - 10);
			textTimer = textTimer - 1;
		}
		//Gravity and collision with floor
		if (keyPressed (32) && !isJumping && vy == 0 && !onLadder) {
			isJumping = true;
			vy = -10.15625;
			setSprite (walkSprite);
			getAnimationHandler ().setAnimationSpeed (0);
			getAnimationHandler ().setFrame (3);
		}
		if (vy == 0) {
			getAnimationHandler ().setAnimationSpeed (.7);
		}
		if (!onLadder) {
			if (!standingOnPlatform) {
		vy += room.getGravity ();
			}
		}
		if (vy > 15.0) {
			vy = 15.0;
		}
		setY (getY () + (int) Math.ceil (vy));
		if (room.isColliding (this.getHitbox ())) {
			vy = 0;
			double fc = .2; //Friction coefficient
			if (vx > 0) {
				vx -= fc;
				if (vx < 0) {
					vx = 0;
				}
			} else if (vx < 0) {
				vx += fc;
				if (vx > 0) {
					vx = 0;
				}
			}
			boolean[][] collidingTiles = room.getCollidingTiles (this.getHitbox ());
			int tileY = 0;
			this.setY (this.getY() + vy);
			for (int i = 0; i < collidingTiles.length; i ++) {
				if (collidingTiles [i][collidingTiles [0].length - 1]) {
					tileY = (((int) getY () + this.getHitboxYOffset ()) / 16) + collidingTiles [0].length - 1;
					this.setY (tileY * 16 - 32);
					this.vy = 0;
					isJumping = false;
					break;
				}
				if (collidingTiles [i][0]) {
					this.setY ((int)((getY () + this.getHitboxYOffset ()) / 16) * 16 + 16 - this.getHitboxYOffset ());
					break;
				}
			}
		}
		if (!onLadder) {
		if (keyCheck ('A')) {
			if (vx >= -3.0) {
				ax = -.5;
			}
			setFlipHorizontal (true);
			if (vy == 0 && !isWalking) {
				isWalking = true;
				setSprite (walkSprite);
			}
		} else if (keyCheck ('D')) {
			if (vx <= 3.0) {
				ax = .5;
			}
			setFlipHorizontal (false);
			if (vy == 0 && !isWalking) {
				isWalking = true;
				setSprite (walkSprite);
			}
		} else {
			if (isWalking) {
				isWalking = false;
				setSprite (standSprite);
			}
			if (!isJumping && vy == 0) {
				isJumping = false;
				setSprite (standSprite);
			}
		}
		}
		vx = vx + ax;
		vy = vy + ay;
		ax = 0;
		this.setX (this.getX () + vx);
		if (room.isColliding (this.getHitbox ())) {
			this.backstepX ();
			vx = 0;
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
		if (getFlipHorizontal ()) {
			wpn.setX (this.getX () - 5);
			wpn.setY (this.getY () + 16);
			wpn.setFlipHorizontal (true);
		} else {
			wpn.setX (this.getX () + 11);
			wpn.setY (this.getY () + 16);
			wpn.setFlipHorizontal (false);
		}
		//Handles weapon aiming
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
		//Damage animation
		if (invulTimer != 0) {
			if ((invulTimer / 2) % 2 == 1) {
				this.hide ();
			} else {
				this.show ();
			}
		}
		//Handles invulnerability
		if (invulTimer > 0) {
			invulTimer --;
		}
		if (this.health <= 0) {
			this.health = this.maxHealth;
			MainLoop.getConsole ().enable ("You died, and I'm too lazy to put anything in for that. :P");
		}
	}
	public void damage (double baseDamage) {
		if (invulTimer == 0) {
			health -= baseDamage;
			invulTimer = 15;
		}
	}
	public Inventory getInventory () {
		return this.inventory;
	}
	public double getHealth () {
		return this.health;
	}
	@Override
	public void forget () {
		
	}
}