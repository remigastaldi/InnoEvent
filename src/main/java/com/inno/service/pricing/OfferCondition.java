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
import java.util.ArrayList;

import com.inno.service.pricing.ImmutableOfferOperation.LogicalOperator;


public class OfferCondition implements ImmutableOfferCondition, Serializable {

  private static final long serialVersionUID = 1L;
  protected String _name = new String();
  protected String _description = new String();
  protected LogicalOperator _logicalOperator;
  protected ArrayList<OfferOperation> _offerOperations = new ArrayList<>();

  public OfferCondition(String name, String description, LogicalOperator logicalOperator) {
    this._name = name;
    this._description = description;
    this._logicalOperator = logicalOperator;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public String getDescription() {
    return _description;
  }

  @Override
  public LogicalOperator getLogicalOperator() {
    return _logicalOperator;
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