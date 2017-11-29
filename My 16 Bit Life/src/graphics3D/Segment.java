package graphics3D;

public class Segment {
	int x1, y1, x2, y2;
	double slope;
	double intercept;
	boolean isVertical;
	public Segment (int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		if (x1 - x2 == 0) {
			isVertical = true;
		} else {
			isVertical = false;
			slope = ((double) y1 - (double) y2) / ((double) x1 - (double) x2);
			intercept = y1 - slope * x1;
		}
	}
	public double collideRayDown (int x, int y) {
		if (isBetween (x, x1, x2)) {
			if (!isVertical) {
				double result = slope * x + intercept;
				if (result >= (double) y) {
					return result;
				} else {
					return -1d;
				}
			} else {
				if (x1 == x) {
					return y1;
				} else {
					return -1;
				}
			}
		} else {
			return -1;
		}
	}
	public double collideScaline (int y) {
		if (isBetween (y, y1, y2)) {
			if (slope != 0) {
				if (!isVertical) {
					return ((double) y - intercept) / slope;
				} else {
					return (double) x1;
				}
			} else {
				return (double) x1;
			}
		} else {
			return -1d;
		}
	}
	public boolean isBetween (int a, int b, int c) {
		if (c > b) {
			return (a >= b && a <= c);
		} else {
			return (a <= b && a >= c);
		}
	}
}