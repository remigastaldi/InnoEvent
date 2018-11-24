/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public interface ImmutableSection {

  public abstract String getIdSection();
  public abstract double getElevation();
  public abstract double[] getPositions();
  public abstract double getRotation();
}