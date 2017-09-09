package main;
import java.awt.event.KeyEvent;

import main.GameAPI;
import resources.Sprite;
import resources.Spritesheet;



public class Textbox extends GameAPI {
	int timer;
	int amountToDraw = 1;
	boolean isFinished;
	boolean finalCheck = false;
	boolean isDone = false;
	int spaceManipulation;
	public Sprite textBoxTop;
	public Sprite textBoxBottum;
	public Sprite textBoxBackground;
	public Sprite textBoxSides;
	public Sprite fontSheet;
	String message;
	int isScrolled = 0;
	public Textbox (){
	Spritesheet borderSheet;
	Spritesheet FontSheet;
	borderSheet = new Spritesheet ("resources/sprites/windowsprites2.png");
	FontSheet = new Spritesheet ("resources/sprites/text.png");
	fontSheet = new Sprite (FontSheet, 8, 8);
	textBoxTop = new Sprite (borderSheet, 0, 0, 8, 8); 
	textBoxBottum = new Sprite (borderSheet, 24, 0, 8, 1);
	textBoxSides = new  Sprite (borderSheet, 16, 0, 1, 8);
	textBoxBackground = new Sprite (borderSheet, 17, 0, 8, 8);
	isFinished = false;
	spaceManipulation = 0;
	}
public void textBoxCreator (String text, int width, int height, int x_origin, int y_origin){
	if (finalCheck && isFinished && (keyPressed(65) || keyPressed (97) || isDone)){
		isDone = true;
		return;
	}
	else {
	int space = 0;
	timer = timer + 1;
	int textLength = text.length();
	width = width/8;
	height = height/8;
	int width_start = width;
	int width_beginning = width;
	int width_basis = width;
	int height_start = height;
	height_start = height_start - 2;
	int x_start = x_origin;
	int x_beginning = x_origin;
	int x_basis = x_origin;
	int y_start = y_origin;
	while (width > 1){
	textBoxTop.draw (x_start, y_start);
	width = width - 1;
	x_start = x_start + 8;
		}
	while (height > 1){
	textBoxSides.draw(x_origin, y_origin);
	textBoxSides.draw(x_start, y_origin);
	height = height - 1;
	y_origin = y_origin + 8;
		}
	while (width_start > 1){
		textBoxBottum.draw (x_origin, y_origin);
		width_start = width_start - 1;
		x_origin = x_origin + 8;
			}
	y_origin = y_start;
	x_origin = x_basis;
	int x = 0;
	while (x < height_start){
		width_beginning = width_basis;
		y_start = y_start + 8;
		x_beginning = x_basis;
		x = x + 1;
		while (width_beginning > 1){
			textBoxBackground.draw (x_beginning, y_start);
			width_beginning = width_beginning - 1;
			x_beginning = x_beginning + 8;
			space = space + 1;
			}
		}
	x_beginning = x_origin;
	int spaceBasis = space;
	int charictarNumber = 0;
	int spaceManipulationPlusSpace = 0;
	int textLengthAtBeginning = textLength;
	width_beginning = width_basis;
	if (space < text.substring (spaceManipulation, text.length () - 1).length ()) {
		message = text.substring(spaceManipulation,spaceManipulation + spaceBasis);
	}
	y_origin = y_origin + 8;
	if (((keyPressed(65) || keyPressed(97)) && isFinished) || message.length () == 0){
		
		amountToDraw = 0;
		if (spaceManipulation <= textLength - spaceManipulation && isFinished) {
			spaceManipulation = spaceManipulation + spaceBasis;
		}
		isFinished = false;
		spaceManipulationPlusSpace = spaceManipulation + spaceBasis;
		if (spaceManipulationPlusSpace >= textLengthAtBeginning){
			spaceManipulationPlusSpace = textLengthAtBeginning;
			isScrolled = space;
			}
		if (text.substring (spaceManipulation, text.length () - 1).length () >= 30) {
			spaceBasis = 30;
		} else {
			spaceBasis = text.length () - spaceManipulation;
		}
		message = text.substring(spaceManipulation,spaceManipulationPlusSpace);
}
		textLength = textLength - isScrolled;
		if (timer == 2) {
			timer = 0;
			amountToDraw = amountToDraw + 1;
		}
		int amountToDrawBasis = amountToDraw;
		if ((spaceManipulation + spaceBasis) >= textLength) {
			finalCheck = true;
			space = message.length() - 1;
		}
	for (int n = 0; n < textLength; textLength = textLength - 1){
		if (width_basis == 1){
			y_origin = y_origin + 8;
			x_beginning = x_origin;
			width_basis = width_beginning;
		}
		if (space <= 0 && amountToDraw >= message.length()) {
			isFinished = true;
		}	
		if (amountToDrawBasis > 0) {
			amountToDrawBasis = amountToDrawBasis - 1;
			if (charictarNumber < message.length()){
				char charictarInQuestion = message.charAt(charictarNumber);
				charictarNumber = charictarNumber + 1;
				width_basis = width_basis - 1;
				int charitarCode = KeyEvent.getExtendedKeyCodeForChar(charictarInQuestion);
				if (charitarCode == 32) {
					x_beginning = x_beginning + 8;
					space = space - 1;
				}
				else{
				fontSheet.setFrame(charitarCode);
				fontSheet.draw(x_beginning, y_origin);
				x_beginning = x_beginning + 8;
				space = space - 1;
						}
					}
				}
			}
		}
	}
}