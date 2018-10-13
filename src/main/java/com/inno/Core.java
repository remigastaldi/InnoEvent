/*
 * File Created: Tuesday, 9th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 13th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno;

import com.inno.service.View;
import com.inno.service.Engine;
import com.inno.service.Save;
import com.inno.service.Utils;
import com.inno.room.Room;

public class Core {

  private static Core _instance = null; 

  // Services
  private Save  _saveService = new Save();
  private View  _viewService = null;
  private Utils _utilsService = new Utils();

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

  public void start(String[] args) {
    _viewService.run(args);
  }

  public void test() {
    _viewService.test();
    System.out.println("test");
  }

  public void setViewService(View view) {
    _viewService = view;
  }

  public void setEngine(InnoEngine engine) {
    _engine = engine;
  }
};