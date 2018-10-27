/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 27th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.pricing;

import java.util.ArrayList;

public class Offer {
  public enum ReductionType {
    PERCENTAGE,
    AMOUNT,
    NONE;
  }
  private String _name = new String();
  private double _reduction;
  private String _description = new String();
  private ReductionType _reductionType = ReductionType.NONE;
  private ArrayList<OfferCondition> _listConditions = new ArrayList<>();

  public Offer(String name, double reduction, String description, ReductionType reductionType, ArrayList<OfferCondition> condition) {
  }

  public void setName(String name) {
  }

  public void setReduction(double reduction) {
  }

  public void setDescription(String description) {
  }

  public void setReductionType(ReductionType reductionType) {
  }

  public void addCondition(OfferCondition offerCondition) {
  }

  public void removeCondtion(OfferCondition offerCondition) {
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

  public ArrayList<OfferCondition> getOfferConditions() {
    return this._listConditions;
  }
};