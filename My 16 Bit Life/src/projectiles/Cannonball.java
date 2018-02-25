package projectiles;

import main.GameObject;
import main.MainLoop;
import players.Jeffrey;


public class Cannonball extends Projectile{
	public static Jeffrey player = (Jeffrey) MainLoop.getObjectMatrix ().get (MainLoop.getObjectMatrix ().getTypeId ("players.Jeffrey"), 0);
	boolean hitSomething = false;
	int animation = 0;
	public Cannonball (boolean direction){
		setSprite (sprites.cannonBall);
		createHitbox (0,0,8,8);
		setAttributes (128, 128, 3.14, 1);
		if (direction){
			setDirection (0);
		}
		else{
			setDirection (3.14);
		}
	}
	@Override
	public void projectileFrame (){
		if (hitSomething){
			animation = animation + 1;
		}
		if (animation == 20){
			this.forget();
		}
		if (room.isColliding(this.getHitbox()) && !hitSomething){
			setSprite (sprites.explosion);
			hitSomething = true;
			setSpeed (0);
			setY (getY());
		}
		if (isColliding("players.Jeffrey") && !hitSomething){
			player.damage(7);
			setSprite (sprites.explosion);
			hitSomething = true;
			setSpeed (0);
			setY (getY());
			
			}
	}
}

