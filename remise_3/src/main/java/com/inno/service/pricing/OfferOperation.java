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

public class OfferOperation extends OfferOperationData {

  
  public OfferOperation(String value, RelationalOperator relationalOperator, LogicalOperator logicalOperator) {
    super(value, relationalOperator, logicalOperator);
  }

  public void setValue(String value) {
    this._value = value;
  }
  
  public void setRelationalOperator(RelationalOperator relationalOperator) {
    this._relationalOperator = relationalOperator;
  }
    
  public void setLogicalOperator(LogicalOperator logicalOperator) {
    this._logicalOperator = logicalOperator;
  }
};