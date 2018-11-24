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

import java.util.ArrayList;

public interface ImmutableSittingSection extends ImmutableSection {
    public boolean isAutoDistribution();
    public ArrayList<? extends ImmutableSittingRow> getImmutableSittingRow();
    public ImmutableVitalSpace getImmutableVitalSpace();
}
