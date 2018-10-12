/*
 * File Created: Tuesday, 9th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno;

import com.inno.service.View;
import com.inno.service.Save;
import com.inno.service.Utils;
import com.inno.room.Room;

public class Core {
  private static Core _instance = null; 

  // Services
  private Save  _saveService = null;
  private View  _viewService = null;
  private Utils _utilsService = null;

  // Inno Class
  private Room  _room = null;

  public Core() {
    _saveService = new Save();
    _viewService = new View();
    _utilsService = new Utils();
  }

  public static Core get() {
    if (_instance == null) {
      _instance = new Core();
    }

    return _instance;
  }

  public void test() {
    System.out.println("test");
  }
};