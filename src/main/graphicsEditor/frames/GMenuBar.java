package main.graphicsEditor.frames;

import main.graphicsEditor.menus.GFileMenu;

import javax.swing.*;

public class GMenuBar extends JMenuBar {
	// components
	private GFileMenu fileMenu;
	// associations

	public GMenuBar() {
		this.fileMenu = new GFileMenu();
		this.add(this.fileMenu);
	}
}
