package com.inno.service;

public class Point {

    private double _x,_y;

    public Point(double x, double y)
    {
        this._x = x;
        this._y = y;
    }

    public double get_x() {
        return _x;
    }

    public double get_y() {
        return _y;
    }

    public void set_x(double _x) {
        this._x = _x;
    }

    public void set_y(double _y) {
        this._y = _y;
    }
}
