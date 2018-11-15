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

import java.util.ArrayList;

public interface ImmutableSittingSection {

    public boolean isAutoDistribution();
    public ArrayList<? extends ImmutableSittingRow> getImmutableSittingRow();
    public VitalSpace getVitalSpace();
}
