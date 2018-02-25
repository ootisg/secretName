package gameObjects;

import main.GameObject;

public class TestObject extends GameObject {
	public TestObject () {
		this.setSprite(sprites.butterflySprite);
		this.createHitbox (0, 0, 8, 8);
	}
	@Override
	public void frameEvent () {
		if (this.isColliding ("players.Jeffrey")) {
			System.out.println("HELLO");
		}
	}
}