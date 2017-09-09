package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;

public class Console {
	//Commands are:
	//-loadmap <name>
	//-createinstance <name> <x> <y>
	//-getinstance <name> <id>
	//-gettype
	//-getposition
	//-delete
	private boolean enabled = false;
	private GameWindow window;
	private int currentLine = 0;
	String[] text = new String[40];
	GameObject objectRef;
	public Console () {
		window = MainLoop.getWindow ();
		text [0] = "-";
		for (int i = 1; i < text.length; i ++) {
			text [i] = "";
		}
		objectRef = null;
	}
	public boolean isEnabled () {
		return enabled;
	}
	public void enable () {
		this.enabled = true;
		text [0] = "-";
		for (int i = 1; i < text.length; i ++) {
			text [i] = "";
		}
	}
	public void enable (String message) {
		this.enabled = true;
		text [0] = message;
		text [1] = "-";
		this.currentLine = 1;
		for (int i = 2; i < text.length; i ++) {
			text [i] = "";
		}
	}
	public void disable () {
		this.enabled = false;
	}
	public void addChar (char c) {
		if ((int) c >= 0x20 && (int) c != 0x7F) {
			text [currentLine] += c;
		} else {
			if ((int) c == KeyEvent.VK_DELETE || (int) c == KeyEvent.VK_BACK_SPACE) {
				if (text [currentLine].length () > 0) {
					text [currentLine] = text [currentLine].substring (0, text [currentLine].length () - 1);
				}
			}
			if ((int) c == KeyEvent.VK_ENTER) {
				this.doNewline ();
			}
		}
	}
	public void doNewline () {
		String command = text [currentLine];
		if (currentLine < text.length) {
			currentLine ++;
		} else {
			for (int i = 1; i < text.length; i ++) {
				text [i - 1] = text [i];
			}
		}
		evaluate (command);
		if (!text [currentLine].equals ("-")) {
			if (currentLine < text.length) {
				currentLine ++;
			} else {
				for (int i = 1; i < text.length; i ++) {
					text [i - 1] = text [i];
				}
			}
			text [currentLine] = "-";
		}
	}
	public void evaluate (String command) {
		if (command.equals ("-exit")) {
			this.disable ();
			return;
		}
		String[] cmd = command.split (" ");
		if (cmd.length == 1) {
			if (cmd [0].equals ("-gettype")) {
				try {
					if (objectRef != null) {
						text [currentLine] = "Object is type " + MainLoop.getObjectMatrix ().getStringId (objectRef.getId ());
					} else {
						text [currentLine] = "Error: referenced object is null";
					}
				}
				catch (Throwable e) {
					text [currentLine] = "Error: object does not exist";
				}
			}
			if (cmd [0].equals ("-getposition")) {
				try {
					if (objectRef != null) {
						text [currentLine] = "The selected object is at " + String.valueOf (objectRef.getX ()) + ", " + String.valueOf (objectRef.getY ());
					} else {
						text [currentLine] = "Error: referenced object is null";
					}
				}
				catch (Throwable e) {
					text [currentLine] = "Error: object does not exist";
				}
			}
			if (cmd [0].equals ("-delete")) {
				if (objectRef != null) {
					objectRef.forget ();
					text [currentLine] = "The selected object has been deleted";
				} else {
					text [currentLine] = "Error: object does not exist";
				}
			}
			return;
		}
		if (cmd.length == 2) {
			if (cmd [0].equals ("-loadmap")) {
				try {
					GameAPI.room.loadRoom ("resources/maps/" + cmd [1]);
				} catch (FileNotFoundException e) {
					text [currentLine] = "Error: file could not be found";
				} finally {

				}
				if (!text [currentLine].equals ("Error: file could not be found")) {
					text [currentLine] = "Map successfully loaded";
				}
				return;
			}
		} else if (cmd.length == 3) {
			if (cmd [0].equals ("-getinstance")) {
				if (MainLoop.getObjectMatrix ().getTypeId (cmd [1]) != -1) {
					objectRef = MainLoop.getObjectMatrix ().get (MainLoop.getObjectMatrix ().getTypeId (cmd [1]), Integer.parseInt (cmd [2]));
					if (objectRef == null) {
						text [currentLine] = "Error: object does not exist";
					} else {
						text [currentLine] = "Object has been selected";
					}
				} else {
					text [currentLine] = "Error: instance does not exist";
				}
				return;
			}
		} else if (cmd.length == 4) {
			if (cmd [0].equals ("-newinstance")) {
				addObject (cmd [1], Integer.parseInt (cmd [2]), Integer.parseInt (cmd [3]));
				return;
			}
		}
		text [currentLine] = "-";
	}
	public void addObject (String name, int x, int y) {
		Class<?> objectClass = null;
		try {
			objectClass = Class.forName (name);
		}
		catch (ClassNotFoundException e) {
			text [currentLine] = "Error: class not found";
			return;
		}
		catch (IllegalArgumentException e) {
			text [currentLine] = "Error: object cannot be created";
			return;
		}
		try {
			GameObject obj = (GameObject) objectClass.newInstance ();
			obj.declare (x, y);
		} catch (InstantiationException e) {
			text [currentLine] = "Error: object cannot be created";
			return;
		} catch (IllegalAccessException e) {
			text [currentLine] = "Error: object cannot be created";
			return;
		}
		text [currentLine] = "The object was successfully created";
	}
	public void render () {
		Graphics g = window.getBuffer ();
		g.setColor (new Color (0));
		g.fillRect (0, 0, 640, 480);
		g.setColor (new Color (0xFFFFFF));
		for (int i = 0; i < text.length; i ++) {
			g.drawString (text [i], 0, 11 + i * 12);
		}
	}
}
