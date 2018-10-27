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

public class OfferCondition {
  private String _name = new String();
  private String _description = new String();
  private ArrayList<OfferOperation> _offerOperations = new ArrayList<>();

  public OfferCondition(String name, String description, ArrayList<OfferOperation> offerOperations) {

  }

  public void setName(String name) {
  }

  public void setDescription(String description) {
  }

  public void addOperation(OfferOperation offerOperation) {
  }

  public void removeOperation(OfferOperation offerOperation) {
  }

  public String getName() {
    return this._name;
  }

  public String getDescription() {
    return this._description;
  }

  public ArrayList<OfferOperation> getOfferOperations() {
    return this._offerOperations;
  }
};