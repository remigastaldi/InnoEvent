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

public class StandingSection extends Section implements ImmutableSection, ImmutableStandingSection {

  private int _nbPeople;

  public StandingSection(String idSection, double elevation,double[] positions, int nbPeople, double rotation) {
    super(idSection, elevation, positions, rotation);
    this._nbPeople = nbPeople;
  }

  public void setNbPeople(int nbPeople) {
    this._nbPeople = nbPeople;
  }

  public int getNbPeople() {
    return this._nbPeople;
  }
}