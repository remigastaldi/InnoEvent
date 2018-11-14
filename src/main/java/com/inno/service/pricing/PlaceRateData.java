/*
 * File Created: Saturday, 27th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Tuesday, 13th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.util.ArrayList;

public class PlaceRateData {
    
    protected String _id;
    protected String _color;
    protected double _price; 
    protected ArrayList<String> _offers = new ArrayList<String>();
    
    public PlaceRateData(String id, String color, double price) {
        this._id = id;
        this._color = color;
        this._price = price;
    }
    public String getId() {
        return this._id;
    }

    public double getPrice() {
        return this._price;
    }

    public String getColor() {
        return this._color;
    }
  
    public ArrayList<String> getOffers() {
      return this._offers;
    } 
}