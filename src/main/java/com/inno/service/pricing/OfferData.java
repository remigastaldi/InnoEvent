/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 13th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.pricing;

import java.util.ArrayList;

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
  protected ArrayList<OfferCondition> _offerConditions = new ArrayList<OfferCondition>();

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

  public ArrayList<? extends OfferConditionData> getOfferConditions() {
    return this._offerConditions;
  }
};