package gameObjects;

import projectiles.Button;

public class ClostridiumBowtielinea extends Enemy {
	private Button virus;
	private int cooldown;
	private boolean isStopped;
	private int timer;
public ClostridiumBowtielinea (){
	setSprite (sprites.bowTie);
	this.defence = 0;
	createHitbox(0, 0, 24, 16);
	getAnimationHandler().setAnimationSpeed (.2);
	this.health = 50;
	isStopped = false;
	timer = 0;
	}
	@Override
	public void enemyFrame (){
		boolean firstCollision = false;
		boolean secondCollison = false;
		boolean isChecked = false;
		boolean notGoingUp = false;
		boolean stopped = false;
		double y_basis = getY(); 
		while(getY() >= 0){
			setY(getY() - 1);
			if (room.isColliding (this.getHitbox()) && !isChecked){
				isChecked = true;
					if (firstCollision){
						secondCollison = true;
					} else{
						firstCollision = true;	
					}
			}
			if (isChecked && !(room.isColliding(this.getHitbox()))){
				isChecked = false;
			}
			if (this.getY() == 1 && !(room.isColliding(this.getHitbox())) && firstCollision){
				secondCollison = true;
			}
		}
		setY(y_basis);
		if (!secondCollison && !room.isColliding(this.getHitbox()) && !(getY() == 10)){
			setY(getY() - 1);
		} else {
			notGoingUp = true;
		}
		System.out.println(player.getX() - this.getX());
		if (!room.isColliding(this.getHitbox()) && !isStopped && (!(player.getX() - this.getX() < 3) || !(this.getX() - player.getX() < 3))){
		if (player.getX()> getX()){
			setX(getX() + 3);
			while (room.isColliding(this.getHitbox())){
				setX(getX() - 1);
				stopped = true;
			}
		} else {
			setX(getX() - 3);
			while (room.isColliding(this.getHitbox())){
				setX(getX() + 1);
				stopped = true;
			}
		}
	} 
		if (this.getX()- 24<=player.getX() && player.getX()<=getX() + 24 && cooldown >= 6){
			virus = new Button();
			virus.declare(getX() + 12, getY() + 16);
			cooldown = 0;
		}
		cooldown = cooldown + 1;
		if ((notGoingUp && stopped) || isStopped ){
			isStopped = true;
			if (player.getX()>= getX()){
				setX(getX() - 3);
			} else {
				setX(getX() + 3);
		}
	}		if (!notGoingUp){
		timer = timer + 1;
	}
			if (timer > 15){
				isStopped = false;
				timer = 0;
			}
}
}