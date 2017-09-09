package players;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import main.GameObject;
import resources.Sprite;

public class TubeRaster extends GameObject {
	BufferedImage img;
	int color;
	int x1;
	int x2;
	int x3;
	int x4;
	int y1;
	int y2;
	int y3;
	int y4;
	WritableRaster raster;
	Sprite spr;
	public TubeRaster (int color, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		this.color = color;
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
		this.y1 = y1;
		this.y2 = y2;
		this.y3 = y3;
		this.y4 = y4;
		if (x2 > x4) {
			int temp = x4;
			x4 = x2;
			x2 = temp;
		}
		if (y2 > y4) {
			int temp = y4;
			y4 = y2;
			y2 = temp;
		}
		img = new BufferedImage (x4 + 1, y4 + 1, BufferedImage.TYPE_INT_ARGB);
		raster = img.getRaster ();
		spr = new Sprite (img);
	}
	public void render () {
		int width = img.getWidth ();
		int height = img.getHeight ();
		int[] data = new int[width * height];
		double m1 = (y1 - y2) / (x1 - x2);
		double b1 = y1 - m1 * x1;
		double m2 = (y3 - y4) / (x3 - x4);
		double b2 = y3 - m2 * x3;
		int ytop;
		int ybot;
		for (int i = x3; i < x4; i ++) {
			ytop = (int) (m1 * i + b1);
			ybot = (int) (m2 * i + b2);
			for (int j = ytop; j <= ybot; j ++) {
				if (j >= 0 && j < height) {
					data [j * width + i] = color;
				}
			}
		}
		raster.setDataElements (0, 0, width, height, data);
		spr.draw(0, 0);
	}
	@Override
	public void frameEvent () {
		render ();
	}
	public void setCoords (int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
		this.y1 = y1;
		this.y2 = y2;
		this.y3 = y3;
		this.y4 = y4;
		if (x2 > x4) {
			int temp = x4;
			x4 = x2;
			x2 = temp;
		}
		if (y2 > y4) {
			int temp = y4;
			y4 = y2;
			y2 = temp;
		}
		img = new BufferedImage (x4 + 1, y4 + 1, BufferedImage.TYPE_INT_ARGB);
	}
	public void setColor (int color) {
		this.color = color;
	}
}