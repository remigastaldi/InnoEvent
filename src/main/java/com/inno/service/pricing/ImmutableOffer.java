/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI R??mi
 * -----
 * Last Modified: Wednesday, 28th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI R??mi
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.util.HashMap;

public interface ImmutableOffer {
  public enum ReductionType {
    PERCENTAGE, AMOUNT, NONE;
  }

  public String getName();

  public double getReduction();

  public String getDescription();

  public ReductionType getReductionType();

  public HashMap<String, ? extends ImmutableOfferCondition> getOfferConditions();
};