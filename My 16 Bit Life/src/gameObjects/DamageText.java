package gameObjects;

import main.GameObject;
import map.Room;
import resources.Sprite;
import resources.Spritesheet;

public class DamageText extends GameObject {
	DamageText secondidget;
	int testAmount;
	int momentum = 0;
	public Spritesheet damageText = new Spritesheet ("resources/sprites/damage_text.png");
	public Sprite DamageText = new Sprite (damageText, 8, 8);
	private int x;
	private int y;
	public DamageText (int amount, double d, double e){
		this.setY(this.getY() - 12);
		if (this.getX() < 508){
		this.setX(this.getX() + 4);
		}
		if(amount < 10){
		DamageText.setFrame(amount);
		} else {
		if (amount < 100){
		secondidget = new DamageText (amount % 10, d + 8, e);
		secondidget.declare(d, e);
		DamageText.setFrame((amount - (amount % 10))/10);
		}
		}
		testAmount = amount;
		x = (int) d;
		y = (int) e;
	}
	@Override
	public void frameEvent(){
		DamageText.draw(x- room.getViewX(), y);
		momentum = momentum + 1;
		if (momentum < 6){
			y = y + 2;
		}
		if (momentum > 6 && momentum < 12){
			y = y + 4;
		}
		if (momentum > 6 && momentum < 18){
			y = y + 6;
		}
		if (momentum > 18 && momentum < 24){
			y = y + 8;
		}
		if (momentum > 24){
			y = y + 10;
		}	
		if (y > 512){
			this.forget();
		}
	}
}
