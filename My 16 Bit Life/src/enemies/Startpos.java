package enemies;

import main.GameObject;
import main.MainLoop;

public class Startpos extends GameObject {
	public Startpos () {
		
	}
	@Override
	public void declare (double x, double y) {
		MainLoop.getObjectMatrix ().get (this.getTypeId ("players.Jeffrey"), 0).setPosition (x, y);
	}
}
