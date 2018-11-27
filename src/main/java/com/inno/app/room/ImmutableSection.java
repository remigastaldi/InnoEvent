/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public interface ImmutableSection {

  public abstract String getIdSection();
  public abstract String getNameSection();
  public abstract double getElevation();
  public abstract double[] getPositions();
  public abstract double getRotation();
}