/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 14th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.pricing;

import java.util.HashMap;

public class Offer extends OfferData {
 

  public Offer(String name, String description, double reduction, ReductionType reductionType) {
    super(name, description, reduction, reductionType);
  }

  public void setName(String name) {
    this._name = name;
  }

  public void setReduction(double reduction) {
    this._reduction = reduction;
  }

  public void setDescription(String description) {
    this._description = description;
  }

  public void setReductionType(ReductionType reductionType) {
    this._reductionType = reductionType;
  }

  public void addCondition(OfferCondition offerCondition) {
    this._offerConditions.put(offerCondition.getName(), offerCondition);
  }

  public void removeCondition(String offerConditionName) {
    this._offerConditions.remove(offerConditionName);
  }

  public HashMap<String, OfferCondition> getOfferConditions() {
    return this._offerConditions;
  }
};