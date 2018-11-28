/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 28th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.io.Serializable;

public class OfferOperation implements ImmutableOfferOperation, Serializable {

  protected String _value;
  protected RelationalOperator _relationalOperator;
  protected LogicalOperator _logicalOperator;

  public OfferOperation(String value, RelationalOperator relationalOperator, LogicalOperator logicalOperator) {
    this._value = value;
    this._relationalOperator = relationalOperator;
    this._logicalOperator = logicalOperator;
  }

  @Override
  public String getValue() {
    return _value;
  }

  @Override
  public RelationalOperator getRelationalOperator() {
    return _relationalOperator;
  }

  @Override
  public LogicalOperator getLogicalOperator() {
    return _logicalOperator;
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