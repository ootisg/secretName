package graphics3D;

import graphics3D.Segment;
import main.GameObject;
import main.MainLoop;

import java.util.*;

public class VectorCamera extends GameObject {
	private double fov;
	private double clipNear;
	private double clipFar;
	public double[][] verticies;
	public double[][] transformed;
	public double[] vertexBuffer = new double[2];
	double px1, py1, pz1, px2, py2, pz2;
	double camX, camY;
	int portWidth, portHeight;
	double hrot, vrot;
	public VectorCamera (double x, double y) {
		this.declare (x, y);
		this.fov = Math.PI / 2;
		this.clipNear = .5;
		this.clipFar = 100;
		this.hrot = 0;
		this.vrot = 0;
		this.camX = 0;
		this.camY = 0;
	}
	@Override
	public void frameEvent () {
		if (keyCheck ((int)'A')) {
			hrot += Math.PI / 120;
		}
		if (keyCheck ((int)'D')) {
			hrot -= Math.PI / 120;
		}
		if (keyCheck ((int)'W')) {
			camX += .36 * Math.cos (hrot);
			camY -= .36 * Math.sin (hrot);
		}
		if (keyCheck ((int)'S')) {
			camX -= .36 * Math.cos (hrot);
			camY += .36 * Math.sin (hrot);
		}
		this.render ();
	}
	public void render () {
		portWidth = MainLoop.getWindow ().getResolution ()[0];
		portHeight = MainLoop.getWindow ().getResolution ()[1];
		verticies = new double[][] {{16, -4, -4}, {16, -4, 4}, {16, 4, -4},
									{16, -4, 4}, {16, 4, 4}, {16, 4, -4}};
		/*verticies = new double[][] {{-128, -128, 1}, {128, -128, -1}, {-128, 128, -1},
									{128, 128, -1}, {128, -128, -1}, {-128, 128, -1}};*/
		transformed = new double[6][3];
		px1 = Math.cos (fov / 2) * clipNear;
		py1 = Math.sin (fov / 2) * clipNear;
		px2 = Math.cos (-fov / 2) * clipNear;
		py2 = Math.sin (-fov / 2) * clipNear;
		pz1 = Math.sin (fov / 2) * clipNear;
		pz2 = Math.sin (-fov / 2) * clipNear;
		int[][] projCoords = new int[6][2];
		for (int i = 0; i < verticies.length; i ++) {
			vertex (verticies [i], transformed [i]);
		}
		double[] depthBuffer = new double[2];
		int[] triangleBuffer = new int[2];
		for (int i = 0; i < depthBuffer.length; i ++) {
			triangleBuffer [i] = i;
			depthBuffer [i] = 0;
			for (int j = 0; j < 3; j ++) {
				depthBuffer [i] += Math.sqrt (transformed [i * 3 + j][0] * transformed [i * 3 + j][0] + transformed [i * 3 + j][1] * transformed [i * 3 + j][1] + transformed [i * 3 + j][2] + transformed [i * 3 + j][2]);
			}
		}
		for (int i = 0; i < depthBuffer.length; i ++) {
			int maxIndex = 0;
			//System.out.println(Double.valueOf(depthBuffer [0]) + ", " + Double.valueOf(depthBuffer [1]));
			for (int j = i + 1; j < depthBuffer.length; j ++) {
				//System.out.println(Double.valueOf(depthBuffer[maxIndex]) + ", " + Double.valueOf(depthBuffer[j]));
				if (depthBuffer [j] < depthBuffer [maxIndex]) {
					maxIndex = j;
				}
			}
			int temp = triangleBuffer [maxIndex];
			double tempDouble = depthBuffer [maxIndex];
			depthBuffer [maxIndex] = depthBuffer [i];
			depthBuffer [i] = tempDouble;
			triangleBuffer [maxIndex] = triangleBuffer [i];
			triangleBuffer [i] = temp;
		}
		//System.out.println(Double.valueOf (depthBuffer [0]) + ", " + Double.valueOf (depthBuffer [1]) + "; " + Integer.valueOf(triangleBuffer[0]) + ", " + Integer.valueOf(triangleBuffer[1]));
		//cast (4, 0, 0, px1, py1, pz1, px2, py2, pz2, vertexBuffer);
		project (transformed, projCoords);
		//System.out.println(projCoords[1]);
		renderTriangles (projCoords);
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
			isect = getIntersectSV (0, 0, getDist (0, 0, px, py), pz, getDist (0, 0, px1, isect));
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
					for (int k = 0; k < 3; k ++) {
						dest [k][j] = 0;
					}
				}
				return;
			} else {
				dest [i][0] = (int)(vertexBuffer [0] * portWidth);
				dest [i][1] = (int)(vertexBuffer [1] * portHeight);
			}
		}
	}
	public void project (double[][] verticies, int[][] dest, int[] map) {
		/*if (verticies.length != dest.length || verticies.length % 3 != 0 || dest.length % 2 != 0) {
			for (int i = 0; i < dest.length; i ++) {
				dest [i][0] = -1;
			}
			return;
		}*/
		for (int i = 0; i < verticies.length; i ++) {
			int mapIndex = map [i];
			cast (verticies [mapIndex][0], verticies [mapIndex][1], verticies [mapIndex][2], px1, py1, pz1, px2, py2, pz2, vertexBuffer);
			if (Double.isNaN (vertexBuffer [0])) {
				for (int j = 0; j < 2; j ++) {
					for (int k = 0; k < 3; k ++) {
						dest [k][j] = 0;
					}
				}
				return;
			} else {
				dest [i][0] = (int)(vertexBuffer [0] * portWidth);
				dest [i][1] = (int)(vertexBuffer [1] * portHeight);
			}
		}
	}
	public void renderTriangles (int[][] coords) {
		int[][] tempArray = new int[3][2];
		for (int i = 0; i < coords.length; i += 3) {
			tempArray [0][0] = coords [i][0];
			tempArray [0][1] = coords [i][1];
			tempArray [1][0] = coords [i + 1][0];
			tempArray [1][1] = coords [i + 1][1];
			tempArray [2][0] = coords [i + 2][0];
			tempArray [2][1] = coords [i + 2][1];
			renderTriangle (tempArray);
		}
	}
	public void renderTriangle (int[][] verticies) {
		int[] imageData = MainLoop.getWindow ().getImageData ();
		int rasterWidth = MainLoop.getWindow ().getResolution () [0];
		int pixelCounter = 0;
		int yMin, yMax, xMin, xMax;
		boolean xMinDefined, xMaxDefined;
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
		if (yMax >= portHeight) {
			yBoundHigh = portHeight - 1;
		}
		if (yMin >= portHeight || yMax < 0) {
			yMin = 0;
			yMax = 0;
		}
		for (int i = yBoundLow; i <= yBoundHigh; i ++) {
			xMinDefined = false;
			xMaxDefined = false;
			for (int c = 0; c < 3; c ++) {
				usedX = segments [c].collideScaline (i);
				if (usedX != -1) {
					if (usedX < xMin || !xMinDefined) {
						xMin = (int) Math.round (usedX);
						xMinDefined = true;
					}
					if (usedX > xMax || !xMaxDefined) {
						xMax = (int) Math.round (usedX);
						xMaxDefined = true;
					}
				}
			}
			if (xMin < 0) {
				xMin = 0;
			}
			if (xMax < 0 && xMax < 0 || xMin >= portWidth && xMax >= portWidth) {
				xMin = -1;
			}
			if (xMax >= portWidth) {
				xMax = portWidth - 1;
			}
			//System.out.println(Double.valueOf(xMin) + ", " + Double.valueOf(xMax));
			if (xMin != -1) {
				for (int c = xMin; c <= xMax; c ++) {
					imageData [i * rasterWidth + c] = frag ();
					pixelCounter ++;
				}
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
		double[] dest2 = new double[3];
		translate (-camX, -camY, 0, vertex, dest2);
		rotate (hrot, vrot, dest2, dest);
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
	public void translate (double deltaX, double deltaY, double deltaZ, double[] vertex, double[] dest) {
		dest [0] = vertex [0] + deltaX;
		dest [1] = vertex [1] + deltaY;
		dest [2] = vertex [2] + deltaZ;
	}
	@Override
	public void forget () {
		
	}
}
