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

public class PlaceRate {

  private double _price;
  private int _idSection;
  private String _idRow = new String();
  private int _idSeat;
  private ArrayList<String> _listOffers = new ArrayList<String>();

  public PlaceRate(int section, String row, int seat, double price, ArrayList<String> listOffers) {
  }

  public void setPrice(double price) {
  }

  public double getPrice() {
    return this._price;
  }

  public void addOffer(String offer) {

  }

  public void removeOffer(String offer) {

  }

  public ArrayList<String> getListOffers() {
    return this._listOffers;
  }
};