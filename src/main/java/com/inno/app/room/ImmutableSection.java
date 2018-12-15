/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Saturday, 15th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public interface ImmutableSection {

  public abstract String getId();
  public abstract String getNameSection();
  public abstract double getElevation();
  public abstract double[] getPositions();
  public abstract double getRotation();
  public abstract double getUserRotation();
  public ImmutableSection clone() throws CloneNotSupportedException;
  public abstract boolean isRectangle();
  public abstract boolean isStanding();
}