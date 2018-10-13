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


package com.inno.room;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Section {
  private HashMap<String, Row> _rows = new HashMap<>();
  private ArrayList<Point> _points = new ArrayList<>();

  public Section() {
  }
};