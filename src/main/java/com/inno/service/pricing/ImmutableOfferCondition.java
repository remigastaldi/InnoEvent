/*
 * File Created: Saturday, 27th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Monday, 10th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.util.ArrayList;

import com.inno.service.pricing.ImmutableOfferOperation.LogicalOperator;

public interface ImmutableOfferCondition {

  public String getName();

  public String getDescription();

  public LogicalOperator getLogicalOperator();

  public ArrayList<? extends ImmutableOfferOperation> getImmutableOfferOperations();

  public ImmutableOffer getParentOffer();
}