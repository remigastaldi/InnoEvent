/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceRate implements ImmutablePlaceRate, Serializable {

  private static final long serialVersionUID = 1L;
  protected String _id;
  protected String _color;
  protected double _price;
  protected ArrayList<Offer> _offers = new ArrayList<Offer>();

  public PlaceRate(String id, String color, double price) {
    _id = id;
    _color = color;
    _price = price;
  }

  @Override
  public String getId() {
    return _id;
  }

  @Override
  public double getPrice() {
    return _price;
  }

  @Override
  public String getColor() {
    return _color;
  }

  @Override
  public ArrayList<? extends ImmutableOffer> getImmutableOffers() {
    return _offers;
  }

  public ArrayList<Offer> getOffers() {
    return _offers;
  }

  public void setPrice(double price) {
    this._price = price;
  }

  public void setColor(String color) {
    this._color = color;
  }

  public boolean addOffer(Offer offer) {
    return this._offers.add(offer);
  }

  public boolean removeOffer(Offer offer) {
    return this._offers.remove(offer);
  }
};