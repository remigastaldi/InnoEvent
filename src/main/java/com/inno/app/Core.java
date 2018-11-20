/*
 * File Created: Tuesday, 9th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 20th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app;

import com.inno.app.InnoSave;
import com.inno.app.room.*;
import com.inno.service.pricing.Pricing;

public class Core {

  private static Core _instance = null;

  // Services
  private InnoSave  _saveService = new InnoSave();
  private Pricing _pricing = new Pricing();

  // Inno Class
  private Room  _room = null;

  public Core() {
  }

  public static Core get() {
    if (_instance == null) {
      _instance = new Core();
    }

    return _instance;
  }

  //Room methods
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

  //Scene methods
  public void createScene(double width, double height, double[] positions) {
    this._room.createScene(width, height, positions);
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

  //Section methods
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

    //standingSection Methods
  public ImmutableStandingSection createStandingSection(double elevation, int nbPeople, double[] positions, double rotation) {
    return this._room.createStandingSection(elevation, nbPeople, positions, rotation);
  }

  /*public ImmutableStandingSection getImmutableStandingSection(int idSection) {
    return this;
  }*/

  public void setStandingNbPeople(String idSection, int nbPeople) {
    this._room.setStandingNbPeople(idSection, nbPeople);
  }

    //sittingSection Methods
    public ImmutableSittingSection createSittingSection(double elevation, double[] positions, double rotation) {
      return this._room.createSittingSection(elevation, positions, rotation);
    }

    public void setSittingSectionVitalSpace(String idSection, double height, double width) {
      this._room.setSittingSectionVitalSpace(idSection, height, width);
    }
  
};