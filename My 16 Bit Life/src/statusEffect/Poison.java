package statusEffect;

import gameObjects.Enemy;
import main.GameObject;
import players.Jeffrey;
import statusEffect.Status;
public class Poison extends GameObject{
	Enemy affectedPerson;
	Jeffrey affectedMAN;
	Status stateOfAffairs;
	int timer;
	int effectTimer;
	int level;
	public Poison(Enemy affected, Status state, int tier) {
		stateOfAffairs = state;
		affectedPerson = affected;
		timer = 0;
		effectTimer = 0;
		level = tier;
	}
	public Poison (Jeffrey affected, Status state, int tier){
		stateOfAffairs = state;
		affectedMAN = affected;
		timer = 0;
		effectTimer = 0;
		level = tier;
		affectedMAN.walkSprite = sprites.poisonedJeffreyWalking;
		affectedMAN.standSprite = sprites.poisonedJeffreyIdle;
	}
	@Override
	public void frameEvent (){
		
		if(stateOfAffairs.isAffected && !(affectedPerson == null) && ((timer == 150 && level == 1) || (timer == 120 && level == 2) || (timer == 90 && level == 3) || (timer == 60 && level == 4))){
			affectedPerson.health = affectedPerson.health - affectedPerson.health/50;
			timer = 0;
     		}
		if (stateOfAffairs.isAffected && !(affectedMAN == null) && ((timer == 150 && level == 1) || (timer == 120 && level == 2) || (timer == 90 && level == 3) || (timer == 60 && level == 4))){
			affectedMAN.health = affectedMAN.health - affectedMAN.health/50;
			timer = 0;
		}
		timer = timer + 1;
		effectTimer = effectTimer + 1;
		if (effectTimer == 600){
			affectedMAN.walkSprite = sprites.jeffreyWalking;
			affectedMAN.standSprite = sprites.jeffreyIdle;
			this.forget();
			}
	}
}
