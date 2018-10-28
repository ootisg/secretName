package gameObjects;

import weapons.AimableWeapon;

public class CyclopesCrab extends Enemy {
	private boolean moveRight;
	private boolean turrning;
	private boolean chillForASecond;
	private int finishedAnimationFrames;
	private int momentum;
	private boolean stuck;
	private boolean notStuck;
	AimableWeapon gun;
	public CyclopesCrab(){
		this.setSprite(sprites.leftCrab);
		this.getAnimationHandler().setAnimationSpeed(.2);
		this.createHitbox(0, 0, 24, 12);
		this.health = 38;
		chillForASecond = false;
		turrning = false;
		finishedAnimationFrames = 0;
		moveRight = false;
		this.defence = 6;
		momentum = 0;
		stuck = false;
		notStuck = true;
		gun = new AimableWeapon (sprites.crabGun);
		gun.declare(0, 0);
	}
	@Override
	public void enemyFrame(){
		boolean lowered;
		lowered = false;
		boolean onFloor;
		onFloor = false;
		boolean moved;
		moved = false;
		int height;
		height = 0;
		boolean toClimbOrNotToClimb;
		toClimbOrNotToClimb = false;
		if (player.getX() - this.getX() < 75 && player.getY() - this.getY() < 370){
			if (player.getX() >= this.getX()){
				if (player.getX() - this.getX() > 4){
					if (!stuck){
					this.setX(getX() + 2);
					}
					if (!moveRight){
					moveRight = true;
					turrning = true;
					if (stuck){
						notStuck = false;
					}
					stuck = false;
				}
			}		
		} else {
				if (player.getX() - this.getX() < 4){
				if (!stuck){
				this.setX(getX() - 2);
				}
				if (moveRight){
					turrning = true;
					moveRight = false;
					if (stuck){
						notStuck = false;
					}
					stuck = false;
				}
			}
			}
		} else {
			if (moveRight){
				setY(getY() - 1);
				setX(getX() + 1);
			} else {
				setX(getX() - 1);
				setY(getY() - 1);
			}
			if (room.isColliding(this.getHitbox())){
				chillForASecond = true;
				turrning = true;
			}
			if (moveRight){
				setX(getX() - 1);
				setY(getY() + 1);
			} else {
				setX(getX() + 1);
				setY(getY() + 1);
			}
			if (chillForASecond){
				moveRight = !moveRight;
				chillForASecond = false;
			}
			if (moveRight){
				this.setX(getX() + 2);
			} else {
				this.setX(getX()-2);
			}
		}
		if (turrning){
			if(!(this.getSprite() == sprites.turrnngCrab)){
				setSprite(sprites.turrnngCrab);
			}
			finishedAnimationFrames = finishedAnimationFrames + 1;
			if(finishedAnimationFrames == 10){
				turrning = false;
				finishedAnimationFrames = 0;
				if (moveRight){
					setSprite (sprites.rightCrab);
				} else {
					setSprite (sprites.leftCrab);
				}
			}
		}
		if (room.isColliding(this.getHitbox())){
			this.setY(getY() - 1);
			lowered = true;
			if (!(room.isColliding(this.getHitbox()))){
				this.setY(getY() + 1);
				lowered = false;
				if (moveRight){
					this.setX(getX() - 1);
					moved = true;
				} else {
					this.setX(getX() + 1);
					moved = true;
				}
				if (room.isColliding(this.getHitbox())){
					onFloor = true;
				}
				}
			}
		if (lowered){
			this.setY(getY() + 1);
		}
		this.setX(getX() + 1);
		
		this.setX(getX() - 1);
		this.setY(getY() + 1);
		if (moved){
			if (moveRight){
				this.setX(getX() + 1);
			} else {
				this.setX(getX() - 1);
			}
		}
		if (!onFloor){
			momentum = momentum + 1;
			if (momentum < 6){
				this.setY(getY() + 1);
			}
			if (momentum > 6 && momentum < 12){
				this.setY(getY() + 2);
			}
			if (momentum > 6 && momentum < 18){
				this.setY(getY() + 3);
			}
			if (momentum > 18 && momentum < 24){
				this.setY(getY() + 4);
			}
			if (momentum > 24){
				this.setY(getY() + 5);
			}	
		}
		if (onFloor && momentum >= 1){
			momentum = 0;
		}
	if (room.isColliding(this.getHitbox())){
		while (!toClimbOrNotToClimb){
			this.setY(getY() - 1);
			height = height + 1;
			if ((height >= 24)){
				this.setY(getY() + 18);
				if (!notStuck){
				notStuck = true;
				} else {
					stuck = true;
				}
			}
			if((!(room.isColliding(this.getHitbox())) || height >= 24)){
				toClimbOrNotToClimb = true;
			}
			
		}
		}
	}
}