package gameObjects;

import main.GameObject;
import map.Room;
import resources.Sprite;
import resources.Spritesheet;

public class DamageText extends GameObject {
	int momentum = 0;
	public Spritesheet damageText = new Spritesheet ("resources/sprites/damage_text.png");
	public Sprite DamageText = new Sprite (damageText, 8, 8);
	private int x;
	private int y;
	public DamageText (double amount, double d, double e){
		DamageText secondidget;
		int timesAmount = 1; 
		this.setY(this.getY() - 12);
		if (this.getX() < 508){
		this.setX(this.getX() + 4);
		}
		if(amount < 10){
		DamageText.setFrame((int)amount);
		} else {
		double copyOfamount = amount; 
		while (amount > 9) {
			amount = amount/10;
			timesAmount = timesAmount * 10;
		}
		int frameToSet = (int) (Math.floor(amount));
		System.out.println(amount);
		System.out.println(frameToSet);
		DamageText.setFrame(frameToSet);
		if (frameToSet * timesAmount > 10 && copyOfamount -  frameToSet * timesAmount < 10) {
			int amountOfZeros = 0;
			int copyOfTimesAmount = timesAmount;
			while (timesAmount != 1) {
				timesAmount = timesAmount/10;
				amountOfZeros = amountOfZeros + 1;
			}
			System.out.println(amountOfZeros);
			secondidget = new DamageText (amountOfZeros, copyOfamount -(frameToSet * copyOfTimesAmount), d + 8, e);
			secondidget.declare(d, e);
		} else {
		copyOfamount = copyOfamount - frameToSet * timesAmount;
		secondidget = new DamageText (copyOfamount, d + 8, e);
		secondidget.declare(d, e);
		}
		}
		x = (int) d;
		y = (int) e;
	}
	public DamageText(double numOfZeros, double endNum, double d, double e) {
		DamageText secondidget;
		if (numOfZeros != 1) {
			DamageText.setFrame(0);
			secondidget = new DamageText (numOfZeros - 1, endNum, d + 8, e);
			secondidget.declare(d, e);
		} else {
			DamageText.setFrame((int)endNum);
		}
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
