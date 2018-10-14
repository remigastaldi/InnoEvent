/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 14th October 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.pricing;

import java.util.HashMap;
import java.util.ArrayList;

import javafx.scene.paint.Color;

import com.inno.pricing.Offer.ReductionType;

public class Pricing {
  public enum AttributionType {
    SECTION,
    ROW,
    SEAT;
  }
  private HashMap<Double, Color> _colors = new HashMap<>();
  private HashMap<Integer, HashMap<String, HashMap<Integer, Price>>> _prices = new HashMap<>(new HashMap<>(new HashMap<>()));
  private HashMap<String, Offer> _offers = new HashMap<>();

  public Pricing() {
  }

  public void setColor(double price, Color color) {
  }

  public Color getColor(double price) {
    return null;
  }

  public void addPrice(int section, String row, int seat, int price, ArrayList<String> listOffers) {
  }

  public void addOffer(double reduction, String description, ReductionType reductionType, String name, ArrayList<OfferCondition> listOfferCondition) {
  }

  public void calculatePrice(int minPrice, int maxPrice, int total, AttributionType attributionType) {
  }
};