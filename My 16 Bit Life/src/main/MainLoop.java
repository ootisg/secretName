package main;

import java.awt.Toolkit;

import resources.SpriteContainer;

public class MainLoop {
	private static GameWindow gameWindow;
	private static SpriteContainer sprites;
	private static ObjectMatrix objectMatrix;
	private static Console console;
	private static int delay;
	public static void main (String[] args) {
		//Test message
		//Test message 2
		double framerate = 30;
		boolean running = true;
		long startTime;
		long delay = 0;
		gameWindow = new GameWindow ();
		objectMatrix = new ObjectMatrix ();
		sprites = new SpriteContainer ();
		console = new Console ();
		GameCode gameCode = new GameCode ();
		gameCode.initialize ();
		while (running) {
			startTime = System.currentTimeMillis();
			try {
				gameWindow.updateClick ();
				if (!console.isEnabled ()) {
					gameCode.gameLoop ();
					objectMatrix.callAll ();
				}
				gameWindow.clearKeyArrays ();
				gameWindow.doPaint ();
			}
			catch (Throwable e) {
				e.printStackTrace ();
				console.enable ("A runtime error has occured: " + e.getClass ());
			}
			delay = System.currentTimeMillis () - startTime;
			//System.out.println (System.currentTimeMillis() - startTime);
			if (delay < 33) {
				try {
					Thread.sleep (1000 / (long)framerate - delay - 1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			while (System.currentTimeMillis() - startTime < 1000 / (long)framerate) {
			}
		}
	}
	public static GameWindow getWindow () {
		return gameWindow;
	}
	public static SpriteContainer getSprites () {
		return sprites;
	}
	public static ObjectMatrix getObjectMatrix () {
		return objectMatrix;
	}
	public static Console getConsole () {
		return console;
	}
	public static long getDelay () {
		return delay;
	}
}