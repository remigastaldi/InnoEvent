/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.pricing;

import java.util.ArrayList;

import com.inno.service.pricing.OfferOperationData.LogicalOperator;

public class OfferCondition extends OfferConditionData {
 

  public OfferCondition(String name, String description, LogicalOperator logicalOperator) {
    super(name, description, logicalOperator);
  }

  public void setName(String name) {
    this._name = name;
  }

  public void setDescription(String description) {
    this._description = description;
  }

  public void setLogicalOperator(LogicalOperator logicalOperator) {
    this._logicalOperator = logicalOperator;
  }

  public void addOperation(OfferOperation offerOperation) {
    this._offerOperations.add(offerOperation);
  }

  public void removeOperation(OfferOperation offerOperation) {
    this._offerOperations.remove(offerOperation);
  }

  public ArrayList<OfferOperation> getOfferOperations() {
    return this._offerOperations;
}

 
};