package items;

import main.GameObject;

public class Item extends GameObject {
	String name;
	String entry;
	
	public void Item () {
	}
	public void setName (String newName) {
		name = newName;
	}
	public void setEntry (String newEntry) {
		entry = newEntry;
	}
	public void dropItem() {
		boolean onFloor = false;
		if (room.isColliding(this.getHitbox())){
			this.setY(getY() - 1);
			if (!(room.isColliding(this.getHitbox()))){
				this.setY(getY() + 1);
					onFloor = true;
				}
			}
	}
	
}
