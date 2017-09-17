package main;

import resources.AnimationHandler;
import resources.Sprite;
import resources.Spritesheet;
import gameObjects.DuoflyPlus;
import gameObjects.DuoflyMinus;
import players.Jeffrey;
import gameObjects.AimableWeapon;
import gameObjects.CreepyButterfly;

import java.awt.Image;
import java.awt.image.*;
import java.io.FileNotFoundException;

public class GameCode extends GameAPI {
	private GameWindow gameWindow;
	//GameObjects
	AimableWeapon wpn;
	CreepyButterfly newFly;
	DuoflyPlus testFly;
	DuoflyMinus testFly2;
	Jeffrey testJeffrey;
	Textbox textbox;
	public void initialize () {
		gameWindow = MainLoop.getWindow ();
		//Initialize sprites
		//GameObject initialization
		testJeffrey = new Jeffrey ();
		//GameObject declaration
		textbox = new Textbox ();
		try {
			room.loadRoom ("resources/maps/snowmap.cmf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println (room.isColliding (0, 0, 128, 128));
	}
	public void gameLoop () {
		room.frameEvent (); //Draws the tiles
		textbox.textBoxCreator ("HELLOTHISISTHEAWESOMEJEFFREY1234567890", 128, 32, 128, 128);
	}
}