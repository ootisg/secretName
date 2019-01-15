//A container class for sprites

package resources;

public class SpriteContainer {
	//Please alphebitize spritesheets and sprites
	//Spritesheets
	public Spritesheet heartSheet = new Spritesheet ("resources/sprites/heartsheet.png");
	public Spritesheet butterflySheet = new Spritesheet ("resources/sprites/creepy_butterfly.png");
	public Spritesheet duoflySheet = new Spritesheet ("resources/sprites/duofly.png");
	public Spritesheet jeffreySheet = new Spritesheet ("resources/sprites/jeffrey_walking.png");
	public Spritesheet slimeletSheet = new Spritesheet ("resources/sprites/slimelet.png");
	public Spritesheet blobSheet = new Spritesheet ("resources/sprites/blob_with_shoes.png");
	public Spritesheet tankSheet = new Spritesheet ("resources/sprites/Tank_enemy.png");
	public Spritesheet textBorderSheet = new Spritesheet ("resources/sprites/windowsprites2.png");
	public Spritesheet fontSheet = new Spritesheet ("resources/sprites/text.png");
	public Spritesheet slimeletClimbSheet = new Spritesheet ("resources/sprites/slimelet_climb.png");
	public Spritesheet slimeletAroundSheet = new Spritesheet ("resources/sprites/slimelet_around.png");
	public Spritesheet walkSheetTD = new Spritesheet ("resources/sprites/walk_cycle_TD.png");
	public Spritesheet explosionSheet = new Spritesheet ("resources/sprites/explosion.png");
	public Spritesheet bowtieSheet = new Spritesheet ("resources/sprites/Clostridium_bowtielinea_24x16.png");
	public Spritesheet crabSheet = new Spritesheet ("resources/sprites/Cyclops_Crab_1.png");
	public Spritesheet buttonSheet = new Spritesheet ("resources/sprites/Button_L.png");
	public Spritesheet poisonedJeffrey = new Spritesheet ("resources/sprites/jeffrey_walking_poisoned.png");
	public Spritesheet crabGunSheet = new Spritesheet ("resources/sprites/Cyclops_Crab_Gun.png");
	public Spritesheet moneyBagSheet = new Spritesheet("resources/sprites/Money_Bag.png");
	//Sprites
	public Sprite crabGun = new Sprite (crabGunSheet, 0, 0, 6, 16);
	public Sprite idleBag = new Sprite (moneyBagSheet, 16, 15);
	public Sprite ladder = new Sprite ("resources/sprites/Ladder.png");
	public Sprite crabGunFireing = new Sprite (crabGunSheet, new int []{0, 7, 13, 19}, new int [] {0,0,0,0}, 6, 16);
	public Sprite crabBullet = new Sprite (crabGunSheet, 26, 13, 3, 3);
	public Sprite button = new Sprite (buttonSheet, 8, 8);
	public Sprite leftCrab = new Sprite (crabSheet, new int[]{0, 24, 48, 72, 96, 120, 144}, new int[]{0, 0, 0, 0, 0, 0, 0},24, 12);
	public Sprite turrnngCrab = new Sprite (crabSheet, new int []{168, 192, 216, 240}, new int []{0,0,0,0,},24,12);
	public Sprite rightCrab = new Sprite (crabSheet, new int []{264, 288, 312, 336, 360, 384}, new int []{0,0,0,0,0,0},24,12);
	public Sprite bowTie = new Sprite (bowtieSheet, new int[]{0, 0, 0}, new int []{0, 16, 32}, 24, 16);
	public Sprite hearts = new Sprite (heartSheet, 16, 16);
	public Sprite butterflySprite = new Sprite (butterflySheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 16, 16);
	public Sprite duoflyMinus = new Sprite (duoflySheet, new int[]{0, 16}, new int[]{16, 16}, 16, 16);
	public Sprite duoflyMinusDeath = new Sprite (duoflySheet, new int[]{32, 48, 64, 80, 96}, new int[]{16, 16, 16, 16, 16}, 16, 16);
	public Sprite duoflyPlus = new Sprite (duoflySheet, new int[]{0, 16}, new int[]{0, 0}, 16, 16);
	public Sprite duoflyPlusDeath = new Sprite (duoflySheet, new int[]{32, 48, 64, 80, 96}, new int[]{0, 0, 0, 0, 0}, 16, 16);
	public Sprite jeffreyIdle = new Sprite (jeffreySheet, 0, 0, 16, 32);
	public Sprite poisonedJeffreyIdle = new Sprite (poisonedJeffrey, 0, 0, 16, 32);
	public Sprite jeffreyWalking = new Sprite (jeffreySheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176}, new int[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32}, 16, 32);
	public Sprite poisonedJeffreyWalking = new Sprite (poisonedJeffrey, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 144, 160, 176}, new int[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32}, 16, 32);
	public Sprite paintball = new Sprite ("resources/sprites/redblack_ball.png");
	public Sprite slime = new Sprite ("resources/sprites/slime.png");
	public Sprite slimeletHorizontal = new Sprite (slimeletSheet, new int[]{0, 16}, new int[]{0, 0}, 16, 16);
	public Sprite slimeletVertical = new Sprite (slimeletSheet, new int[]{0, 16}, new int[]{16, 16}, 16, 16);
	public Sprite blob = new Sprite (blobSheet, new int[]{0, 0}, new int[]{0, 16}, 16, 16);
	public Sprite textBorder = new Sprite (textBorderSheet, 8, 8);
	public Sprite font = new Sprite (fontSheet, 8, 8);
	public Sprite selector = new Sprite ("resources/sprites/selector.png");
	public Sprite slimeletClimbHorizontal = new Sprite (slimeletClimbSheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 0, 16, 32, 48, 64, 80, 96, 112, 128}, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 16, 16, 16, 16, 16, 16, 16, 16, 16}, 16, 16);
	public Sprite slimeletClimbVertical = new Sprite (slimeletClimbSheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128, 0, 16, 32, 48, 64, 80, 96, 112, 128}, new int[] {32, 32, 32, 32, 32, 32, 32, 32, 32, 48, 48, 48, 48, 48, 48, 48, 48, 48}, 16, 16);
	public Sprite slimeletAround = new Sprite (slimeletAroundSheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128}, new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0}, 16, 16);
	public Sprite slimeletOver = new Sprite (slimeletAroundSheet, new int[]{0, 16, 32, 48, 64, 80, 96, 112, 128}, new int[] {16, 16, 16, 16, 16, 16, 16, 16, 16}, 16, 16);
	public Sprite rightTank = new Sprite (tankSheet, new int [] {0, 16}, new int []{0,0}, 16, 32);
	public Sprite leftTank = new Sprite (tankSheet, new int [] {112, 128}, new int []{0,0}, 16, 32);
	public Sprite cannonBall = new Sprite (tankSheet, 144, 24, 8, 8);
	public Sprite turrningCannon = new Sprite (tankSheet, new int [] {32, 48, 64, 80, 96}, new int []{0, 0, 0, 0, 0}, 16, 32);
	public Sprite explosion = new Sprite (explosionSheet, 8, 8);
	public Sprite[] tdWalk = new Sprite[] {
			new Sprite (walkSheetTD, new int[]{16, 32}, new int[]{0, 0}, 16, 16),
			new Sprite (walkSheetTD, new int[]{16, 32}, new int[]{16, 16}, 16, 16),
			new Sprite (walkSheetTD, new int[]{16, 32}, new int[]{32, 32}, 16, 16),
			new Sprite (walkSheetTD, new int[]{16, 32}, new int[]{48, 48}, 16, 16)
	};
	public Sprite tdIdle = new Sprite (walkSheetTD, new int[]{0, 0, 0, 0}, new int[]{0, 16, 32, 48}, 16, 16);
	public SpriteContainer () {

	}
}
