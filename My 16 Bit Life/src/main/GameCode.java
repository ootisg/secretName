package main;

import resources.AnimationHandler;
import resources.Sprite;
import resources.Spritesheet;
import enemies.DuoflyPlus;
import enemies.GreenBlob;
import enemies.DuoflyMinus;
import players.Jeffrey;
import gameObjects.AimableWeapon;
import enemies.CreepyButterfly;

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
	Gui gui;
	Tbox tbox;
	ListTbox ltbox;
	public void initialize () {
		gameWindow = MainLoop.getWindow ();
		//Initialize sprites
		//GameObject initialization
		testJeffrey = new Jeffrey ();
		gui = new Gui ();
		gui.declare (0, 0);
		new GreenBlob ().declare (96, 192);
		//GameObject declaration
		//textbox = new Textbox ();
		tbox = new Tbox (0, 32, 16, 2, "HELLOTHISISTHEAWESOMEJEFFREY1234567890ANDTHEREARELOTSOFLINESTOTHISTEXT!");
		ltbox = new ListTbox (0, 128, new String[] {"OPTION A", "OPTION B", "OPTION C"});
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
		int selectedval = ltbox.getSelected ();
		//textbox.textBoxCreator ("HELLOTHISISTHEAWESOMEJEFFREY1234567890", 128, 32, 128, 128);
	}
}