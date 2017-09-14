package map;

public class TileData {
	//Container class for tile metadata
	private String name;
	private boolean isSolid;
	public TileData (String name, boolean isSolid) {
		this.name = name;
		this.isSolid = isSolid;
	}
	public String getName () {
		return name;
	}
	public boolean isSolid () {
		return isSolid;
	}
}