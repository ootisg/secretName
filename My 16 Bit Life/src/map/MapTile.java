package map;

public class MapTile {
	//Container class for tile metadata
	private String name;
	private boolean isSolid;
	public MapTile (String name, boolean isSolid) {
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