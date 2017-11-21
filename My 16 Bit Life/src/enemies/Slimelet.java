package enemies;

public class Slimelet extends Enemy {
	private boolean isClimbingConcave;
	private int animationTimer;
	private int animationFrames;
	private int climbTimer;
	private int climbFrames;
	private boolean altFrame;
	public Slimelet () {
		this.setSprite (sprites.slimeletClimb);
		this.getAnimationHandler ().setAnimationSpeed (.3);
		this.createHitbox (0, 0, 16, 16);
		this.animationFrames = 6;
		this.climbFrames = 4;
		this.isClimbingConcave = true;
	}
	@Override
	public void enemyFrame () {
		this.setX (this.getX () + .7);
		new Slime ().declare (this.getX () + 2, this.getY () + 15);
	}
	@Override
	public void draw () {
		if (animationTimer % animationFrames == animationFrames - 1) {
			altFrame = !altFrame;
			animationTimer = 0;
		}
		if (isClimbingConcave) {
			if (climbTimer >= climbFrames * 9) {
				climbTimer = 0;
				isClimbingConcave = true; //CHANGE TO FALSE
			} else {
				int frameToDraw = 0;
				if (altFrame) {
					frameToDraw = 9;
				}
				frameToDraw += (climbTimer / climbFrames);
				System.out.println(climbTimer/climbFrames);
				sprites.slimeletClimb.setFrame (frameToDraw);
				sprites.slimeletClimb.draw ((int)this.getX () - room.getViewX (), (int)this.getY () - room.getViewY ());
				climbTimer ++;
			}
		} else {
			if (altFrame) {
				sprites.slimeletHorizontal.setFrame (1);
			} else {
				sprites.slimeletHorizontal.setFrame (0);
			}
			sprites.slimeletHorizontal.draw ((int)getX (), (int)getY ());
		}
		animationTimer ++;
	}
}
