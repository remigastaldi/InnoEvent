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


package com.inno.app.room;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Point2D;

public abstract class Section {
  private HashMap<String, Row> _rows = new HashMap<>();
  private ArrayList<Point2D> _points = new ArrayList<>();

  public Section() {
  }

  public void addPoint(Point2D point) {
    _points.add(point);
  }

  public ArrayList<Point2D> getPoints() {
    return _points;
  }
};