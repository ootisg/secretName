package main;

import java.util.ArrayList;

import map.TileMap;
import resources.AnimationHandler;
import resources.Sprite;

public abstract class GameObject extends GameAPI {
	protected int[] matrixLocation;
	private double x;
	private double y;
	private double xprevious;
	private double yprevious;
	private int hitboxXOffset;
	private int hitboxYOffset;
	private Sprite sprite;
	private AnimationHandler animationHandler = new AnimationHandler (sprite);
	private Hitbox hitbox;
	private boolean animationEnabled;
	private boolean flipHorizontal = false;
	private boolean flipVertical = false;
	protected TileMap tileMap;
	public void declare (double x, double y) {
		matrixLocation = MainLoop.getObjectMatrix ().add (this);
		this.x = x;
		this.y = y;
		xprevious = x;
		yprevious = y;
	}
	public void forget () {
		MainLoop.getObjectMatrix ().remove (matrixLocation);
	}
	public void setPosition (double x, double y) {
		xprevious = this.x;
		yprevious = this.y;
		this.x = x;
		this.y = y;
	}
	public void setX (double x) {
		xprevious = this.x;
		this.x = x;
	}
	public void setY (double y) {
		yprevious = this.y;
		this.y = y;
	}
	public void setX (int x) {
		xprevious = (double) this.x;
		this.x = (double) x;
	}
	public void setY (int y) {
		yprevious = (double) this.y;
		this.y = (double) y;
	}
	public void backstep () {
		//Sets coords to previous values
		x = xprevious;
		y = yprevious;
	}
	public void backstepX () {
		x = xprevious;
	}
	public void backstepY () {
		y = yprevious;
	}
	public void setHitboxXOffset (int hitboxXOffset) {
		this.hitboxXOffset = hitboxXOffset;
	}
	public void setHitboxYOffset (int hitboxYOffset) {
		this.hitboxYOffset = hitboxYOffset;
	}
	public void setSprite (Sprite sprite) {
		this.sprite = sprite;
		if (sprite.getIsAnimated ()) {
			animationHandler.setSprite (sprite);
			animationEnabled = true;
		} else {
			animationEnabled = false;
		}
		animationHandler.setFrame (0);
	}
	public void createHitbox (int xoffset, int yoffset, int width, int height) {
		hitboxXOffset = xoffset;
		hitboxYOffset = yoffset;
		hitbox = new Hitbox ((int) x + xoffset, (int) y + yoffset, width, height);
	}
	public Hitbox getHitbox () {
		if (hitbox != null) {
			hitbox.x = (int) x + hitboxXOffset;
			hitbox.y = (int) y + hitboxYOffset;
			return hitbox;
		} else {
			return null;
		}
	}
	public void destroyHitbox () {
		hitbox = null;
	}
	public boolean isColliding (int x, int y, int width, int height) {
		Hitbox gameHitbox = new Hitbox (0, 0, width, height);
		if (gameHitbox == null || getHitbox () == null) {
			return false;
		} else if (getHitbox ().checkOverlap (gameHitbox)) {
			return true;
		}
		return false;
	}
	public boolean isColliding (GameObject gameObject) {
		if (gameObject.hitbox == null || getHitbox () == null) {
			return false;
		} else if (getHitbox ().checkOverlap (gameObject.hitbox)) {
			return true;
		}
		return false;
	}
	public boolean isColliding (GameObject gameObject, double xTo, double yTo) {
		if (gameObject.hitbox == null || getHitbox () == null) {
			return false;
		} else if (getHitbox ().checkVectorCollision (gameObject.hitbox, xTo, yTo) != null) {
			return true;
		}
		return false;
	}
	public boolean isColliding (String objectType) {
		if (this.hitbox != null) {
			ObjectMatrix objectMatrix = MainLoop.getObjectMatrix ();
			int nameListLength = objectMatrix.classNameList.size ();
			ArrayList<GameObject> objectList = null;
			for (int i = 0; i < nameListLength; i ++) {
				if (objectType.equals (objectMatrix.classNameList.get (i))) {
					objectList = objectMatrix.objectMatrix.get (i);
				}
			}			
			if (objectList != null) {
				int objectListLength = objectList.size ();
				for (int i = 0; i < objectListLength; i ++) {
					if (objectList.get (i) != null) {
						if (objectList.get (i).getHitbox () != null) {
							if (getHitbox ().checkOverlap (objectList.get (i).getHitbox ())) {
								return true;
							}
						}
					}
				}
				return false;
			}
		}
		return false;
	}
	public double[] getCollidingCoords (String objectType, double xTo, double yTo) {
		if (this.hitbox != null) {
			ObjectMatrix objectMatrix = MainLoop.getObjectMatrix ();
			int nameListLength = objectMatrix.classNameList.size ();
			ArrayList<GameObject> objectList = null;
			for (int i = 0; i < nameListLength; i ++) {
				if (objectType.equals (objectMatrix.classNameList.get (i))) {
					objectList = objectMatrix.objectMatrix.get (i);
				}
			}			
			if (objectList != null) {
				int objectListLength = objectList.size ();
				for (int i = 0; i < objectListLength; i ++) {
					if (objectList.get (i) != null) {
						if (objectList.get (i).getHitbox () != null) {
							double[] result = getHitbox ().checkVectorCollision (objectList.get (i).getHitbox (), xTo, yTo);
							if (result != null) {
								return result;
							}
						}
					}
				}
				return null;
			}
		}
		return null;
	}
	public GameObject getCollidingObject (String objectType, double xTo, double yTo) {
		if (this.hitbox != null) {
			ObjectMatrix objectMatrix = MainLoop.getObjectMatrix ();
			int nameListLength = objectMatrix.classNameList.size ();
			ArrayList<GameObject> objectList = null;
			for (int i = 0; i < nameListLength; i ++) {
				if (objectType.equals (objectMatrix.classNameList.get (i))) {
					objectList = objectMatrix.objectMatrix.get (i);
				}
			}			
			if (objectList != null) {
				int objectListLength = objectList.size ();
				for (int i = 0; i < objectListLength; i ++) {
					if (objectList.get (i) != null) {
						if (objectList.get (i).getHitbox () != null) {
							if (getHitbox ().checkVectorCollision (objectList.get (i).getHitbox (), xTo, yTo) != null) {
								return objectList.get (i);
							}
						}
					}
				}
				return null;
			}
		}
		return null;
	}
	public boolean isColliding (String objectType, double xTo, double yTo) {
		if (this.getCollidingCoords (objectType, xTo, yTo) != null) {
			return true;
		}
		return false;
	}
	public boolean isCollidingSameType () {
		String objectType = this.getClass ().getSimpleName ();
		if (this.hitbox != null) {
			ObjectMatrix objectMatrix = MainLoop.getObjectMatrix ();
			int nameListLength = objectMatrix.classNameList.size ();
			ArrayList<GameObject> objectList = null;
			objectList = objectMatrix.objectMatrix.get (this.matrixLocation [0]);		
			if (objectList != null) {
				int objectListLength = objectList.size ();
				for (int i = 0; i < objectListLength; i ++) {
					if (objectList.get (i) != null) {
						if (objectList.get (i).getHitbox () != null) {
							if (getHitbox ().checkOverlap (objectList.get (i).getHitbox ()) && i != this.matrixLocation [1]) {
								return true;
							}
						}
					}
				}
				return false;
			}
		}
		return false;
	}
	public double[] getCollisionCoordsSameType (double xTo, double yTo) {
		String objectType = this.getClass ().getSimpleName ();
		if (this.hitbox != null) {
			ObjectMatrix objectMatrix = MainLoop.getObjectMatrix ();
			int nameListLength = objectMatrix.classNameList.size ();
			ArrayList<GameObject> objectList = null;
			objectList = objectMatrix.objectMatrix.get (this.matrixLocation [0]);		
			if (objectList != null) {
				int objectListLength = objectList.size ();
				for (int i = 0; i < objectListLength; i ++) {
					if (objectList.get (i) != null) {
						if (objectList.get (i).getHitbox () != null) {
							double[] result = getHitbox ().checkVectorCollision (objectList.get (i).getHitbox (), xTo, yTo);
							if (result != null && i != this.matrixLocation [1]) {
								return result;
							}
						}
					}
				}
				return null;
			}
		}
		return null;
	}
	public GameObject getCollidingObjectSameType (double xTo, double yTo) {
		String objectType = this.getClass ().getSimpleName ();
		if (this.hitbox != null) {
			ObjectMatrix objectMatrix = MainLoop.getObjectMatrix ();
			int nameListLength = objectMatrix.classNameList.size ();
			ArrayList<GameObject> objectList = null;
			objectList = objectMatrix.objectMatrix.get (this.matrixLocation [0]);		
			if (objectList != null) {
				int objectListLength = objectList.size ();
				for (int i = 0; i < objectListLength; i ++) {
					if (objectList.get (i) != null) {
						if (objectList.get (i).getHitbox () != null) {
							if (getHitbox ().checkVectorCollision (objectList.get (i).getHitbox (), xTo, yTo) != null && i != this.matrixLocation [1]) {
								return objectList.get (i);
							}
						}
					}
				}
				return null;
			}
		}
		return null;
	}
	public boolean isCollidingSameType (double xTo, double yTo) {
		if (getCollisionCoordsSameType (xTo, yTo) != null) {
			return true;
		}
		return false;
	}
	public double getX () {
		return x;
	}
	public double getY () {
		return y;
	}
	public int getHitboxXOffset () {
		return hitboxXOffset;
	}
	public int getHitboxYOffset () {
		return hitboxYOffset;
	}
	public int[] getMatrixLocation () {
		return matrixLocation;
	}
	public void draw () {
		if (sprite != null) {	
			if (animationEnabled) {
				animationHandler.animate ((int) x - room.getViewX (), (int) y - room.getViewY (), flipHorizontal, flipVertical);
			} else {
				sprite.setFrame (0);
				sprite.draw ((int) x - room.getViewX (), (int) y - room.getViewY (), flipHorizontal, flipVertical);
			}
		}
	}
	public void frameEvent () {
		
	}
	public AnimationHandler getAnimationHandler () {
		return animationHandler;
	}
	public void setAnimationHandler (AnimationHandler animationHandler) {
		this.animationHandler = animationHandler;
	}
	public void setFlipHorizontal (boolean flip) {
		flipHorizontal = flip;
	}
	public void setFlipVertical (boolean flip) {
		flipVertical = flip;
	}
	public boolean getFlipHorizontal () {
		return flipHorizontal;
	}
	public boolean getFlipVertical () {
		return flipVertical;
	}
	public TileMap getTileMap() {
		return tileMap;
	}
	public void setTileMap(TileMap tileMap) {
		this.tileMap = tileMap;
	}
	public int getId () {
		return getTypeId (this.getClass ().getName ());
	}
	public Sprite getSprite () {
		return this.sprite;
	}
}