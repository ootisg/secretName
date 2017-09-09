//A container class for sprites

package resources;

public class SpriteContainer {
	//Spritesheets
	Spritesheet duoflySheet;
	Spritesheet jeffreySheet;
	Spritesheet butterflySheet;
	//Sprites
	public Sprite duoflyPlus;
	public Sprite duoflyMinus;
	public Sprite duoflyPlusDeath;
	public Sprite duoflyMinusDeath;
	public Sprite jeffreyIdle;
	public Sprite jeffreyWalking;
	public Sprite butterflySprite;
	public Sprite paintball;
	public SpriteContainer () {
		//Initialize spritesheets
		duoflySheet = new Spritesheet ("resources/sprites/duofly.png");
		jeffreySheet = new Spritesheet ("resources/sprites/jeffrey_walking.png");
		butterflySheet = new Spritesheet ("resources/sprites/creepy_butterfly.png");
		//Initialize sprites
		duoflyPlus = new Sprite (duoflySheet, new int[]{0, 16}, new int[]{0, 0}, 16, 16);
		duoflyMinus = new Sprite (duoflySheet, new int[]{0, 16}, new int[]{16, 16}, 16, 16);
		duoflyPlusDeath = new Sprite (duoflySheet, new int[]{32, 48, 64, 80, 96}, new int[]{0, 0, 0, 0, 0}, 16, 16);
		duoflyMinusDeath = new Sprite (duoflySheet, new int[]{32, 48, 64, 80, 96}, new int[]{16, 16, 16, 16, 16}, 16, 16);
		butterflySprite = new Sprite (butterflySheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 16, 16);
		jeffreyWalking = new Sprite (jeffreySheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176}, new int[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32}, 16, 32);
		jeffreyIdle = new Sprite (jeffreySheet, 0, 0, 16, 32);
		paintball = new Sprite ("resources/sprites/redblack_ball.png");
	}
}
