package main;

import java.awt.event.MouseMotionListener;

import java.awt.event.MouseEvent;

public class ExtendedMouseMotionListener implements MouseMotionListener {
	private int xOffset;
	private int yOffset;
	private int[] mouseCoords;
	private boolean isClicked;
	private boolean clickDetected;
	public ExtendedMouseMotionListener (int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.isClicked = false;
		this.clickDetected = false;
		mouseCoords = new int[] {0, 0};
	}
	@Override
	public void mouseMoved (MouseEvent event) {
		mouseCoords [0] = event.getX () - xOffset;
		mouseCoords [1] = event.getY () - yOffset;
		this.isClicked = false;
	}
	@Override
	public void mouseDragged (MouseEvent event) {
		mouseCoords [0] = event.getX () - xOffset;
		mouseCoords [1] = event.getY () - yOffset;
		if (!this.isClicked) {
			clickDetected = true;
		}
		this.isClicked = true;
	}
	public int[] getMouseCoords () {
		return mouseCoords;
	}
	public boolean getClicked () {
		boolean temp = clickDetected;
		clickDetected = false;
		return temp;
	}
}