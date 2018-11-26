/*
 * File Created: Tuesday, 9th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app;

import java.util.HashMap;

import com.inno.app.InnoSave;
import com.inno.app.room.*;
import com.inno.service.Point;
import com.inno.service.Utils;
import com.inno.service.pricing.OfferData;
import com.inno.service.pricing.Pricing;

public class Core {

  private static Core _instance = null;

  // Services
  // private InnoSave _saveService = new InnoSave();
  private InnoSave _saveService = new InnoSave();
  private Pricing _pricing = new Pricing();

  // Inno Class
  private Room _room = null;

  private Core() {
  }

  public static Core get() {
    if (_instance == null) {
      _instance = new Core();
    }

    return _instance;
  }

  // Room methods
  public void createRoom(String name, double width, double height, double heightVitalSpace, double widthVitalSpace) {
    this._room = new Room(name, height, width, heightVitalSpace, widthVitalSpace);
  }

  public ImmutableRoom getImmutableRoom() {
    return this._room;
  }

  public void setRoomName(String name) {
    this._room.setName(name);
  }

  public void setRoomHeight(double height) {
    this._room.setHeight(height);
  }

  public void setRoomWidth(double width) {
    this._room.setWidth(width);
  }

  public void setRoomVitalSpaceHeight(double height) {
    this._room.setHeightVitalSpace(height);
  }

  public void setRoomVitalSpaceWidth(double width) {
    this._room.setWidthVitalSpace(width);
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
    this._room.setSectionId(idSection, name);
  }

  public void setSectionElevation(String idSection, double elevation) {
    this._room.setSectionElevation(idSection, elevation);
  }

  public void updateSectionPositions(String idSection, double[] positions) {

    this._room.updateSectionPositions(idSection, positions);
  }

  public void deleteSection(String idSection) {
    this._room.deleteSection(idSection);
  }

  public void setSectionRotation(String idSection, double rotation) {
    this._room.setSectionRotation(idSection, rotation);
  }

  // standingSection Methods
  public ImmutableStandingSection createStandingSection(int nbPeople, double[] positions, double rotation) {

    return this._room.createStandingSection(nbPeople, positions, rotation);
  }

  public void setStandingNbPeople(String idSection, int nbPeople) {
    this._room.setStandingNbPeople(idSection, nbPeople);
  }

  // sittingSection Methods
  public ImmutableSittingSection createSittingSection(double[] positions, double rotation) {
    // Point pt = new Point(getImmutableRoom().getImmutableScene().getCenter()[0],
    // getImmutableRoom().getImmutableScene().getCenter()[1]);
    // double[] newPos = Utils.rotateRectangle(pt, positions);
    // double newRotation = Utils.calculateRectangleRotation(pt, positions);
    // return this._room.createSittingSection(newPos, newRotation);
    return this._room.createSittingSection(positions, rotation);
  }

  public void setSittingSectionVitalSpace(String idSection, double width, double height) {
    this._room.setSittingSectionVitalSpace(idSection, width, height);
  }

  public void setSittingSectionAutoDistribution(String idSection, boolean autoDistrib) {
    this._room.setSittingSectionAutoDistribution(idSection, autoDistrib);
  }

  public ImmutableSittingRow createSittingRow(String idSection, double[] posStart, double[] posEnd) {
    return this._room.createSittingRow(idSection, posStart, posEnd);
  }

  public void deleteSittingRow(String idSection, String idRow) {
    this._room.deleteSittingRow(idSection, idRow);
  }

  public void clearAllSittingRows(String idSection) {
    this._room.clearAllSittingRows(idSection);
  }

  public ImmutableSeat createSeat(String sectionId, String idRow, double[] pos) {
    return this._room.createSeat(sectionId, idRow, pos);
  }

  // SAVE
  public boolean save() {
    SaveObject save = new SaveObject(_room);
    return _saveService.save(save);
  }

  public void saveTo(String path) {
    SaveObject save = new SaveObject(_room);
    _saveService.saveTo(save, path);
  }

  public void loadProject(String absolutePath) {
    SaveObject save = _saveService.loadFrom(absolutePath);

    _room = (Room) save.getRoomData();
  }

  public HashMap<String, ? extends OfferData> getOffers() {
    return _pricing.getOffers();
  }

  public OfferData createOffer(String name, String description, double reduction, String reductionType) {
    return _pricing.createOffer(name, description, reduction, reductionType);
  }

  public void closeProject() {
    _saveService = new InnoSave();
    _pricing = new Pricing();
    _room = null;
  }

public OfferData getOffer(String name) {
	return _pricing.getOffer(name);
}

public void setOfferReduction(String name, double reduction) {
  _pricing.setOfferReduction(name, reduction);
}

public OfferData setOfferName(String name, String newName) {
  return _pricing.setOfferName(name, newName);
}

  // Save Methods
};