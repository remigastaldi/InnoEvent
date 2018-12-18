/*
 * File Created: Tuesday, 9th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 18th December 2018
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inno.app.room.ImmutableRoom;
import com.inno.app.room.ImmutableScene;
import com.inno.app.room.ImmutableSection;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.ImmutableStandingSection;
import com.inno.app.room.Room;
import com.inno.app.undoredo.UndoRedoHelper;
import com.inno.service.SettingsService;
import com.inno.service.pricing.ImmutableOffer;
import com.inno.service.pricing.ImmutableOfferCondition;
import com.inno.service.pricing.ImmutableOfferOperation;
import com.inno.service.pricing.ImmutablePlaceRate;
import com.inno.service.pricing.Pricing;
import com.inno.ui.innoengine.InnoEngine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Core {
  private InnoEngine _engine = null;
  private static Core _instance = null;

  // Services
  private UndoRedoHelper _undoRedo = null;
  private InnoSave _saveService = new InnoSave();
  private Pricing _pricing;
  private SettingsService _settings = new SettingsService();
  private ObservableList<String> _availableOffers = FXCollections.observableArrayList();

  public enum AttributionType {
    SEAT, ROW, SECTION
  };

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

  public void setEngine(InnoEngine engine) {
    _engine = engine;
    createUndoRedoHelper(_room);
  }

  public String[] getAttributionTypesPossibilities() {
    return Arrays.stream(AttributionType.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
  }

  private void createUndoRedoHelper(Room room) {
    _undoRedo = new UndoRedoHelper(room, _pricing);
  }

  // Room methods
  public void createRoom(String name, double width, double height, double widthVitalSpace, double heightVitalSpace) {
    _room = new Room(name, width, height, widthVitalSpace, heightVitalSpace);
    _pricing = new Pricing();
  }

  public ImmutableRoom getImmutableRoom() {
    return _room;
  }

  public void setRoomName(String name) {
    _room.setName(name);
  }

  public void setRoomWidth(double width) {
    _room.setWidth(width);
  }

  public void setRoomHeight(double height) {
    _room.setHeight(height);
  }

  public void setRoomVitalSpace(double width, double height) {
    _room.setVitalSpace(width, height);
  }

  // Scene methods
  public ImmutableScene createScene(double width, double height, double[] positions) {
    return _room.createScene(width, height, positions);
  }

  public void deleteScene() {
    _room.deleteScene();
  }

  public void setSceneWidth(double width) {
    _room.setSceneWidth(width);
  }

  public void setSceneHeight(double height) {
    _room.setSceneHeight(height);
  }

  public void setScenePositions(double[] positions) {
    _room.setScenePositions(positions);
  }

  public void setSceneRotation(double rotation) {
    _room.setSceneRotation(rotation);
  }

  public void setSceneElevation(double elevation) {
    _room.setSceneElevation(elevation);
  }

  // Section methods
  public void setSectionName(String idSection, String name) {
    _room.setSectionName(idSection, name);
  }

  public void setSectionElevation(String idSection, double elevation) {
    _room.setSectionElevation(idSection, elevation);
  }

  public void updateSectionPositions(String idSection, double[] positions, boolean rectangular) {
    _undoRedo.updateSectionPositions(idSection, positions, rectangular);
  }

  // public void deleteStandingSection(String idSection)
  public void deleteSection(String idSection) {
    _undoRedo.deleteSection(idSection);
  }

  public void setSectionUserRotation(String idSection, double rotation) {
    // _room.setSectionRotation(idSection, rotation);
    _room.setSectionUserRotation(idSection, rotation);
  }

  public ImmutableStandingSection sittingToStandingSection(String idSection) {
    return _room.sittingToStandingSection(idSection);
  }

  public ImmutableSittingSection standingToSittingSection(String idSection) {
    return _room.standingToSittingSection(idSection);
  }

  public ImmutableSection duplicateSection(String idSection) {
    return _room.duplicateSection(idSection);
  }

  // standingSection Methods
  // public void createStandingSection(int nbPeople, double[] positions, double rotation) {
    // Command command = new CreateStandingSection(_engine, _room, nbPeople,
    // positions, rotation);
    // command.execute();
    // _undoRedo.insert(command);
  // }

  public void setStandingNbPeople(String idSection, int nbPeople) {
    _room.setStandingNbPeople(idSection, nbPeople);
  }

  // sittingSection Methods
  public ImmutableSittingSection createSittingSection(double[] positions, double rotation, boolean isRectangle) {
    ImmutableSittingSection section = _undoRedo.createSittingSection(positions, rotation, isRectangle);
    return section;
  }

  public void setSectionPrice(String idSection, double price, String color, Boolean all) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(idSection + "|");
    _pricing.setPlaceRatePrice(idSection, price);
    if (color != null && price != -1) {
      if (all) {
        setPlaceRateColor(idSection, price, color);
      } else {
        _pricing.setPlaceRateColor(idSection, color);
      }
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

  public void setSectionPrice(String idSection, double price, String color) {
    setSectionPrice(idSection, price, color, true);
  }

  private void setPlaceRateColor(String key, double price, String color) {
    _pricing.setPlaceRateColor(key, color);
    for (ImmutablePlaceRate place : _pricing.getPlaces().values()) {
      if (place.getPrice() == price) {
        _pricing.setPlaceRateColor(place.getId(), color);
      }
    }
    _engine.updateAllSectionsFromData();
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

  public void setRowPrice(String idSection, String idRow, double price, String color, Boolean all) {
    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(idSection + "|" + idRow + "|");
    _pricing.setPlaceRatePrice(idSection, -1);
    _pricing.setPlaceRateColor(idSection, "#6378bf");

    _pricing.setPlaceRatePrice(idSection + "|" + idRow, price);
    if (color != null) {
      if (all) {
        setPlaceRateColor(idSection + "|" + idRow, price, color);
      } else {
        _pricing.setPlaceRateColor(idSection + "|" + idRow, color);
      }
    }
    for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
      String key = entry.getKey();
      _pricing.setPlaceRatePrice(key, price);
      if (color != null) {
        _pricing.setPlaceRateColor(key, color);
      }
    }
  }

  public void setRowPrice(String idSection, String idRow, double price, String color) {
    setRowPrice(idSection, idRow, price, color, true);
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

  public void setSeatPrice(String idSection, String idRow, String idSeat, double price, String color, Boolean all) {
    _pricing.setPlaceRatePrice(idSection, -1);
    _pricing.setPlaceRateColor(idSection, "#6378bf");
    _pricing.setPlaceRatePrice(idSection + "|" + idRow, -1);
    _pricing.setPlaceRateColor(idSection + "|" + idRow, "#7289DA");
    _pricing.setPlaceRatePrice(idSection + "|" + idRow + "|" + idSeat, price);
    if (color != null) {
      if (all) {
        setPlaceRateColor(idSection + "|" + idRow + "|" + idSeat, price, color);
      } else {
        _pricing.setPlaceRateColor(idSection + "|" + idRow + "|" + idSeat, color);
      }
    }
  }

  public void setSeatPrice(String idSection, String idRow, String idSeat, double price, String color) {
    setSeatPrice(idSection, idRow, idSeat, price, color, true);
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
    _room.setSittingSectionVitalSpace(idSection, width, height);
  }

  public void setSittingSectionAutoDistribution(String idSection, boolean autoDistrib) {
    _room.setSittingSectionAutoDistribution(idSection, autoDistrib);
  }

  // public void deleteSittingRow(String idSection, String idRow) {
  // _room.deleteSittingRow(idSection, idRow);
  // }

  // public void clearAllSittingRows(String idSection) {
  // _room.clearAllSittingRows(idSection);
  // }

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

  public void deletePlaceRate(String key) {
    _pricing.deletePlaceRate(key);
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

  public String getColorPrice(double price) {
    for (ImmutablePlaceRate place : _pricing.getPlaces().values()) {
      if (place.getPrice() == price) {
        return place.getColor();
      }
    }
    return null;
  }

  // Save Methods

  public void closeProject() {
    _saveService = new InnoSave();
    _pricing = null;
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

  public boolean setAutomaticPrices(double minPrice, double maxPrice, double total, String attributionType) {
    AttributionType attributionTypeEnum = _pricing.getEnumFromString(AttributionType.class, attributionType);

    boolean done = AutomaticPrices.setAutomaticPrices(_room, minPrice, maxPrice, total, attributionTypeEnum);
    _engine.updateAllSectionsFromData();
    return done;
  }

  public void copySectionToBuffer(String id) {
    _room.copySectionToBuffer(id);
  }

  public void createSectionFromBuffer() {
    _undoRedo.createSectionFromBuffer();
  }

  public void undo() {
    _undoRedo.undo(1);
  }

  public void redo() {
    _undoRedo.redo(1);
  }

  public boolean hasChanged() {
    return _undoRedo.hasChanged();
  }

  public void createRectangularSection(String id) {
    _engine.createRectangularSection(id);
  }

  public void createIrregularSection(String id, boolean b) {
    _engine.createIrregularSection(id, b);
  }

  public void updateSectionFromData(String _idSection) {
    _engine.updateSectionFromData(_idSection);
  }

  public void deleteUiSection(String id) {
    _engine.deleteSection(id);
  }

  public double getAutoMinPrice() {
    return AutomaticPrices.getMinPrice();
  }

  public double getAutoTotal() {
    return AutomaticPrices.getTotal();
  }

  public String getAutoAttributionType() {
    return AutomaticPrices.getAttributionType().toString();
  }

  public double getAutoMaxPrice() {
    return AutomaticPrices.getMaxPrice();
  }

public boolean isAutoEnabled() {
	return AutomaticPrices.isEnabled();
}
};