package map;

public class TileAttributesList {
	private MapTile[] list;
	public TileAttributesList (MapTile[] list) {
		this.list = list;
	}
	public MapTile[] getTiles () {
		return list;
	}
	public MapTile getTile (String tileId) {
		for (int i = 0; i < list.length; i ++) {
			if (list [i].getName ().equals (tileId)) {
				return list [i];
			}
		}
		return null;
	}
}