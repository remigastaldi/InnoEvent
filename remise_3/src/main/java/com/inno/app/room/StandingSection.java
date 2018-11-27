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

public class StandingSection extends Section implements ImmutableSection, ImmutableStandingSection {

  private static final long serialVersionUID = 1L;
  private int _nbPeople;

  public StandingSection(String nameSection, String idSection, double[] positions, int nbPeople, double rotation) {
    super(nameSection, idSection, positions, rotation);
    this._nbPeople = nbPeople;
  }

  public void setNbPeople(int nbPeople) {
    this._nbPeople = nbPeople;
  }

  public int getNbPeople() {
    return this._nbPeople;
  }
}