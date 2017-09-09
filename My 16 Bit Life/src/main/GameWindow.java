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
	WritableRaster rasterBuffer;
	Graphics bufferGraphics;
	Insets insets;
	int numtest = 0;
	int[] imageData;
	int[] resolution = {640, 480};
	int[] mouseCoords = null;
	public GameWindow () {
		bufferImage = new BufferedImage (640, 480, BufferedImage.TYPE_INT_ARGB);
		rasterImage = new BufferedImage (640, 480, BufferedImage.TYPE_INT_RGB);
		consoleImage = new BufferedImage (640, 480, BufferedImage.TYPE_INT_RGB);
		rasterBuffer = rasterImage.getRaster ();
		keysPressed = new boolean[256];
		keysPressedOnFrame = new boolean[256];
		keysReleasedOnFrame = new boolean[256];
		imageData = new int [307200];
		bufferGraphics = bufferImage.getGraphics ();
		for (int i = 0; i < 307200; i ++) {
			imageData [i] = 0xC0C0C0;
		}
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit (0);
			}
		});
		this.pack ();
		this.insets = this.getInsets();
		this.setSize (640 + insets.left + insets.right, 480 + insets.top + insets.bottom);
		this.setVisible (true);
		mouseListener = new ExtendedMouseListener (insets.left, insets.top);
		motionListener = new ExtendedMouseMotionListener (insets.left, insets.top);
		this.addMouseListener (mouseListener);
		this.addMouseMotionListener (motionListener);
		KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager ();
		keyboardFocusManager.addKeyEventDispatcher (new KeyEventDispatcher () {
			@Override
			public boolean dispatchKeyEvent (KeyEvent e) {
				if (e.getID () == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode () <= 255) {
						keysPressed [e.getKeyCode ()] = true;
						keysPressedOnFrame [e.getKeyCode ()] = true;
						if (e.getKeyCode () == 0x7F) {
							MainLoop.getConsole ().enable ();
						}
						if (MainLoop.getConsole ().isEnabled ()) {
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
		mouseCoords = mouseListener.getClick ();
		if (motionListener.getClicked ()) {
			mouseCoords = motionListener.getMouseCoords ();
		}
	}
	public int[] getClick () {
		return mouseCoords;
	}
	public void doPaint () {
		Graphics g = this.getGraphics ();
		if (bufferImage != null) {
			if (bufferGraphics != null) {
				Console console = MainLoop.getConsole ();
				if (!console.isEnabled ()) {
					bufferGraphics.drawRect (getMouseX (), getMouseY (), 2, 2);
					rasterBuffer.setDataElements (0, 0, resolution [0], resolution [1], imageData);
				} else {
					console.render ();
				}
				rasterImage.getGraphics ().drawImage (bufferImage, 0, 0, null);
				g.drawImage (rasterImage, insets.left, insets.top, this.getContentPane ().getWidth (), this.getContentPane ().getHeight (), null);
			}
		}
		int[] usedResolution = this.getResolution ();
		bufferGraphics.setColor (new Color (0xC0C0C0));
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
	public int[] getImageData () {
		return imageData;
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
		imageData = new int[width * height];
		bufferImage = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);
		rasterImage = new BufferedImage (width, height, BufferedImage.TYPE_INT_RGB);
		rasterBuffer = rasterImage.getRaster ();
		for (int i = 0; i < width * height; i ++) {
			imageData [i] = 0xC0C0C0;
		}
	}
}