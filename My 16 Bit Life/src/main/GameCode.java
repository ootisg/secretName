package main;

import resources.AnimationHandler;
import resources.Sprite;
import resources.Spritesheet;
import statusEffect.Poison;
import statusEffect.Status;
import gameObjects.DuoflyPlus;
import gameObjects.GreenBlob;
import gameObjects.Ladder;
import gameObjects.Slimelet;
import gameObjects.TestObject;
import gameObjects.DuoflyMinus;
import players.Jeffrey;
import players.TopDown;
import players.TubeRaster;
import weapons.AimableWeapon;
import graphics3D.RayCamera;
import graphics3D.VectorCamera;
import gameObjects.CannonTankEnemy;
import gameObjects.ClostridiumBowtielinea;
import gameObjects.CreepyButterfly;
import gameObjects.CyclopesCrab;

import java.awt.Image;
import java.awt.image.*;
import java.io.FileNotFoundException;

public class GameCode extends GameAPI {
	private GameWindow gameWindow;
	//GameObjects
	TubeRaster raster;
	AimableWeapon wpn;
	CreepyButterfly newFly;
	DuoflyPlus testFly;
	DuoflyMinus testFly2;
	public static Jeffrey testJeffrey;
	TopDown td;
	Textbox textbox;
	Gui gui;
	Tbox tbox;
	VectorCamera cam;
	ListTbox ltbox;
	CannonTankEnemy showTank;
	CyclopesCrab testCrab;
	ClostridiumBowtielinea testTie;
	Status testStatus;
	Ladder testLaddder;
	public void initialize () {
		gameWindow = MainLoop.getWindow ();
		gameWindow.setResolution (640, 480);
		//Initialize sprites
		//GameObject initialization
		testLaddder = new Ladder ();
		testJeffrey = new Jeffrey ();
		showTank = new CannonTankEnemy();
		gui = new Gui ();
		testCrab = new CyclopesCrab();
		testTie = new ClostridiumBowtielinea();
	
		//cam = new VectorCamera (0, 0);
		//Uncomment the above line if you want to see them
		//GameObject declaration
		//textbox = new Textbox ();
		//tbox = new Tbox (0, 32, 16, 2, "HELLOTHISISTHEAWESOMEJEFFREY1234567890ANDTHEREARELOTSOFLINESTOTHISTEXT!");
		//ltbox = new ListTbox (0, 128, new String[] {"OPTION A", "OPTION B", "OPTION C"});
		//WARNING: LOADING A ROOM PURGES ALL THE OBJECTS USING THE FORGET METHOD
		//Add the following to an object to a class to keep it around: @Override public void forget () {}
		try {
			room.loadRoom ("resources/maps/snowmap.cmf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//new TestObject ().declare (128, 200);
		//new Slimelet ().declare (200, 400);// From when I was messing around with slimelets =P
		//td = new TopDown ();
		testLaddder.declare(150, 195);
	}
	public void gameLoop () {
		room.frameEvent (); //Draws the tiles
		//int selectedval = ltbox.getSelected ();
		//textbox.textBoxCreator ("HELLOTHISISTHEAWESOMEJEFFREY12", 128, 32, 128, 128);
	}
}