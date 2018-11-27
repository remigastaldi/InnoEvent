/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Tuesday, 27th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.util.ArrayList;

public interface ImmutableSittingSection extends ImmutableSection {
    public boolean getAutoDistribution();
    public ArrayList<? extends ImmutableSittingRow> getImmutableSittingRows();
    public ImmutableVitalSpace getImmutableVitalSpace();
    public boolean isRectangle();
}
