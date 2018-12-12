/*
 * File Created: Tuesday, 9th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inno.app.InnoSave;
import com.inno.app.room.*;
import com.inno.service.Point;
import com.inno.service.SettingsService;
import com.inno.service.Utils;
import com.inno.service.pricing.ImmutableOffer;
import com.inno.service.pricing.ImmutableOfferCondition;
import com.inno.service.pricing.ImmutableOfferOperation;
import com.inno.service.pricing.PlaceRate;
import com.inno.service.pricing.ImmutablePlaceRate;
import com.inno.service.pricing.Pricing;

public class Core {

  private static Core _instance = null;

  // Services
  private InnoSave _saveService = new InnoSave();
  private Pricing _pricing = new Pricing();
  private SettingsService _settings = new SettingsService();

  private ArrayList<String> _recentPaths = new ArrayList<>();

  // Inno Class
  private Room _room = null;

  private Core() {
    if (_settings.has("recent_paths")) {
      _recentPaths = (ArrayList<String>) _settings.get("recent_paths");
    }
  }

  public static Core get() {
    if (_instance == null) {
      _instance = new Core();
    }

    return _instance;
  }

  // Room methods
  public void createRoom(String name, double width, double height, double widthVitalSpace, double heightVitalSpace) {
    this._room = new Room(name, width, height, widthVitalSpace, heightVitalSpace);
  }

  public ImmutableRoom getImmutableRoom() {
    return this._room;
  }

  public void setRoomName(String name) {
    this._room.setName(name);
  }

  
  public void setRoomWidth(double width) {
    this._room.setWidth(width);
  }

  public void setRoomHeight(double height) {
    this._room.setHeight(height);
  }

  public void setRoomVitalSpace(double width, double height) {
    _room.setVitalSpace(width, height);
  }

  // Scene methods
  public ImmutableScene createScene(double width, double height, double[] positions) {
    return this._room.createScene(width, height, positions);
  }

  public void deleteScene() {
    this._room.deleteScene();
  }

  public void setSceneWidth(double width) {
    this._room.setSceneWidth(width);
  }

  public void setSceneHeight(double height) {
    this._room.setSceneHeight(height);
  }

  public void setScenePositions(double[] positions) {
    this._room.setScenePositions(positions);
  }

  public void setSceneRotation(double rotation) {
    this._room.setSceneRotation(rotation);
  }

  // Section methods
  public void setSectionName(String idSection, String name) {
    this._room.setSectionName(idSection, name);
  }

  public void setSectionElevation(String idSection, double elevation) {
    this._room.setSectionElevation(idSection, elevation);
  }

  public void updateSectionPositions(String idSection, double[] positions, boolean rectangular) {
    if (rectangular) {
      Point pt = new Point(getImmutableRoom().getImmutableScene().getCenter()[0],
          getImmutableRoom().getImmutableScene().getCenter()[1]);
      double rotation = Utils.calculateRectangleRotation(pt, positions);
      if (rotation != rotation)
        rotation = 0.0;
      _room.setSectionRotation(idSection, rotation);
    }

    this._room.updateSectionPositions(idSection, positions);
  }

  public void deleteSection(String idSection) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(idSection);

    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.deletePlaceRate(key);
    }
    this._room.deleteSection(idSection);
  }

  public void setSectionUserRotation(String idSection, double rotation) {
    // this._room.setSectionRotation(idSection, rotation);
    _room.setSectionUserRotation(idSection, rotation);
  }

  public ImmutableStandingSection sittingToStandingSection(String idSection) {
    return this._room.sittingToStandingSection(idSection);
  }

  public ImmutableSittingSection standingToSittingSection(String idSection) {
    return this._room.standingToSittingSection(idSection);
  }

  // standingSection Methods
  public ImmutableStandingSection createStandingSection(int nbPeople, double[] positions, double rotation) {
    return this._room.createStandingSection(nbPeople, positions, rotation);
  }

  public void setStandingNbPeople(String idSection, int nbPeople) {
    this._room.setStandingNbPeople(idSection, nbPeople);
  }

  // sittingSection Methods
  public ImmutableSittingSection createSittingSection(double[] positions, double rotation, boolean isRectangle) {
    double newRotation = 0d;
    if (isRectangle) {
      Point pt = new Point(getImmutableRoom().getImmutableScene().getCenter()[0],
          getImmutableRoom().getImmutableScene().getCenter()[1]);
      // double[] newPos = Utils.rotateRectangle(pt, positions);
      newRotation = Utils.calculateRectangleRotation(pt, positions);
    }
    ImmutableSittingSection section = _room.createSittingSection(positions, newRotation, isRectangle);

    createPlace(section.getIdSection(), "#6378bf");

    return section;
  }

  public void setSectionPrice(String idSection, double price, String color) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(idSection + "|");
    _pricing.setPlaceRatePrice(idSection, price);
    if (color != null) {
      _pricing.setPlaceRateColor(idSection, color);
    }
    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.setPlaceRatePrice(key, price);
      if (color != null) {
        _pricing.setPlaceRateColor(key, color);
      }
    }
  }

  public void setSectionPrice(String idSection, double price) {
    setSectionPrice(idSection, price, null);
  }

  public ImmutablePlaceRate getSectionPrice(String idSection) {
    return _pricing.getPlaceRate(idSection);
  }

  public void setRowPrice(String idSection, String idRow, double price, String color) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(idSection + "|" + idRow + "|");
    _pricing.setPlaceRatePrice(idSection, -1);
    _pricing.setPlaceRateColor(idSection, "#6378bf");

    _pricing.setPlaceRatePrice(idSection + "|" + idRow, price);
    if (color != null) {
      _pricing.setPlaceRateColor(idSection + "|" + idRow, color);
    }
    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.setPlaceRatePrice(key, price);
      if (color != null) {
        _pricing.setPlaceRateColor(key, color);
      }
    }
  }

  public void setRowPrice(String idSection, String idRow, double price) {
    setRowPrice(idSection, idRow, price, null);
  }

  public ImmutablePlaceRate getRowPrice(String idSection, String idRow) {
    return _pricing.getPlaceRate(idSection + "|" + idRow);
  }

  public void setSeatPrice(String idSection, String idRow, String idSeat, double price, String color) {
    _pricing.setPlaceRatePrice(idSection, -1);
    _pricing.setPlaceRateColor(idSection, "#6378bf");
    _pricing.setPlaceRatePrice(idSection + "|" + idRow, -1);
    _pricing.setPlaceRateColor(idSection + "|" + idRow, "#7289DA");
    _pricing.setPlaceRatePrice(idSection + "|" + idRow + "|" + idSeat, price);
    if (color != null) {
      _pricing.setPlaceRateColor(idSection + "|" + idRow + "|" + idSeat, color);
    }
  }

  public void setSeatPrice(String idSection, String idRow, String idSeat, double price) {
    setSeatPrice(idSection, idRow, idSeat, price, null);
  }

  public ImmutablePlaceRate getSeatPrice(String idSection, String idRow, String idSeat) {
    return _pricing.getPlaceRate(idSection + "|" + idRow + "|" + idSeat);
  }

  public void setSittingSectionVitalSpace(String idSection, double width, double height) {
    this._room.setSittingSectionVitalSpace(idSection, width, height);
  }

  public void setSittingSectionAutoDistribution(String idSection, boolean autoDistrib) {
    this._room.setSittingSectionAutoDistribution(idSection, autoDistrib);
  }

  public void deleteSittingRow(String idSection, String idRow) {
    this._room.deleteSittingRow(idSection, idRow);
  }

  public void clearAllSittingRows(String idSection) {
    this._room.clearAllSittingRows(idSection);
  }

  // SAVE
  public boolean save() {
    SaveObject save = new SaveObject(_room, _pricing);
    return _saveService.save(save);
  }

  public void saveTo(String path) {
    SaveObject save = new SaveObject(_room, _pricing);
    _recentPaths.remove(path);
    _recentPaths.add(path);
    _settings.set("recent_paths", _recentPaths);
    _saveService.saveTo(save, path);
  }

  public void loadProject(String absolutePath) {
    SaveObject save = _saveService.loadFrom(absolutePath);
    _recentPaths.remove(absolutePath);
    _recentPaths.add(absolutePath);
    _settings.set("recent_paths", _recentPaths);
    _room = (Room) save.getRoomData();
    _pricing = save.getPricing();
  }

  public ArrayList<String> getRecentPaths() {
    try {
      _recentPaths.forEach(path -> {
        Path nPath = Paths.get(path);
        if (!Files.exists(nPath)) {
          _recentPaths.remove(path);
          _settings.set("recent_paths", _recentPaths);
        }
      });
    } catch (Exception e) {
    }
    return _recentPaths;
  }

  public void exportAsJson(String path) {
    try (Writer writer = new FileWriter(path)) {
      System.out.println(path);
      Gson gson = new GsonBuilder().create();
      gson.toJson(_room, writer);
      gson.toJson(_pricing, writer);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  // Pricing && Offers

  public void createPlace(String id, String color) {
    ImmutablePlaceRate place = _pricing.getPlaceRate(id);
    if (place == null) {
      _pricing.createPlace(id, color, -1);
    }
  }

  public HashMap<String, ? extends ImmutableOffer> getOffers() {
    return _pricing.getOffers();
  }

  public ImmutableOffer createOffer(String name, String description, double reduction, String reductionType) {
    return _pricing.createOffer(name, description, reduction, reductionType);
  }

  public ImmutableOfferCondition createOfferCondition(String offerName, String offerConditionName, String description,
      String logicalOperator) {
    return _pricing.createOfferCondition(offerName, offerConditionName, description, logicalOperator);
  }

  public ImmutableOfferOperation createOfferConditionOperation(String offerName, String offerConditionName,
      String value, String relationalOperator, String logicalOperator) {
    return _pricing.createOfferConditionOperation(offerName, offerConditionName, value, relationalOperator,
        logicalOperator);
  }

  public ImmutableOffer getOffer(String name) {
    return _pricing.getOffer(name);
  }

  public HashMap<String, ? extends ImmutableOfferCondition> getOfferConditions(String offerName) {
    return _pricing.getOfferConditions(offerName);
  }

  public ImmutableOfferCondition getOfferCondition(String offerName, String offerCondtionName) {
    return _pricing.getImmutableOfferCondition(offerName, offerCondtionName);
  }

  public void setOfferReduction(String name, double reduction) {
    _pricing.setOfferReduction(name, reduction);
  }

  public ImmutableOffer setOfferName(String name, String newName) {
    return _pricing.setOfferName(name, newName);
  }

  public void setOfferConditionName(String offerName, String offerConditionName, String nName) {
    _pricing.setOfferConditionName(offerName, offerConditionName, nName);
  }

  public void setOfferConditionDescription(String offerName, String offerConditionName, String description) {
    _pricing.setOfferConditionDescription(offerName, offerConditionName, description);

  }

  public String[] getRelationalOperatorTypePossibilities() {
    return _pricing.getRelationalOperatorTypePossibilities();
  }

  public String[] getLogicalOperatorTypePossibilities() {
    return _pricing.getLogicalOperatorTypePossibilities();
  }

  public HashMap<String, ? extends ImmutablePlaceRate> getPrices() {
    return _pricing.getPlaces();
  }

  public void setOfferConditionOperationValue(String offerName, String offerConditionName, int index, String value) {
    _pricing.setOfferConditionOperationValue(offerName, offerConditionName, index, value);
  }

  public void setOfferConditionOperationLogicalOperator(String offerName, String offerConditionName, int index,
      String logicalOperator) {
    _pricing.setOfferConditionOperationLogicalOperator(offerName, offerConditionName, index, logicalOperator);
  }

  public void setOfferConditionOperationRelationalOperator(String offerName, String offerConditionName, int index,
      String relationalOperator) {
    _pricing.setOfferConditionOperationRelationalOperator(offerName, offerConditionName, index, relationalOperator);
  }

  public void removeOfferConditionOperation(String offerName, String offerConditionName, int index) {
    _pricing.removeOfferConditionOperation(offerName, offerConditionName, index);
  }

  // Save Methods

  public void closeProject() {
    _saveService = new InnoSave();
    _pricing = new Pricing();
    _room = null;
  }

  // Settings methods
  public void setSettingsValue(String key, Object value) {
    _settings.set(key, value);
  }

  public Object getSettingsValue(String key) {
    return _settings.get(key);
  }

};