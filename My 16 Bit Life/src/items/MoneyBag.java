package items;

public class MoneyBag extends Item {
	
	public void MoneyBag (int amountToPickUp) {
		this.setName(Integer.toString(amountToPickUp) + " Money");
	}
	
}
