/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI R??mi
 * -----
 * Last Modified: Wednesday, 14th November 2018
 * Modified By: HUBERT L??o
 * -----
 * Copyright - 2018 GASTALDI R??mi
 * <<licensetext>>
 */


package com.inno.service.pricing;

import java.util.HashMap;

public class OfferData {
  public enum ReductionType {
    PERCENTAGE,
    AMOUNT,
    NONE;
  }
  protected String _name;
  protected double _reduction;
  protected String _description;
  protected ReductionType _reductionType = ReductionType.NONE;
  protected HashMap<String, OfferCondition> _offerConditions = new HashMap<String, OfferCondition>();

  public OfferData(String name, String description,  double reduction,  ReductionType reductionType) {
    this._name = name;
    this._description = description;
    this._reduction = reduction;
    this._reductionType = reductionType;
  }

  public String getName() {
    return this._name;
  }

  public double getReduction() {
    return this._reduction;
  }

  public String getDescription() {
    return this._description;
  }

  public ReductionType getReductionType() {
    return this._reductionType;
  }

  public HashMap<String, ? extends OfferConditionData> getOfferConditions() {
    return this._offerConditions;
  }
};