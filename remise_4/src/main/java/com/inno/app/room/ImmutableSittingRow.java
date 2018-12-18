/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.app.room;

import java.util.ArrayList;

public interface ImmutableSittingRow {

    public ArrayList<? extends ImmutableSeat> getSeats();
    public String getIdRow();
    public double[] getPosStartRow();
    public double[] getPosEndRow();
}