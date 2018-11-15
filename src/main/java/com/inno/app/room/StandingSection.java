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

public class StandingSection extends Section implements ImmutableStandingSection {

  private int _nbPeople;

  public StandingSection(String name, double elevation, int idSection, double[] points, int nbPeople) {
    super(name, elevation, idSection, points);
    this._nbPeople = nbPeople;
  }

  public void setNbPeople(int nbPeople) {
    this._nbPeople = nbPeople;
  }

  public int getNbPeople() {
    return this._nbPeople;
  }
}