package main;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class GameWindow extends JFrame {
	boolean[] keysPressed;
	boolean[] keysPressedOnFrame;
	boolean[] keysReleasedOnFrame;
	ExtendedMouseListener mouseListener;
	ExtendedMouseMotionListener motionListener;
	BufferedImage bufferImage;
	BufferedImage rasterImage;
	BufferedImage consoleImage;
	WritableRaster bufferRaster;
	Graphics bufferGraphics;
	Insets insets;
	int numtest = 0;
	int rasterMode = 1;
	int[] resolution = {640, 480};
	int[] mouseCoords = null;
	int[] imageData = new int[640 * 480];
	public static final int NO_RASTER = 0;
	public static final int RASTER_ONLY = 1;
	public static final int RASTER_BACKGROUND = 2;
	public static final int RASTER_OVERLAY = 3;
	public GameWindow () {
		//Create buffers
		bufferImage = new BufferedImage (640, 480, BufferedImage.TYPE_INT_ARGB); //Used for sprites
		rasterImage = new BufferedImage (640, 480, BufferedImage.TYPE_INT_ARGB);
		consoleImage = new BufferedImage (640, 480, BufferedImage.TYPE_INT_RGB); //The buffer for the dev console
		keysPressed = new boolean[256]; //Array for tracking which keys are down
		keysPressedOnFrame = new boolean[256]; //Array for tracking which keys have just been pressed
		keysReleasedOnFrame = new boolean[256]; //Array for tracking which keys have just been released
		bufferGraphics = bufferImage.getGraphics (); //Get a graphics interface for bufferedimage
		bufferRaster = rasterImage.getRaster ();
		//Makes sure that java closes
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit (0);
			}
		});
		this.pack (); //This line makes the code work for some reason
		this.insets = this.getInsets(); //Get offsets for the actual display part of the window
		this.setSize (640 + insets.left + insets.right, 480 + insets.top + insets.bottom); //Sets the size of the window to get a useable size of 640x480
		this.setVisible (true); //Makes the window visible
		mouseListener = new ExtendedMouseListener (insets.left, insets.top); //Makes a mouse listener
		motionListener = new ExtendedMouseMotionListener (insets.left, insets.top); //Makes a mouse motion listener
		this.addMouseListener (mouseListener);
		this.addMouseMotionListener (motionListener);
		//This section handles keystroke detection
		KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager ();
		keyboardFocusManager.addKeyEventDispatcher (new KeyEventDispatcher () {
			@Override
			public boolean dispatchKeyEvent (KeyEvent e) {
				if (e.getID () == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode () <= 255) {
						keysPressed [e.getKeyCode ()] = true;
						keysPressedOnFrame [e.getKeyCode ()] = true;
						if (e.getKeyCode () == 0x7F) {
							//Enable the dev console if delete (0x7F) is pressed 
							MainLoop.getConsole ().enable ();
						} else if (MainLoop.getConsole ().isEnabled ()) {
							//Adds characters to the dev console if it is enabled
							if (e.getKeyCode () >= 0x20 && e.getKeyCode () != 0x7F) {
								MainLoop.getConsole ().addChar (e.getKeyChar ());
							} else {
								MainLoop.getConsole ().addChar ((char) e.getKeyCode ());
							}
						}
					}
				}
				if (e.getID () == KeyEvent.KEY_RELEASED) {
					keysPressed [e.getKeyCode ()] = false;
					keysReleasedOnFrame [e.getKeyCode ()] = true;
				}
				return false;
			}
		});
	}
	public void updateClick () {
		//This method updates click information
		mouseCoords = mouseListener.getClick ();
		if (motionListener.getClicked ()) {
			mouseCoords = motionListener.getMouseCoords ();
		}
	}
	public int[] getClick () {
		//Returns the coordinates of the last click, or null if there wasn't a click this frame
		return mouseCoords;
	}
	public void doPaint () {
		//Refreshes the screen
		Graphics g = this.getGraphics ();
		if (bufferImage != null) {
			if (bufferGraphics != null) {
				Console console = MainLoop.getConsole ();
				if (!console.isEnabled ()) {
					bufferGraphics.drawRect (getMouseX (), getMouseY (), 2, 2);
					if (rasterMode != 0) {
						bufferRaster.setDataElements (0, 0, rasterImage.getWidth (), rasterImage.getHeight (), imageData);
					}
					if (rasterMode == 0) {
						g.drawImage (bufferImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
					}
					if (rasterMode == 1) {
						g.drawImage (rasterImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
					}
					if (rasterMode == 2) {
						g.drawImage (rasterImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
						g.drawImage (bufferImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
					}
					if (rasterMode == 3) {
						g.drawImage (bufferImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
						g.drawImage (rasterImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
					}
					if (rasterMode != 0) {
						for (int i = 0; i < imageData.length; i ++) {
							imageData [i] = 0xFF000000;
						}
					}
				} else {
					console.render ();
					g.drawImage (bufferImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
				}
			}
		}
		int[] usedResolution = this.getResolution ();
		bufferGraphics.setColor (new Color (0xC0C0C0));
		//Fills the screen with grey
		bufferGraphics.fillRect (0, 0, bufferImage.getWidth () - 1, bufferImage.getHeight () - 1);
		/*for (int i = 0; i < usedResolution [0] * usedResolution [1]; i ++) {
			imageData [i] = 0xC0C0C0;
		}*/
		/*bufferImage = new BufferedImage (resolution [0], resolution [1], BufferedImage.TYPE_INT_ARGB);
		if (bufferImage != null) {
			bufferGraphics = bufferImage.getGraphics ();
		}*/
		//bufferImage.flush ();
	}
	public void clearKeyArrays () {
		for (int i = 0; i < 256; i ++) {
			keysPressedOnFrame [i] = false;
			keysReleasedOnFrame [i] = false;
		}
	}
	public boolean keyCheck (int keyCode) {
		if (keyCode > 0 && keyCode <= 255) {
			return keysPressed [keyCode];
		} else {
			return false;
		}
	}
	public boolean keyPressed (int keyCode) {
		if (keyCode > 0 && keyCode <= 255) {
			return keysPressedOnFrame [keyCode];
		} else {
			return false;
		}
	}
	public boolean keyReleased (int keyCode) {
		if (keyCode > 0 && keyCode <= 255) {
			return keysReleasedOnFrame [keyCode];
		} else {
			return false;
		}
	}
	public Graphics getBuffer () {
		return bufferGraphics;
	}
	public int[] getResolution () {
		return resolution;
	}
	public int getMouseX () {
		return (int) (motionListener.getMouseCoords ()[0] * ((double) this.getResolution ()[0] / (this.getWidth () - insets.left - insets.right)));
	}
	public int getMouseY () {
		return (int) (motionListener.getMouseCoords ()[1] * ((double) this.getResolution ()[1] / (this.getHeight () - insets.top - insets.bottom)));
	}
	public void setResolution (int width, int height) {
		int[] usedResolution = {width, height};
		resolution = usedResolution;
		bufferImage = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
		rasterImage = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
		bufferRaster = rasterImage.getRaster ();
		bufferGraphics = bufferImage.getGraphics ();
		imageData = new int[width * height];
	}
	public void setRasterMode (int mode) {
		rasterMode = mode;
	}
	public int getRasterMode () {
		return rasterMode;
	}
	public int[] getImageData () {
		return imageData;
	}
}