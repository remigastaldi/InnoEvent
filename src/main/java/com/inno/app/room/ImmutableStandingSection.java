/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Friday, 16th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public interface ImmutableStandingSection {
    
    public String getIdSection();
    public double getElevation();
    public double[] getPositions();
    public int getNbPeople();
}