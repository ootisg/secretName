package graphics3D;

import graphics3D.Segment;
import main.GameObject;
import main.MainLoop;

public class VectorCamera extends GameObject {
	private double fov;
	private double clipNear;
	private double clipFar;
	public double[][] verticies;
	public double[][] transformed;
	public double[] vertexBuffer = new double[2];
	double px1, py1, pz1, px2, py2, pz2;
	int portWidth, portHeight;
	double hrot, vrot;
	public VectorCamera (double x, double y) {
		this.declare (x, y);
		this.fov = Math.PI / 2;
		this.clipNear = 3;
		this.clipFar = 100;
		this.hrot = 0;
		this.vrot = 0;
	}
	@Override
	public void frameEvent () {
		if (keyCheck ((int)'A')) {
			hrot -= Math.PI / 120;
		}
		if (keyCheck ((int)'D')) {
			hrot += Math.PI / 120;
		}
		this.render ();
	}
	public void render () {
		portWidth = MainLoop.getWindow ().getResolution ()[0];
		portHeight = MainLoop.getWindow ().getResolution ()[1];
		verticies = new double[][] {{64, -8, -8}, {64, 0, 8}, {64, 8, -8}};
		transformed = new double[3][3];
		px1 = Math.cos (fov / 2) * clipNear;
		py1 = Math.sin (fov / 2) * clipNear;
		px2 = Math.cos (-fov / 2) * clipNear;
		py2 = Math.sin (-fov / 2) * clipNear;
		pz1 = Math.sin (fov / 2) * clipNear;
		pz2 = Math.sin (-fov / 2) * clipNear;
		int[][] projCoords = new int[3][2];
		for (int i = 0; i < verticies.length; i ++) {
			vertex (verticies [i], transformed [i]);
		}
		//cast (4, 0, 0, px1, py1, pz1, px2, py2, pz2, vertexBuffer);
		project (transformed, projCoords);
		//System.out.println(projCoords[1]);
		renderTriangle (projCoords);
	}
	public void cast (double px, double py, double pz, double px1, double py1, double pz1, double px2, double py2, double pz2, double[] dest) {
		double isect = getIntersectSV (0, 0, px, py, px1);
		if (Double.isNaN (isect)) {
			//System.out.println(px1);
			dest [0] = Double.NaN;
			dest [1] = Double.NaN;
			return;
		} else {
			double xproj = (-isect - py1) / (py2 - py1);
			isect = getIntersectSV (0, 0, getDist (0, 0, px, py), pz, px1);
			if (Double.isNaN (isect)) {
				dest [0] = Double.NaN;
				dest [1] = Double.NaN;
				return;
			}
			dest [0] = xproj;
			dest [1] = (isect - pz1) / (pz2 - pz1);
		}
	}
	public double getIntersectSV (double x1, double y1, double x2, double y2, double x3) {
		//Returns the point at which a segment (x1, y1) -> (x2, y2) intersects with a vertical line x3
		if (x1 == x2 || !isBetween (x3, x1, x2)) {
			return Double.NaN;
		}
		double m = (y1 - y2) / (x1 - x2);
		//System.out.println(y2);
		double b = y1 - m * x1;
		double isect = m * x3 + b;
		return isect;
	}
	public void project (double[][] verticies, int[][] dest) {
		/*if (verticies.length != dest.length || verticies.length % 3 != 0 || dest.length % 2 != 0) {
			for (int i = 0; i < dest.length; i ++) {
				dest [i][0] = -1;
			}
			return;
		}*/
		for (int i = 0; i < verticies.length; i ++) {
			cast (verticies [i][0], verticies [i][1], verticies [i][2], px1, py1, pz1, px2, py2, pz2, vertexBuffer);
			if (Double.isNaN (vertexBuffer [0])) {
				for (int j = 0; j < 2; j ++) {
					dest [i][j] = -1;
				}
				return;
			} else {
				dest [i][0] = (int)(vertexBuffer [0] * portWidth);
				dest [i][1] = (int)(vertexBuffer [1] * portHeight);
			}
		}
	}
	public void renderTriangle (int[][] verticies) {
		int[] imageData = MainLoop.getWindow ().getImageData ();
		int rasterWidth = MainLoop.getWindow ().getResolution () [0];
		int pixelCounter = 0;
		int yMin, yMax, xMin, xMax;
		double usedX;
		yMin = verticies [0][1];
		yMax = verticies [0][1];
		xMin = -1;
		xMax = -1;
		Segment[] segments = {new Segment (verticies [0][0], verticies [0][1], verticies [1][0], verticies [1][1]), new Segment (verticies [1][0], verticies [1][1], verticies [2][0], verticies [2][1]), new Segment (verticies [2][0], verticies [2][1], verticies [0][0], verticies [0][1])};
		for (int i = 1; i < 3; i ++) {
			if (verticies [i][1] > yMax) {
				yMax = verticies [i][1];
			}
			if (verticies [i][1] < yMin) {
				yMin = verticies [i][1];
			}
		}
		int yBoundLow = yMin;
		int yBoundHigh = yMax;
		if (yMin < 0) {
			yBoundLow = 0;
		}
		if (yMax > portHeight) {
			yBoundHigh = portHeight;
		}
		if (yMin > portHeight || yMax < 0) {
			yMin = 0;
			yMax = 0;
		}
		for (int i = yBoundLow; i <= yBoundHigh; i ++) {
			xMin = -1;
			xMax = -1;
			for (int c = 0; c < 3; c ++) {
				usedX = segments [c].collideScaline (i);
				if (usedX != -1) {
					if (usedX < xMin || xMin == -1) {
						xMin = (int) Math.round (usedX);
					}
					if (usedX > xMax || xMax == -1) {
						xMax = (int) Math.round (usedX);
					}
				}
			}
			if (xMin < 0) {
				xMin = 0;
			}
			if (xMin > portWidth) {
				xMin = portWidth;
			}
			if (xMax < 0) {
				xMax = 0;
			}
			if (xMax > portWidth) {
				xMax = portWidth;
			}
			for (int c = xMin; c <= xMax; c ++) {
				imageData [i * rasterWidth + c] = frag ();
				pixelCounter ++;
			}
		}
	}
	double getDist (double x1, double y1, double x2, double y2) {
		return Math.sqrt ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	public boolean isBetween (double num, double bound1, double bound2) {
		//Returns true if num is between bound1 and bound2
		if (bound1 >= bound2) {
			double temp = bound2;
			bound2 = bound1;
			bound1 = temp;
		}
		return (num >= bound1 && num <= bound2);
	}
	public int frag () {
		return 0xFFFF0000;
	}
	public void vertex (double[] vertex, double[] dest) {
		rotate (hrot, vrot, vertex, dest);
	}
	public void rotate (double pitch, double yaw, double[] vertex, double[] dest) {
		/*
		 * dist = sqrt (x^2 + y^2 + z^2)
		 */
		double dist = Math.sqrt (vertex [0] * vertex [0] + vertex [1] * vertex [1]);
		double tempAng = Math.acos (vertex [0] / dist);
		if (vertex [1] < 0) {
			tempAng = Math.PI * 2 - tempAng;
		}
		dest [0] = Math.cos (tempAng + pitch) * dist;
		dest [1] = Math.sin (tempAng + pitch) * dist;
		dest [2] = vertex [2];
		MainLoop.getWindow ().getBuffer ().fillRect (64 + (int)dest [0], 64 + (int)dest [1], 1, 1);
	}
}
