package statusEffect;

import gameObjects.Enemy;
import main.GameObject;
import players.Jeffrey;

public class Status extends GameObject {
	Boolean isAffected;
	Boolean isCreated;
	public Status(){
		// make sure that in the code in your "frameEvent" for your status only runs if isAffected is true (you can check that using IsAffected)
		isAffected = false;
		isCreated = false;
	}
	//gives the status in question to that gameObject
	public void ApplyStatus(){
		isAffected = true;
	}
	// returns true if the gameObject is Affected with the status
	public boolean IsAffected(){
		if (isAffected){
			return true;
		} else {
			return false;
		}
	}
	public void CureStatus(){
	isAffected = false;
	}
	// the GameObject is the thing that has the status applied to it the String is the name of the Status that is applied
	// Make sure to set your Attributes as soon as you constuct your Status
	// the tier is for the tiers for the status I havent made a set attributes without tier yet because we don't have a status that has no teirs yet but I will add one when the time comes
	public void setAttributesWithTier(Enemy affected, String status, int tier){
		switch (status){
		// when you create a new status you need to add a case for it in this switch statement with the case as the same name as the status
		case "Poison":
			if (!isCreated){
		Poison applyAbleStatus;
		applyAbleStatus = new Poison(affected, this, tier);
		applyAbleStatus.declare(0,0);
		isCreated = true;
		}
		break;
		} 
	}
	// use this when you want the status to be applied to the player
	public void setAttributesPlayerWithTier(Jeffrey sickMAN, String sickness, int level){
		switch (sickness){
		// when you create a new status you need to add a case for it in this switch statement with the case as the same name as the status
		case "Poison":
			if(!isCreated){
		Poison applyAbleStatus;
		applyAbleStatus = new Poison(sickMAN, this, level);
		applyAbleStatus.declare(0,0);
		isCreated = true;
			}
		break;
		} 
	}
}
