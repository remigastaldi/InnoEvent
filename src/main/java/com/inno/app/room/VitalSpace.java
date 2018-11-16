/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public class VitalSpace implements ImmutableVitalSpace {

    private double _height;
    private double _width;

    public VitalSpace(double height, double width) {
        this._height = height;
        this._width = width;
    }

    public void setHeight(double height) {
        this._height = height;
    }

    public void setWidth(double width) {
        this._width = width;
    }

    public double getHeight() {
        return this._height;
    }

    public double getWidth() {
        return this._width;
    }
}