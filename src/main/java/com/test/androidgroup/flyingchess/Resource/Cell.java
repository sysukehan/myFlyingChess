package com.test.androidgroup.flyingchess.Resource;

import java.util.HashSet;

public class Cell {
	private int _index;
	private Color _color;
	private Cell _nextCell;
	private Cell _nextCell_2;
	private Cell _nextSameColorCell;
	private HashSet<Chess> _chessList;
	private boolean _is_Home;
	private boolean _is_Start;
	private boolean _is_Done;
	private boolean _is_Flyable;
	public Cell(int index) {
		_index = index;
	}
	public void init(
			Color color,
			Cell nextCell,
			Cell nextCell_2,
			Cell NextSameColorCell,
			boolean is_Home,
			boolean is_Start,
			boolean is_Done,
			boolean is_Flyable) {
		_color = color;
		_nextCell = nextCell;
		_nextCell_2 = nextCell_2;
		_nextSameColorCell = NextSameColorCell;
		_chessList = new HashSet<Chess>();
		_is_Home = is_Home;
		_is_Start = is_Start;
		_is_Done = is_Done;
		_is_Flyable = is_Flyable;
	}
	public int getIndex() {
		return _index;
	}
	public Color getColor() {
		return _color;
	}
	public Cell getNextCell() {
		return _nextCell;
	}
	public Cell getNextCell_2() {
		return _nextCell_2;
	}
	public Cell getNextSameColorCell() {
		return _nextSameColorCell;
	}
	public HashSet<Chess> getChessList() {
		return _chessList;
	}
	public boolean is_Home() {
		return _is_Home;
	}
	public boolean is_Start() {
		return _is_Start;
	}
	public boolean is_Done() {
		return _is_Done;
	}
	public boolean is_Flyable() {
		return _is_Flyable;
	}
}
