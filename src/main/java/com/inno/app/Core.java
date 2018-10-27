/*
 * File Created: Tuesday, 9th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 27th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.app;

import com.inno.ui.View;
import com.inno.ui.innoengine.InnoEngine;
import com.inno.service.Save;
import com.inno.app.room.Room;

public class Core {

  private static Core _instance = null; 

  // Services
  private View  _viewService = null;
  private Save  _saveService = new Save();

  // Inno Class
  private Room  _room = null;
  private InnoEngine  _engine = null;

  public Core() {
    // _saveService = new Save();
    // _viewService = new View();
    // _utilsService = new Utils();
  }

  public static Core get() {
    if (_instance == null) {
      _instance = new Core();
    }

    return _instance;
  }

  public void setViewService(View view) {
    _viewService = view;
  } 

  public void setEngine(InnoEngine engine) {
    _engine = engine;
  }

  // Services
  public InnoEngine Engine() {
    return _engine;
  }

  public View View() {
    return _viewService;
  }
  
  public Save Save() {
    return _saveService;
  }

};