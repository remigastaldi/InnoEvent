/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.pricing;

import java.util.HashMap;

public class Pricing {
  private HashMap<Double, Color> _colors = new HashMap<>();
  private HashMap<Integer, HashMap<String, HashMap<Integer, Price>>> _prices = new HashMap<>(new HashMap<>(new HashMap<>()));
  private HashMap<String, Offer> _offers = new HashMap<>();

  public Pricing() {
  }
};