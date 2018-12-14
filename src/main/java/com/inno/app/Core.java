/*
 * File Created: Tuesday, 9th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 14th December 2018
 * Modified By: GASTALDI Rémi
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
import com.inno.service.pricing.ImmutablePlaceRate;
import com.inno.service.pricing.Pricing;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Core {

  private static Core _instance = null;

  // Services
  private InnoSave _saveService = new InnoSave();
  private Pricing _pricing = new Pricing();
  private SettingsService _settings = new SettingsService();
  private ObservableList<String> _availableOffers = FXCollections.observableArrayList();

  private ArrayList<String> _recentPaths = new ArrayList<>();

  // Inno Class
  private Room _room = null;

  @SuppressWarnings("unchecked")
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

  public void setSceneElevation(double elevation) {
    this._room.setSceneElevation(elevation);
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

  public ImmutableSection duplicateSection(String idSection) {
    return this._room.duplicateSection(idSection);
  }

  // standingSection Methods
  public ImmutableStandingSection createStandingSection(int nbPeople, double[] positions, double rotation) {
    ImmutableStandingSection section = _room.createStandingSection(nbPeople, positions, rotation);
    createPlace(section.getIdSection(), "#6378bf");
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
      newRotation = Utils.calculateRectangleRotation(pt, positions);
    }
    ImmutableSittingSection section = _room.createSittingSection(positions, newRotation, isRectangle);

    return section;
  }

  public void setSectionPrice(String idSection, double price, String color) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(idSection + "|");
    _pricing.setPlaceRatePrice(idSection, price);
    if (color != null && price != -1) {
      _pricing.setPlaceRateColor(idSection, color);
    } else if (price == -1) {
      _pricing.setPlaceRateColor(idSection, "#6378bf");
    }
    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.setPlaceRatePrice(key, price);
      if (color != null && price != -1) {
        _pricing.setPlaceRateColor(key, color);
      } else if (price == -1 && (key.length() - key.replace("|", "").length()) == 1) {
        _pricing.setPlaceRateColor(key, "#7289DA");
      } else if (price == -1 && (key.length() - key.replace("|", "").length()) == 2) {
        _pricing.setPlaceRateColor(key, "#FFA500");
      }
    }
  }

  public void setSectionPrice(String idSection, double price) {
    setSectionPrice(idSection, price, null);
  }

  public void addSectionOffer(String id, String offerName) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(id + "|");
    _pricing.addPlaceRateOffer(id, offerName);
    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.addPlaceRateOffer(key, offerName);
    }
  }

  public void removeSectionOffer(String id, String offerName) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(id + "|");
    _pricing.removePlaceRateOffer(id, offerName);
    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.removePlaceRateOffer(key, offerName);
    }
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

  public void addRowOffer(String idSection, String idRow, String offerName) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(idSection + "|" + idRow + "|");
    _pricing.addPlaceRateOffer(idSection + "|" + idRow, offerName);

    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.addPlaceRateOffer(key, offerName);
    }
  }

  public void removeRowOffer(String idSection, String idRow, String offerName) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(idSection + "|" + idRow + "|");
    _pricing.removePlaceRateOffer(idSection + "|" + idRow, offerName);

    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.removePlaceRateOffer(key, offerName);
    }
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

  public void addSeatOffer(String idSection, String idRow, String idSeat, String offerName) {
    _pricing.addPlaceRateOffer(idSection + "|" + idRow + "|" + idSeat, offerName);
  }

  public void removeSeatOffer(String idSection, String idRow, String idSeat, String offerName) {
    _pricing.removePlaceRateOffer(idSection + "|" + idRow + "|" + idSeat, offerName);
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
    refreshOfferList();
  }

  public void refreshOfferList() {
    _availableOffers.clear();
    HashMap<String, ? extends ImmutableOffer> offers = getOffers();
    for (Map.Entry<String, ? extends ImmutableOffer> entry : offers.entrySet()) {
      _availableOffers.add(entry.getKey());
    }
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
      HashMap<String, Object> map = new HashMap<>();
      map.put("room", _room);
      map.put("pricing", _pricing);
      gson.toJson(map, writer);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  // Pricing && Offers

  public void createPlace(String id, String color, double price) {
    ImmutablePlaceRate place = _pricing.getPlaceRate(id);
    if (place == null) {
      _pricing.createPlace(id, color, price);
    }
  }

  public void createPlace(String id, String color) {
    createPlace(id, color, -1);
  }

  public HashMap<String, ? extends ImmutableOffer> getOffers() {
    return _pricing.getOffers();
  }

  public ImmutableOffer createOffer(String name, String description, double reduction, String reductionType) {
    ImmutableOffer offer = _pricing.createOffer(name, description, reduction, reductionType);
    refreshOfferList();
    return offer;
  }

  public void deleteOffer(String offerName) {
    _availableOffers.remove(offerName);
    _pricing.deleteOffer(offerName);
  }

  public ImmutableOfferCondition createOfferCondition(String offerName, String offerConditionName, String description,
      String logicalOperator) {
    return _pricing.createOfferCondition(offerName, offerConditionName, description, logicalOperator);
  }

  public void deleteOfferCondition(String offerName, String offerConditionName) {
    _pricing.deleteOfferCondition(offerName, offerConditionName);
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
    ImmutableOffer offer = _pricing.setOfferName(name, newName);
    refreshOfferList();
    return offer;
  }

  public void setOfferReductionType(String offerName, String reductionType) {
    _pricing.setOfferReductionType(offerName, reductionType);
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

  public String[] getReductionTypePossibilities() {
    return _pricing.getReductionTypePossibilities();
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

  public void addPlaceRateOffer(String id, String offerName) {
    _pricing.addPlaceRateOffer(id, offerName);
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

  public ObservableList<String> getObservableOffersList() {
    return _availableOffers;
  }

  public ImmutableSection createSectionFromBuffer() {
    ImmutableSection section = _room.createSectionFromBuffer();

    if (section == null)
      return null;

    double[] pos = section.getPositions();
    for (int i = 0; i < pos.length; ++i) {
      pos[i] += 1;
    }
    _room.updateSectionPositions(section.getIdSection(), pos);
    return section;
  }

  public void copySectionToBuffer(String id) {
    _room.copySectionToBuffer(id);
  }
};