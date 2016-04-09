package com.test.androidgroup.flyingchess.Resource;

import java.util.Vector;

public class Player {
	private Color _color;
	private Vector<Chess> _chessList;
	public boolean is_Bot;
	public Player(Color color,Vector<Chess> chessList,boolean is_Bot) {
		_color = color;
		this.is_Bot = is_Bot;
		_chessList = chessList;
	}

	public Vector<Chess> getChessList() {
		return _chessList;
	}
	public Color getColor() {
		return _color;
	}
}
