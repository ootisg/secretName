package main;

public class Tbox extends GameObject {
	//Lightweight textbox object
	//Alternatively, Textbox can be used
	protected int width;
	protected int height;
	protected String text;
	protected int frameCount;
	protected int scrollTime;
	protected int letterPos;
	protected int startPos;
	public Tbox () {
		
	}
	public Tbox (double x, double y, int width, int height, String text) {
		//Initialize parameters
		this.declare (x, y);
		this.width = width;
		this.height = height;
		this.text = text;
		this.scrollTime = 2;
		this.frameCount = 0;
		this.startPos = 0;
		this.letterPos = 0;
	}
	@Override
	public void draw () {
		//Get the x and y coordinates as integers
		int x = (int)this.getX ();
		int y = (int)this.getY ();
		//Draw the top bar, the background, and the bottom bar
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
		//Draw the side bars
		for (int i = 0; i < this.height + 1; i ++) {
			sprites.textBorder.setFrame (2);
			sprites.textBorder.draw (x, y + i * 8);
			sprites.textBorder.draw (x + this.width * 8 + 1, y + i * 8);
		}
		//Draw the text in the box
		for (int i = 0; i < letterPos; i ++) {
			sprites.font.setFrame ((int)text.charAt (startPos + i));
			sprites.font.draw (x + (i % width) * 8, y + (i / width) * 8 + 8);
		}
		//Handles scrolling
		int scrollLimit;
		if (startPos / (width * height) == text.length () / (width * height)) {
			scrollLimit = (text.length () % (width * height)) * scrollTime;
			//Closes the textbox if A is pressed and all the text has been displayed
			if (keyPressed((int)'A') && frameCount == scrollLimit) {
				this.close ();
			}
		} else {
			scrollLimit = width * height * scrollTime;
			//Scrolls the textbox if A is pressed and not all the text has been displayed
			if (keyPressed((int)'A') && frameCount == scrollLimit) {
				this.scroll ();
			}
		}
		//Applies the letter limit
		if (frameCount < scrollLimit) {
			frameCount ++;
		}
		//Sets the letter to draw to
		letterPos = frameCount / scrollTime;
	}
	public void setContent (String text) {
		this.text = text;
	}
	public void setWidth (int width) {
		this.width = width;
	}
	public void setHeight (int height) {
		this.height = height;
	}
	public void setFrames (int frames) {
		this.frameCount = frames;
	}
	public void setScrollRate (int rate) {
		this.scrollTime = rate;
	}
	public String getContent () {
		return this.text;
	}
	public int getWidth () {
		return this.width;
	}
	public int getHeight () {
		return this.height;
	}
	public int getFrames () {
		return this.frameCount;
	}
	public int getScrollRate () {
		return this.scrollTime;
	}
	public void close () {
		//Adds extensability; this can be overriden to prevent the window from closing when A is pressed
		this.forget ();
	}
	public void scroll () {
		//Adds extensability; this can be overriden to prevent the window from scrolling when A is pressed
		this.frameCount = 0;
		this.startPos += (width * height);
		this.letterPos = 0;
	}
}
