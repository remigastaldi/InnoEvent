/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Monday, 10th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.io.Serializable;

public class VitalSpace implements ImmutableVitalSpace, Serializable {

    private static final long serialVersionUID = 1L;
    private double _height;
    private double _width;

    public VitalSpace(double width, double height) {
        this._width = width;
        this._height = height;
    }

    public void setWidth(double width) {
        this._width = width;
    }

    public void setHeight(double height) {
        this._height = height;
    }

    public double getWidth() {
        return this._width;
    }

    public double getHeight() {
        return this._height;
    }
}