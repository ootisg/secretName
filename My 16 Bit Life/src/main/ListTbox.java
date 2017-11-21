package main;

public class ListTbox extends Tbox {
	String[] options;
	int selected;
	private boolean complete;
	public ListTbox (double x, double y, String[] options) {
		//Initialize parameters
		this.declare (x, y);
		int longestWidth = 0;
		for (int i = 0; i < options.length; i ++) {
			if (options [i].length () > longestWidth) {
				longestWidth = options [i].length ();
			}
		}
		this.width = longestWidth + 1;
		this.height = options.length;
		this.options = options;
	}
	@Override
	public void draw () {
		//Draws the top bar, bottom bar, and background
		int x = (int)this.getX ();
		int y = (int)this.getY ();
		for (int i = 0; i < this.width; i ++) {
			sprites.textBorder.setFrame (0);
			sprites.textBorder.draw (x + i * 8 + 1, y);
			sprites.textBorder.setFrame (3);
			sprites.textBorder.draw (x + i * 8 + 1, y + (this.height + 1) * 8);
			for (int j = 0; j < this.height; j ++) {
				sprites.textBorder.setFrame (1);
				sprites.textBorder.draw (x + i * 8 + 1, y + j * 8 + 8);
			}
		}
		//Draws the two side bars
		for (int i = 0; i < this.height + 1; i ++) {
			sprites.textBorder.setFrame (2);
			sprites.textBorder.draw (x, y + i * 8);
			sprites.textBorder.draw (x + this.width * 8 + 1, y + i * 8);
		}
		//Draws the text
		for (int i = 0; i < height; i ++) {
			for (int j = 0; j < options [i].length (); j ++) {
				sprites.font.setFrame ((int)options [i].charAt (j));
				sprites.font.draw (x + j * 8 + 8, y + i * 8 + 8);
			}
		}
		//Draws the selector
		sprites.selector.draw (x + 1, y + selected * 8 + 8);
	}
	@Override
	public void frameEvent () {
		//Handles key presses
		if (!complete) {
			if (keyPressed ((int)'W')) {
				selected --;
			}
			if (keyPressed ((int)'S')) {
				selected ++;
			}
			if (selected < 0) {
				selected = height - 1;
			}
			if (selected >= height) {
				selected = 0;
			}
			if (keyPressed ((int)'A')) {
				this.select ();
			}
		}
	}
	@Override
	public void pausedEvent () {
		//Handles key presses
		if (!complete) {
			if (keyPressed ((int)'W')) {
				selected --;
			}
			if (keyPressed ((int)'S')) {
				selected ++;
			}
			if (selected < 0) {
				selected = height - 1;
			}
			if (selected >= height) {
				selected = 0;
			}
			if (keyPressed ((int)'A')) {
				this.select ();
			}
		}
	}
	public void select () {
		//Doesn't close the window, but sets it ready to be closed
		complete = true;
	}
	public void deselect () {
		//Deselects the currently selected option
		complete = false;
	}
	@Override
	public void close () {
		this.forget ();
	}
	public int getSelected () {
		//Closes the window and returns the option selected if the window is ready to be closed
		//Returns -1 if the window is not ready to be closed
		if (!complete) {
			return -1;
		} else {
			return selected;
		}
	}
	public String getSelectedValue () {
		if (!complete) {
			return "NULL";
		} else {
			return options [selected];
		}
	}
}
