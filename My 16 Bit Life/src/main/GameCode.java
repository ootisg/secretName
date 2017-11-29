package main;

import resources.AnimationHandler;
import resources.Sprite;
import resources.Spritesheet;
import enemies.DuoflyPlus;
import enemies.GreenBlob;
import enemies.Slimelet;
import enemies.DuoflyMinus;
import players.Jeffrey;
import players.TopDown;
import gameObjects.AimableWeapon;
import graphics3D.RayCamera;
import graphics3D.VectorCamera;
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
	TopDown td;
	Textbox textbox;
	Gui gui;
	Tbox tbox;
	VectorCamera cam;
	ListTbox ltbox;
	public void initialize () {
		gameWindow = MainLoop.getWindow ();
		//Initialize sprites
		//GameObject initialization
		testJeffrey = new Jeffrey ();
		gui = new Gui ();
		//td = new TopDown ();
		//cam = new VectorCamera (0, 0);
		//new Slimelet ().declare (0, 16);// From when I was messing around with slimelets =P
		//Uncomment the above line if you want to see them
		//GameObject declaration
		//textbox = new Textbox ();
		//tbox = new Tbox (0, 32, 16, 2, "HELLOTHISISTHEAWESOMEJEFFREY1234567890ANDTHEREARELOTSOFLINESTOTHISTEXT!");
		//ltbox = new ListTbox (0, 128, new String[] {"OPTION A", "OPTION B", "OPTION C"});
		try {
			room.loadRoom ("resources/maps/snowmap.cmf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void gameLoop () {
		room.frameEvent (); //Draws the tiles
		//int selectedval = ltbox.getSelected ();
		//textbox.textBoxCreator ("HELLOTHISISTHEAWESOMEJEFFREY12", 128, 32, 128, 128);
	}
}