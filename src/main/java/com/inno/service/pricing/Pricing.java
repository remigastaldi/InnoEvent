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

import java.util.HashMap;

import com.inno.service.pricing.OfferData.ReductionType;


public class Pricing {

  private HashMap<String, PlaceRate> _places = new HashMap<String, PlaceRate>();
  private HashMap<String, Offer> _offers = new HashMap<String, Offer>();

  public Pricing() {
  }

  public HashMap<String, PlaceRate> getPlaces() {
    return this._places;
  }

  public HashMap<String, Offer> getOffers() {
    return this._offers;
  }
  /**
   * Create PlaceRate with an id, color and price
   * 
   * @param id
   * @param color
   * @param price
   * @return
   */
  public PlaceRateData createPlace(String id, String color, Double price) {
    PlaceRate place = new PlaceRate(id, color, price);

    this._places.put(id, place);
    return place;
  }

  /**
   * Remove PlaceRate by id 
   * 
   * @param id
   */
  public void deletePlaceRate(String id) {
    this._places.remove(id);
  }

  /**
   * Get PlaceRateData by id
   * @param id
   * @return
   */
  public PlaceRateData getPlaceRate(String id) {
    return this._places.get(id);
  }

  /**
   * Set PlaceRate price by id
   * 
   * @param id
   * @param price
   */
  public void setPlaceRatePrice(String id, double price) {
    PlaceRate place = this._places.get(id);

    if (place == null) {
      return;
    }
    place.setPrice(price);
  }
  
  /**
   * Set PlaceRate color by id
   * 
   * @param id
   * @param color
   */
  public void setPlaceRateColor(String id, String color) {
    PlaceRate place = this._places.get(id);

    if (place == null) {
      return;
    }
    place.setColor(color);
  }

  /**
   * Add PlaceRate offer
   * 
   * @param id
   * @param offerName
   */
  public void addPlaceRateOffer(String id, String offerName) {
    PlaceRate place = this._places.get(id);

    if (place == null) {
      return;
    }

    place.addOffer(offerName);
  }

  /**
   * Remove PlaceRate offer
   * 
   * @param id
   * @param offerName
   */
  public void removePlaceRateOffer(String id, String offerName) {
    PlaceRate place = this._places.get(id);

    if (place == null) {
      return;
    }

    place.removeOffer(offerName);
  }

  public OfferData createOffer(String name, String description, double reduction, ReductionType reductionType) {
    Offer offer = new Offer(name, description, reduction, reductionType);
    
    this._offers.put(name, offer);
    
    return offer;
  }

};