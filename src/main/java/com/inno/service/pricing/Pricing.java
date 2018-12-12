/*
 * File Created: Saturday, 27th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.service.pricing;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.inno.service.pricing.ImmutableOffer.ReductionType;
import com.inno.service.pricing.ImmutableOfferOperation.LogicalOperator;
import com.inno.service.pricing.ImmutableOfferOperation.RelationalOperator;

public class Pricing implements Serializable {

  private static final long serialVersionUID = 1L;
  private HashMap<String, PlaceRate> _places = new HashMap<String, PlaceRate>();
  private HashMap<String, Offer> _offers = new HashMap<String, Offer>();

  public Pricing() {
  }

  private <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
    if (c != null && string != null) {
      try {
        return Enum.valueOf(c, string.trim().toUpperCase());
      } catch (IllegalArgumentException ex) {
        System.out.println("Cannot convert ==> " + string + " to " + c.getName());
      }
    }
    return null;
  }

  public HashMap<String, PlaceRate> getPlaces() {
    return this._places;
  }

  public HashMap<String, ? extends ImmutablePlaceRate> getPlaces(String search) {
    HashMap<String, PlaceRate> places = new HashMap<>();

    for (Map.Entry<String, PlaceRate> entry : _places.entrySet()) {
      String key = entry.getKey();
      PlaceRate place = entry.getValue();
      if (key.startsWith(search)) {
        places.put(key, place);
      }
    }
    return places;
  }

  public HashMap<String, ? extends ImmutableOffer> getOffers() {
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
  public ImmutablePlaceRate createPlace(String id, String color, double price) {
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
   * Get ImmutablePlaceRate by id
   * 
   * @param id
   * @return
   */
  public ImmutablePlaceRate getPlaceRate(String id) {
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
    Offer offer = this._offers.get(offerName);

    if (place == null) {
      return;
    }
    place.addOffer(offer);
  }

  /**
   * Remove PlaceRate offer
   * 
   * @param id
   * @param offerName
   */
  public void removePlaceRateOffer(String id, String offerName) {
    PlaceRate place = this._places.get(id);
    Offer offer = this._offers.get(offerName);

    if (place == null) {
      return;
    }
    place.removeOffer(offer);
  }

  private String getNextOfferName() {
    String name = "Untitled";
    int i = 1;
    while (true) {
      boolean find = false;
      for (Map.Entry<String, ? extends ImmutableOffer> entry : _offers.entrySet()) {
        String offerName = entry.getKey();
        if (offerName.equals(name + i) == true) {
          find = true;
        }
      }
      if (!find) {
        return name + i;
      } else {
        i = i + 1;
      }
    }
  }

  private String getNextOfferConditionName(String offerName) {
    String name = "Untitled";
    int i = 1;
    while (true) {
      boolean find = false;
      for (Map.Entry<String, ? extends ImmutableOfferCondition> entry : _offers.get(offerName).getOfferConditions()
          .entrySet()) {
        String offerConditionName = entry.getKey();
        if (offerConditionName.equals(name + i) == true) {
          find = true;
        }
      }
      if (!find) {
        return name + i;
      } else {
        i = i + 1;
      }
    }
  }

  /**
   * Create Offer with a name, description, reduction and reductionType
   * 
   * @param name
   * @param description
   * @param reduction
   * @param reductionType
   * @return
   */
  public ImmutableOffer createOffer(String name, String description, double reduction, String reductionType) {
    ReductionType reductionTypeEnum = getEnumFromString(ReductionType.class, reductionType);

    if (name == null) {
      name = getNextOfferName();
    }

    Offer offer = new Offer(name, description, reduction, reductionTypeEnum);

    this._offers.put(name, offer);
    return offer;
  }

  /**
   * Remove Offer by name
   * 
   * @param name
   */
  public void deleteOffer(String name) {
    this._offers.remove(name);
  }

  /**
   * Get ImmutableOffer by name
   * 
   * @param name
   * @return
   */
  public ImmutableOffer getOffer(String name) {
    Offer offer = this._offers.get(name);

    if (offer == null) {
      return null;
    }
    return offer;
  }

  public ImmutableOffer setOfferName(String name, String newName) {
    Offer offer = this._offers.remove(name);

    if (offer == null) {
      return null;
    }

    offer.setName(newName);

    this._offers.put(newName, offer);
    return offer;
  }

  public void setOfferDescription(String name, String description) {
    Offer offer = this._offers.get(name);

    if (offer == null) {
      return;
    }
    offer.setDescription(description);
  }

  public void setOfferReduction(String name, double reduction) {
    Offer offer = this._offers.get(name);

    if (offer == null) {
      return;
    }
    offer.setReduction(reduction);
  }

  public void setOfferReductionType(String name, ReductionType reductionType) {
    Offer offer = this._offers.get(name);

    if (offer == null) {
      return;
    }
    offer.setReductionType(reductionType);
  }

  // TODO: Changes alls enums to string
  public ImmutableOfferCondition createOfferCondition(String offerName, String offerConditionName, String description,
      String logicalOperator) {
    LogicalOperator logicalOperatorEnum = getEnumFromString(LogicalOperator.class, logicalOperator);
    Offer offer = this._offers.get(offerName);

    if (offer == null) {
      return null;
    }

    if (offerConditionName == null) {
      offerConditionName = getNextOfferConditionName(offer.getName());
    }

    OfferCondition offerCondition = new OfferCondition(offerConditionName, description, logicalOperatorEnum);
    offer.addCondition(offerCondition);
    return offerCondition;
  }

  // TODO: Changes alls enums to string
  public void deleteOfferCondition(String offerName, String offerConditionName) {
    Offer offer = this._offers.get(offerName);

    if (offer == null) {
      return;
    }
    offer.removeCondition(offerConditionName);
  }

  public HashMap<String, ? extends ImmutableOfferCondition> getOfferConditions(String name) {
    Offer offer = this._offers.get(name);

    if (offer == null) {
      return null;
    }
    return offer.getOfferConditions();
  }

  public void setOfferConditionName(String offerName, String offerConditionName, String nName) {
    Offer offer = this._offers.get(offerName);

    if (offer == null) {
      return;
    }

    offer.setOfferConditionName(offerConditionName, nName);
  }

  public void setOfferConditionDescription(String offerName, String offerConditionName, String description) {
    Offer offer = this._offers.get(offerName);

    if (offer == null) {
      return;
    }
    OfferCondition offerCondition = offer.getOfferConditions().get(offerConditionName);
    if (offerCondition == null) {
      return;
    }
    offerCondition.setDescription(description);
  }

  public void setOfferConditionLogicalOperator(String offerName, String offerConditionName,
      LogicalOperator logicalOperator) {
    Offer offer = this._offers.get(offerName);

    if (offer == null) {
      return;
    }
    OfferCondition offerCondition = offer.getOfferConditions().get(offerConditionName);
    if (offerCondition == null) {
      return;
    }
    offerCondition.setLogicalOperator(logicalOperator);
  }

  private OfferOperation getOfferOperation(String offerName, String offerConditionName, int index) {
    Offer offer = this._offers.get(offerName);

    if (offer == null) {
      return null;
    }
    OfferCondition offerCondition = offer.getOfferConditions().get(offerConditionName);
    if (offerCondition == null) {
      return null;
    }
    return offerCondition.getOfferOperations().get(index);
  }

  private OfferCondition getOfferCondition(String offerName, String offerConditionName) {
    Offer offer = this._offers.get(offerName);

    if (offer == null) {
      return null;
    }
    return offer.getOfferConditions().get(offerConditionName);
  }

  public ImmutableOfferCondition getImmutableOfferCondition(String offerName, String offerConditionName) {
    return this.getOfferCondition(offerName, offerConditionName);
  }

  public ImmutableOfferOperation createOfferConditionOperation(String offerName, String offerConditionName,
      String value, String relationalOperator, String logicalOperator) {

    LogicalOperator logicalOperatorEnum = getEnumFromString(LogicalOperator.class, logicalOperator);
    RelationalOperator relationalOperatorEnum = getEnumFromString(RelationalOperator.class, relationalOperator);

    OfferCondition offerCondition = this.getOfferCondition(offerName, offerConditionName);
    if (offerCondition == null) {
      return null;
    }
    OfferOperation offerOperation = new OfferOperation(value, relationalOperatorEnum, logicalOperatorEnum);
    offerCondition.addOperation(offerOperation);
    return offerOperation;
  }

  public void removeOfferConditionOperation(String offerName, String offerConditionName, int index) {
    OfferCondition offerCondition = this.getOfferCondition(offerName, offerConditionName);

    if (offerCondition == null) {
      return;
    }
    offerCondition.getOfferOperations().remove(index);
  }

  public ImmutableOfferOperation getOfferConditionOperations(String offerName, String offerConditionName, int index) {
    return this.getOfferOperation(offerName, offerConditionName, index);
  }

  public void setOfferConditionOperationValue(String offerName, String offerConditionName, int index, String value) {
    OfferOperation offerOperation = this.getOfferOperation(offerName, offerConditionName, index);
    offerOperation.setValue(value);
  }

  public void setOfferConditionOperationLogicalOperator(String offerName, String offerConditionName, int index,
      String logicalOperator) {
    LogicalOperator logicalOperatorEnum = getEnumFromString(LogicalOperator.class, logicalOperator);


    OfferOperation offerOperation = this.getOfferOperation(offerName, offerConditionName, index);
    offerOperation.setLogicalOperator(logicalOperatorEnum);
  }

  public void setOfferConditionOperationRelationalOperator(String offerName, String offerConditionName, int index,
      String relationalOperator) {
    RelationalOperator relationalOperatorEnum = getEnumFromString(RelationalOperator.class, relationalOperator);

    OfferOperation offerOperation = this.getOfferOperation(offerName, offerConditionName, index);
    offerOperation.setRelationalOperator(relationalOperatorEnum);
  }

  private String[] getEnumToStringArray(Class<? extends Enum<?>> e) {
    return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
  }

  public String[] getReductionTypePossibilities() {
    return this.getEnumToStringArray(ReductionType.class);
  }

  public String[] getLogicalOperatorTypePossibilities() {
    return this.getEnumToStringArray(LogicalOperator.class);
  }

  public String[] getRelationalOperatorTypePossibilities() {
    return this.getEnumToStringArray(RelationalOperator.class);
  }

};