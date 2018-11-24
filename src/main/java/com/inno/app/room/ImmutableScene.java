/*
 * File Created: Thursday, 8th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public interface ImmutableScene {

    public double getWidth();
    public double getHeight();
    public double[] getPositions();
    public double getRotation();
    public double[] getCenter();
}