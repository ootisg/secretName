package main;

import java.awt.Color;
import java.awt.Graphics;

import players.Jeffrey;

public class Gui extends GameObject {
	private static Jeffrey player = (Jeffrey) MainLoop.getObjectMatrix ().get (MainLoop.getObjectMatrix ().getTypeId ("players.Jeffrey"), 0);
	//public int totalHearts = 10;
	ListTbox mainMenu;
	ListTbox subMenu;
	Tbox splashBox;
	String[] weaponArray = new String[] {"REDBLACK PAINTBALL GUN", "STABBY KNIFE", "BACK"};
	String[] itemArray = new String[] {"PEACH JUICE", "HEALTH JUICE", "BACK"};
	public int focus = 0;
	public Gui () {
		this.declare (0, 0);
		new ListTbox (0, 0, new String[]{"NULL"}).forget ();
		this.splashBox = new Tbox (0, 0, 1, 1, "A");
		this.splashBox.close ();
	}
	@Override
	public void frameEvent () {
		if (keyPressed ('E')) {
			mainMenu = new ListTbox (0, 0, new String[]{"WEAPONS", "POPTART HOLDER", "EXIT"});
			MainLoop.pause ();
		}
	}
	public void pausedEvent () {
		int selectedVal = mainMenu.getSelected ();
		if (selectedVal == 0) {
			if (focus == 0) {
				focus = 1;
				subMenu = new ListTbox (16, 16, weaponArray);
			}
			int subVal = subMenu.getSelected ();
			if (subVal == 0 && focus == 1) {
				focus = 2;
				splashBox = new Tbox (32, 32, 18, 2, "THAT WEAPON IS    ALREADY EQUIPPED.");
				splashBox.setScrollRate (0);
			}
			if (subVal == 1 && focus == 1 && weaponArray [1].equals ("STABBY KNIFE")) {
				focus = 2;
				splashBox = new Tbox (32, 32, 16, 2, "THE STABBY KNIFERAN AWAY.");
				splashBox.setScrollRate (0);
			}
			if (subVal == 1 && focus == 2 && weaponArray [1].equals ("STABBY KNIFE") && !splashBox.declared) {
				weaponArray = new String[] {"REDBLACK PAINTBALL GUN", "BACK"};
				subMenu.forget ();
				subMenu = new ListTbox (16, 16, weaponArray);
			}
			if (subMenu.getSelected () == 2 && weaponArray [2].equals ("BACK") && !splashBox.declared) {
				subMenu.forget ();
				focus = 0;
				mainMenu.deselect ();
			}
			if (subMenu.getSelected () == 1 && weaponArray [1].equals ("BACK") && !splashBox.declared) {
				subMenu.forget ();
				focus = 0;
				mainMenu.deselect ();
			}
		}
		if (selectedVal == 1) {
			if (focus == 0) {
				focus = 1;
				subMenu = new ListTbox (16, 16, itemArray);
			}
			if (focus == 1 && subMenu.getSelectedValue ().equals ("PEACH JUICE")) {
				splashBox = new Tbox (32, 32, 12, 2, "JEFFREY FELTPEACHY.");
				splashBox.setScrollRate (0);
				focus = 2;
			}
			if (focus == 1 && subMenu.getSelectedValue ().equals ("HEALTH JUICE")) {
				splashBox = new Tbox (32, 32, 15, 2, "YOUR HEALTH WASRESTORED.");
				splashBox.setScrollRate (0);
				Jeffrey jeffrey = (Jeffrey)MainLoop.getObjectMatrix ().get (MainLoop.getObjectMatrix ().getTypeId ("players.Jeffrey"), 0);
				jeffrey.health = jeffrey.maxHealth;
				focus = 2;
			}
			if (focus == 2 && !splashBox.declared) {
				if (subMenu.getSelectedValue ().equals ("PEACH JUICE") || subMenu.getSelectedValue ().equals ("HEALTH JUICE")) {
					itemArray = removeElement (itemArray, subMenu.getSelected ());
					subMenu.forget ();
					subMenu = new ListTbox (16, 16, itemArray);
				}
			}
			if (subMenu.getSelectedValue () == "BACK") {
				focus = 0;
				subMenu.forget ();
				mainMenu.deselect ();
			}
		}
		if (focus == 2 && !splashBox.declared) {
			focus = 1;
			subMenu.deselect ();
		}
		if (focus == 1 && !subMenu.declared) {
			focus = 0;
		}
		if (selectedVal == 2) {
			mainMenu.forget ();
			//System.out.println(((ListTbox)MainLoop.getObjectMatrix ().get ((MainLoop.getObjectMatrix ().getTypeId("main.ListTbox")), 1)).options[0]);
			MainLoop.resume ();
		}
	}
	public String[] removeElement (String[] arr, int index) {
		String[] newArray = new String [arr.length - 1];
		int newIndex = 0;
		for (int i = 0; i < arr.length; i ++) {
			if (i != index) {
				newArray [newIndex] = arr [i];
				newIndex ++;
			}
		}
		return newArray;
	}
	@Override
	public void draw () {
		/*int numHearts = (int) Math.ceil((player.getHealth () / player.maxHealth) * totalHearts);
		for (int i = 0; i < numHearts - 1; i ++) {
			sprites.hearts.setFrame (0);
			sprites.hearts.draw (i * 16, 0);
		}
		int finalHeart;
		if (player.getHealth () - (player.maxHealth / totalHearts) * (numHearts - 1) >= .01) { 
			finalHeart = (int)((player.getHealth () - (player.maxHealth / totalHearts) * (numHearts - 1) - .01) / (player.maxHealth / (totalHearts * 4)));
		} else {
			finalHeart = -1;
		}
		if (finalHeart > 3) {
			finalHeart = 3;
		}
		if (finalHeart >= 0) {
			sprites.hearts.setFrame (3 - finalHeart);
			sprites.hearts.draw ((numHearts - 1) * 16, 0);
		}*/
		Graphics buffer = MainLoop.getWindow ().getBuffer ();
		buffer.setColor (new Color(0xFF0000));
		buffer.fillRect (0, 0, (int)(160 * (player.health / player.maxHealth)), 16);
		buffer.setColor (new Color(0x000000));
		buffer.drawRect (0, 0, 160, 16);
	}
	@Override
	public void forget () {
		
	}
}