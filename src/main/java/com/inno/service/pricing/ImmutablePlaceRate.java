/*
 * File Created: Saturday, 27th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.util.ArrayList;

public interface ImmutablePlaceRate {

  public String getId();

  public double getPrice();

  public String getColor();

  public ArrayList<? extends ImmutableOffer> getImmutableOffers();
}