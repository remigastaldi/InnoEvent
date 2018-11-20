/*
 * File Created: Thursday, 8th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Tuesday, 20th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

public interface ImmutableRoom {

    public String getName();
    public double getHeight();
    public double getWidth();
    public ImmutableScene getImmutableScene();
    public Section getSectionById(String idSection);
    public ImmutableVitalSpace getImmutableVitalSpace();
}