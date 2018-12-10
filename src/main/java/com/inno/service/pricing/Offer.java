/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 10th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.io.Serializable;
import java.util.HashMap;

public class Offer implements ImmutableOffer, Serializable {

  private static final long serialVersionUID = 1L;
  protected String _name;
  protected double _reduction;
  protected String _description;
  protected ReductionType _reductionType = ReductionType.NONE;
  protected HashMap<String, OfferCondition> _offerConditions = new HashMap<String, OfferCondition>();

  public Offer(String name, String description, double reduction, ReductionType reductionType) {
    this._name = name;
    this._description = description;
    this._reduction = reduction;
    this._reductionType = reductionType;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public double getReduction() {
    return _reduction;
  }

  @Override
  public String getDescription() {
    return _description;
  }

  @Override
  public ReductionType getReductionType() {
    return _reductionType;
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
    offerCondition.setParentOffer(this);
    this._offerConditions.put(offerCondition.getName(), offerCondition);
  }

  public void removeCondition(String offerConditionName) {
    this._offerConditions.remove(offerConditionName);
  }

  public HashMap<String, OfferCondition> getOfferConditions() {
    return this._offerConditions;
  }

  public void setOfferConditionName(String offerConditionName, String nName) {
    OfferCondition offerCondition = _offerConditions.remove(offerConditionName);

    if (offerCondition == null) {
      return;
    }

    offerCondition.setName(nName);

    _offerConditions.put(nName, offerCondition);
  }
};