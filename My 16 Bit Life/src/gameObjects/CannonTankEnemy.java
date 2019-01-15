package gameObjects;

import projectiles.Cannonball;

public class CannonTankEnemy extends Enemy {
	boolean moveRight;
	int cooldown;
	boolean moveing;
	Cannonball attack;
	boolean hasTurned;
	int turrning;
	boolean nathansVariable;
	int cannonBalls;
public CannonTankEnemy () {
	setSprite (sprites.rightTank);
	createHitbox (0, 3, 15, 28);
	getAnimationHandler ().setAnimationSpeed(.4);
	this.health = 60;
	this.defence = 10;
	this.moveRight = true;
	cooldown = 0;
	moveing = true;
	hasTurned = false;
	turrning = 0;
	nathansVariable = false;
	}
	@Override
	public void enemyFrame(){
		if ((getY () - player.getY() <= 16 && getY () - player.getY() >= -16) && cooldown >= 20 && ((player.getX () > getX() && moveRight) || (player.getX() < getX() && !moveRight)) ){
			moveing = false;
			cooldown = 0;
			cannonBalls = cannonBalls + 1;
			if (cannonBalls == 2) {
				cooldown = -30;
				cannonBalls = 0;
			}
			attack = new Cannonball (moveRight);
			attack.declare (getX(), getY() + 5);
		}
		if (cooldown == 20){
			moveing = true;
		}
		if (moveRight && moveing){
			setX (getX ()+ 1);
		}
		if(!moveRight && moveing){
			setX(getX () - 1);
		}
		if (this.moveRight) {
			setX (getX () + 16);
			setY (getY () + 16);
			if (!room.isColliding (this.getHitbox ())) {
				nathansVariable = true;
					}
			setX (getX () - 16);
			setY (getY () - 16);
		} else {
			setX (getX () - 16);
			setY (getY () + 16);
			if (!room.isColliding (this.getHitbox ())) {
				nathansVariable = true;	
			}
			setX (getX () + 16);
			setY (getY () - 16);
		}
		if ((room.isColliding (this.getHitbox()) || nathansVariable) && !hasTurned){
			setSprite(sprites.turrningCannon);
			turrning = turrning + 1;
			moveing = false;
			cooldown = 0;
			
			if (turrning == 6){
				hasTurned = true;
				moveing = true;
				turrning = 0;
			}
		}
		if (nathansVariable && moveing && hasTurned){
			moveRight = !moveRight;
			if(moveRight){
				setSprite(sprites.rightTank);
				hasTurned = false;
				nathansVariable = false;
			}
			else{
				setSprite(sprites.leftTank);
				hasTurned = false;
				nathansVariable = false;
			}
		}
		if (room.isColliding (this.getHitbox()) && moveing && hasTurned){
			moveRight = !moveRight;
			if (moveRight){
				setSprite(sprites.rightTank);
				hasTurned = false;
			}
			else{
				setSprite(sprites.leftTank);
				hasTurned = false;
				
			}
		}	
		cooldown = cooldown + 1;
	}
}
