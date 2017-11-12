package enemies;

import main.GameObject;
import main.MainLoop;
import players.Jeffrey;
import resources.Spritesheet;

public abstract class Enemy extends GameObject {
	//Template for enemies
	public static Jeffrey player = (Jeffrey) MainLoop.getObjectMatrix ().get (MainLoop.getObjectMatrix ().getTypeId ("players.Jeffrey"), 0);
	public static String[] enemyList = new String [0];
	protected int health = 1;
	protected double baseDamage = 2.5;
	public Enemy () {
		
	}
	@Override
	public void declare (double x, double y) {
		//Adds this GameObject to the object matrix
		this.matrixLocation = MainLoop.getObjectMatrix ().add (this);
		setX (x);
		setY (y);
		String className = this.getClass ().getName ();
		for (int i = 0; i < enemyList.length; i ++) {
			if (enemyList [i].equals (className)) {
				return;
			}
		}
		String[] tempList = new String [enemyList.length + 1];
		System.arraycopy (enemyList, 0, tempList, 0, enemyList.length);
		tempList [tempList.length - 1] = className;
		enemyList = tempList;
	}
	@Override
	public void frameEvent () {
		if (health <= 0) {
			this.deathEvent ();
		}
		enemyFrame ();
		if (isColliding (player)) {
			attackEvent ();
		}
	}
	public void enemyFrame () {
		
	}
	public void attackEvent () {
		player.damage (this.baseDamage);
	}
	public void deathEvent () {
		this.forget ();
	}
	public void damage (int amount) {
		this.health -= amount;
	}
	public void setHealth (int health) {
		this.health = health;
	}
	public int getHealth () {
		return this.health;
	}
}