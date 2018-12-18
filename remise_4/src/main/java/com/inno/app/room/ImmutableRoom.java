/*
 * File Created: Thursday, 8th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.util.HashMap;

public interface ImmutableRoom {

    public String getName();
    public double getHeight();
    public double getWidth();
    public ImmutableScene getImmutableScene();
    public ImmutableSection getSectionById(String idSection);
    public ImmutableSittingSection standingToSittingSection(String idSection);
    public ImmutableStandingSection sittingToStandingSection(String idSection);
    public ImmutableVitalSpace getImmutableVitalSpace();
    public HashMap<String, ? extends ImmutableSittingSection> getImmutableSittingSections();
    public HashMap<String, ? extends ImmutableStandingSection> getImmutableStandingSections();
}