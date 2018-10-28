package projectiles;

import java.util.Random;

import main.MainLoop;
import players.Jeffrey;
import statusEffect.Status;

public class Button extends Projectile {
	Status poison;
	Random rand;
	public static Jeffrey player = (Jeffrey) MainLoop.getObjectMatrix ().get (MainLoop.getObjectMatrix ().getTypeId ("players.Jeffrey"), 0);
	public Button () {
		setSprite (sprites.button);
		createHitbox (0,0,8,8);
		setAttributes (128, 128, 1.57, 3);
		poison = new Status();
		rand = new Random();
	}
	@Override
	public void projectileFrame(){
		if (room.isColliding (this.getHitbox())){
			this.forget();
		}
		if (isColliding("players.Jeffrey")){
			int poisonChance = rand.nextInt(4) + 1;
			if ((!poison.IsAffected()) && poisonChance == 1){
				poison.setAttributesPlayerWithTier(player, "Poison", 3);
				poison.ApplyStatus();
			} 
			player.damage(7);
		}
	}
}
