//A container class for sprites

package resources;

public class SpriteContainer {
	//Please alphebitize spritesheets and sprites
	//Spritesheets
	public Spritesheet butterflySheet = new Spritesheet ("resources/sprites/creepy_butterfly.png");
	public Spritesheet duoflySheet = new Spritesheet ("resources/sprites/duofly.png");
	public Spritesheet jeffreySheet = new Spritesheet ("resources/sprites/jeffrey_walking.png");
	public Spritesheet slimeletSheet = new Spritesheet ("resources/sprites/slimelet.png");
	public Spritesheet blobSheet = new Spritesheet ("resources/sprites/blob_with_shoes.png");
	//Sprites
	public Sprite butterflySprite = new Sprite (butterflySheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 16, 16);
	public Sprite duoflyMinus = new Sprite (duoflySheet, new int[]{0, 16}, new int[]{16, 16}, 16, 16);
	public Sprite duoflyMinusDeath = new Sprite (duoflySheet, new int[]{32, 48, 64, 80, 96}, new int[]{16, 16, 16, 16, 16}, 16, 16);
	public Sprite duoflyPlus = new Sprite (duoflySheet, new int[]{0, 16}, new int[]{0, 0}, 16, 16);
	public Sprite duoflyPlusDeath = new Sprite (duoflySheet, new int[]{32, 48, 64, 80, 96}, new int[]{0, 0, 0, 0, 0}, 16, 16);
	public Sprite jeffreyIdle = new Sprite (jeffreySheet, 0, 0, 16, 32);
	public Sprite jeffreyWalking = new Sprite (jeffreySheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176}, new int[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32}, 16, 32);
	public Sprite paintball = new Sprite ("resources/sprites/redblack_ball.png");
	public Sprite slime = new Sprite ("resources/sprites/slime.png");
	public Sprite slimeletHorizontal = new Sprite (slimeletSheet, new int[]{0, 16}, new int[]{0, 0}, 16, 16);
	public Sprite slimeletVertical = new Sprite (slimeletSheet, new int[]{0, 16}, new int[]{16, 16}, 16, 16);
	public Sprite blob = new Sprite (blobSheet, new int[]{0, 0}, new int[]{0, 16}, 16, 16);
	
	public SpriteContainer () {

	}
}
