package map;

public class TileData {
	//Container class for tile metadata
	public String name;
	public double frictionCoefficient;
	public boolean isSolid;
	public TileData (String name, String args) {
		//Name is in the format [tileset name].[position in tileset]
		this.name = name;
		this.isSolid = true;
		this.frictionCoefficient = 1.0;
		String[] argsArray = args.split (",");
		String arg;
		for (int i = 0; i < argsArray.length; i ++) {
			arg = argsArray [i].split (":")[1];
			switch (argsArray [i].split (":")[0]) {
				case "solid":
					if (arg.equals ("false")) {
						this.isSolid = false;
					}
					break;
				case "friction":
					this.frictionCoefficient = Double.parseDouble (arg);
					break;
			}
		}
	}
	public String getName () {
		return name;
	}
	public boolean isSolid () {
		return isSolid;
	}
}