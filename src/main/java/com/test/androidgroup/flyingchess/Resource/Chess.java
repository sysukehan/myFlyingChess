package com.test.androidgroup.flyingchess.Resource;;

import com.test.androidgroup.flyingchess.Resource.*;

public class Chess {
	private Color _color;
	private Cell _cell;
	private int _id;
	public boolean _jumped;
	public boolean _flew;
	public boolean is_goingBack;
	public boolean is_Completed;
    public int _rid;
	public Chess(Color color, Cell cell, int rid) {
		_color = color;
		_cell = cell;
        _rid = rid;
		_jumped = false;
		_flew = false;
		is_goingBack = false;
		is_Completed = false;
	}
	public Color getColor() {
		return _color;
	}
	public Cell getCell() {
		return _cell;
	}
	public void setCell(Cell cell) {
		_cell = cell;
	}
    public int getId(){ return _id; }
    public void setId(int id){
        _id = id;
    }

}
