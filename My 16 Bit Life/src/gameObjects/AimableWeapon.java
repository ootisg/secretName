package gameObjects;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import main.GameObject;
import resources.Sprite;

public class AimableWeapon extends GameObject {
	private BufferedImage img;
	private Sprite src;
	private double rotation;
	private double renderedRotation;
	public AimableWeapon (Sprite sprite) {
		this.src = sprite;
		this.rotation = 0;
		this.renderedRotation = -1;
		img = new BufferedImage (sprite.getImageArray ()[0].getWidth (), sprite.getImageArray ()[0].getHeight (), sprite.getImageArray ()[0].getType ());
	}
	@Override
	public void draw () {
		System.out.println(rotation);
		if (rotation != renderedRotation) {
			BufferedImage startImg = src.getImageArray ()[0];
			AffineTransform transform = new AffineTransform ();
			transform.rotate (rotation, 5, 12);
			AffineTransformOp operation = new AffineTransformOp (transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			operation.filter (startImg, img);
			this.setSprite (new Sprite (img));
			renderedRotation = rotation;
		}
		getSprite ().draw ((int) getX () - room.getViewX () - 5, (int) getY () - room.getViewY () - 12, this.getFlipHorizontal (), false);
	}
	public void clearImage () {
		//BufferedImage usedImage = src.getImageArray ()[0];
	}
	public void setRotation (double rotation) {
		this.rotation = rotation;
	}
	public double getRotation () {
		return this.rotation;
	}
	public void shoot (Projectile projectile) {
		double ang = rotation;
		double endX;
		double endY;
		if (this.getFlipHorizontal ()) {
			ang = Math.PI - ang;
		}
		if (this.getFlipHorizontal ()) {
			endX = this.getX () + Math.cos (ang + Math.PI / 180 * 15) * 14 + 4;
			endY = this.getY () + Math.sin (ang + Math.PI / 180 * 15) * 14;
		} else {
			endX = this.getX () + Math.cos (ang - Math.PI / 180 * 15) * 14;
			endY = this.getY () + Math.sin (ang - Math.PI / 180 * 15) * 14;
		}
		projectile.setAttributes (endX, endY, ang);
	}
}