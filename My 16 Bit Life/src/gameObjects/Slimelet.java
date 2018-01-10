package gameObjects;

import main.MainLoop;

public class Slimelet extends Enemy {
	private byte direction;
	private byte animation;
	private int animationTimer;
	private int animationFrames;
	private int climbTimer;
	private int climbFrames;
	private int slimeX;
	private int slimeY;
	private boolean altFrame;
	private boolean converse;
	private boolean conversePrevious;
	public Slimelet () {
		//this.setSprite (sprites.slimeletClimbHorizontal);
		this.getAnimationHandler ().setAnimationSpeed (.3);
		this.createHitbox (0, 0, 15, 15);
		this.animationFrames = 6;
		this.climbFrames = 1;
		this.direction = 0;
		this.animation = 0;
		this.converse = false;
		this.conversePrevious = false;
	}
	@Override
	public void enemyFrame () {
		//Random numbers correspond to sprite coordinates
		int slimeOffset;
		if (this.animation != 2) {
			slimeOffset = (climbTimer + 1) / climbFrames;
		} else {
			slimeOffset = 0;
		}
		if (!this.conversePrevious) {
			switch (this.direction) {
				case 0:
					this.setHitboxRect (6, 7, 15, 15);
					this.slimeX = 3 + slimeOffset;
					this.slimeY = 15;
					break;
				case 1:
					this.setHitboxRect (0, 7, 9, 15);
					this.slimeX = 12 - slimeOffset;
					this.slimeY = 15;
					break;
				case 2:
					this.setHitboxRect (7, 0, 15, 9);
					this.slimeX = 15;
					this.slimeY = 12 - slimeOffset;
					break;
				case 3:
					this.setHitboxRect (7, 6, 15, 15);
					this.slimeX = 15;
					this.slimeY = 3 + slimeOffset;
					break;
			}
		} else {
			switch (this.direction) {
				case 0:
					this.setHitboxRect (6, 0, 15, 8);
					this.slimeX = 3 + slimeOffset;
					this.slimeY = 0;
					break;
				case 1:
					this.setHitboxRect (0, 0, 9, 8);
					this.slimeX = 12 - slimeOffset;
					this.slimeY = 0;
					break;
				case 2:
					this.setHitboxRect (0, 0, 8, 9);
					this.slimeX = 0;
					this.slimeY = 12 - slimeOffset;
					break;
				case 3:
					this.setHitboxRect (0, 6, 8, 15);
					this.slimeX = 0;
					this.slimeY = 3 + slimeOffset;
					break;
				}
		}
		if (this.animation == 0) {
			switch (this.direction) {
				case 0:
					this.setX (this.getX () + 1);
					if (room.isColliding (this.getHitbox ())) {
						this.backstepX ();
						this.animation = 1;
					}
					break;
				case 1:
					this.setX (this.getX () - 1);
					if (room.isColliding (this.getHitbox ())) {
						this.backstepX ();
						this.animation = 1;
					}
					break;
				case 2:
					this.setY (this.getY () - 1);
					if (room.isColliding (this.getHitbox ())) {
						this.backstepY ();
						this.animation = 1;
						this.converse = true;
					}
					break;
				case 3:
					this.setY (this.getY () + 1);
					if (room.isColliding (this.getHitbox ())) {
						this.backstepY ();
						this.animation = 1;
						this.converse = false;
					}
					break;
			}
			if (!this.conversePrevious) {
				switch (this.direction) {
					case 0:
						if (!this.roomIsCollidingOffset (this.getHitbox ().width, this.getHitbox ().height) && !room.isColliding (this.getHitbox ())) {
							this.animation = 2;
							this.setX (this.getX () + 8);
							this.setY (this.getY () + 5);
						}
						break;
					case 1:
						
						break;
					case 2:
						
						break;
					case 3:
						if (!this.roomIsCollidingOffset (-this.getHitbox ().width, this.getHitbox ().height) && !room.isColliding (this.getHitbox ())) {
							this.animation = 2;
							this.setX (this.getX () - 5);
							this.setY (this.getY () - 7);
						}
						break;
				}
			} else {
				switch (this.direction) {
					case 0:
						
						break;
					case 1:
						
						break;
					case 2:
						
						break;
					case 3:
						
						break;
					}
			}
		}
		if (this.animation == 2) {
			if (!this.conversePrevious) {
				switch (this.direction) {
					case 0:
						this.setY (this.getY () + 1);
						break;
					case 1:
						this.setY (this.getY () + 1);
						break;
					case 2:
						this.setX (this.getY () + 1);
						break;
					case 3:
						this.setX (this.getX () + 1);
						break;
				}
			} else {
				switch (this.direction) {
					case 0:
						this.setY (this.getY () - 1);
						break;
					case 1:
						this.setY (this.getY () - 1);
						break;
					case 2:
						this.setX (this.getX () - 1);
						break;
					case 3:
						this.setX (this.getX () - 1);
						break;
				}
			}
		}
		new Slime ().declare (this.getX () + this.slimeX, this.getY () + this.slimeY);
	}
	@Override
	public void draw () {
		//System.out.print ("DRAW: ");
		if (animationTimer % animationFrames == animationFrames - 1) {
			altFrame = !altFrame;
			animationTimer = 0;
		}
		this.setFlipVertical (false);
		this.setFlipHorizontal (false);
		if (direction == 1) {
			this.setFlipHorizontal (true);
		}
		if (direction == 3) {
			this.setFlipVertical (true);
		}
		if ((direction == 0 || direction == 1) && conversePrevious) {
			this.setFlipVertical (true);
		}
		if ((direction == 2 || direction == 3) && conversePrevious) {
			this.setFlipHorizontal (true);
		}
		if (this.animation == 2) {
			if (climbTimer >= climbFrames * 9) {
				climbTimer = 0;
				this.animation = 0;
				//System.out.println(this.converse);
				if (this.conversePrevious) {
					switch (this.direction) {
						case 0:
							this.direction = 2;
							this.converse = false;
							break;
						case 1:
							this.direction = 2;
							this.converse = true;
							break;
						case 2:
							this.direction = 1;
							this.converse = true;
							break;
						case 3:
							this.direction = 1;
							this.converse = false;
							break;
					}
				} else {
					switch (this.direction) {
						case 0:
							this.direction = 3;
							this.converse = true;
							this.setX (this.getX () + 7);
							this.setY (this.getY () - 4);
							break;
						case 1:
							this.direction = 3;
							this.converse = true;
							break;
						case 2:
							this.direction = 0;
							this.converse = true;
							break;
						case 3:
							this.direction = 0;
							this.converse = false;
							break;
					}
				}
				this.conversePrevious = this.converse;
				this.setFlipVertical (false);
				this.setFlipHorizontal (false);
				if (direction == 1) {
					this.setFlipHorizontal (true);
				}
				if (direction == 3) {
					this.setFlipVertical (true);
				}
				if ((direction == 0 || direction == 1) && converse) {
					this.setFlipVertical (true);
				}
				if ((direction == 2 || direction == 3) && converse) {
					this.setFlipHorizontal (true);
				}
				groundAnimationStep ();
			} else {
				int frameToDraw = (climbTimer / climbFrames);
				if (this.direction == 0 || this.direction == 1) {
					sprites.slimeletOver.setFrame (frameToDraw);
					sprites.slimeletOver.draw ((int)this.getX () - room.getViewX (), (int)this.getY () - room.getViewY (), this.getFlipHorizontal (), this.getFlipVertical ());
				} else {
					sprites.slimeletAround.setFrame (frameToDraw);
					sprites.slimeletAround.draw ((int)this.getX () - room.getViewX (), (int)this.getY () - room.getViewY (), this.getFlipHorizontal (), this.getFlipVertical ());
				}
				climbTimer ++;
			}
		} else if (this.animation == 1) {
			//System.out.println("1");
			if (climbTimer >= climbFrames * 9) {
				climbTimer = 0;
				this.animation = 0;
				//System.out.println(this.converse);
				if (this.conversePrevious) {
					switch (this.direction) {
						case 0:
							this.direction = 3;
							this.converse = false;
							this.setY (this.getY () - 5);
							break;
						case 1:
							this.direction = 3;
							this.converse = true;
							this.setY (this.getY () - 5);
							break;
						case 2:
							this.direction = 0;
							this.converse = true;
							this.setX (this.getX () - 5);
							break;
						case 3:
							this.direction = 0;
							this.converse = false;
							this.setX (this.getX () - 5);
							break;
					}
				} else {
					switch (this.direction) {
						case 0:
							this.direction = 2;
							this.converse = false;
							this.setY (this.getY () + 5);
							break;
						case 1:
							this.direction = 2;
							this.converse = true;
							this.setY (this.getY () + 5);
							break;
						case 2:
							this.direction = 1;
							this.converse = true;
							this.setX (this.getX () + 5);
							break;
						case 3:
							this.direction = 1;
							this.converse = false;
							this.setX (this.getX () + 5);
							break;
					}
				}
				this.conversePrevious = this.converse;
				this.setFlipVertical (false);
				this.setFlipHorizontal (false);
				if (direction == 1) {
					this.setFlipHorizontal (true);
				}
				if (direction == 3) {
					this.setFlipVertical (true);
				}
				if ((direction == 0 || direction == 1) && converse) {
					this.setFlipVertical (true);
				}
				if ((direction == 2 || direction == 3) && converse) {
					this.setFlipHorizontal (true);
				}
				groundAnimationStep ();
			} else {
				int frameToDraw = 0;
				if (altFrame) {
					frameToDraw = 9;
				}
				frameToDraw += (climbTimer / climbFrames);
				if (this.direction == 0 || this.direction == 1) {
					sprites.slimeletClimbHorizontal.setFrame (frameToDraw);
					sprites.slimeletClimbHorizontal.draw ((int)this.getX () - room.getViewX (), (int)this.getY () - room.getViewY (), this.getFlipHorizontal (), this.getFlipVertical ());
				} else {
					sprites.slimeletClimbVertical.setFrame (frameToDraw);
					sprites.slimeletClimbVertical.draw ((int)this.getX () - room.getViewX (), (int)this.getY () - room.getViewY (), this.getFlipHorizontal (), this.getFlipVertical ());
				}
				climbTimer ++;
			}
		} else if (animation == 0) {
			groundAnimationStep ();
		}
		animationTimer ++;
		//MainLoop.getWindow ().getBuffer ().drawRect ((int)(this.getX () + this.getHitboxXOffset ()), (int)(this.getY () + this.getHitboxYOffset ()), this.getHitbox ().width, this.getHitbox ().height);
	}
	public void groundAnimationStep () {
		if (altFrame) {
			sprites.slimeletHorizontal.setFrame (1);
			sprites.slimeletVertical.setFrame (1);
		} else {
			sprites.slimeletHorizontal.setFrame (0);
			sprites.slimeletVertical.setFrame (1);
		}
		if (direction == 0 || direction == 1) {
			sprites.slimeletHorizontal.draw ((int)getX (), (int)getY (), this.getFlipHorizontal (), this.getFlipVertical ());
		}
		if (direction == 2 || direction == 3) {
			sprites.slimeletVertical.draw ((int)getX (), (int)getY (), this.getFlipHorizontal (), this.getFlipVertical ());
		}
	}
	public void setHitboxRect (int x1, int y1, int x2, int y2) {
		this.setHitboxXOffset (x1);
		this.setHitboxYOffset (y1);
		this.getHitbox ().width = x2 - x1;
		this.getHitbox ().height = y2 - y1;
	}
	public boolean roomIsCollidingOffset (double offsetX, double offsetY) {
		this.setX (this.getX () + offsetX);
		this.setY (this.getY () + offsetY);
		if (room.isColliding (this.getHitbox ())) {
			this.backstep ();
			return true;
		} else {
			this.backstep ();
			return false;
		}
	}
}
