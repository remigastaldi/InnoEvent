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

import com.inno.app.InnoSave;
import com.inno.app.room.Room;
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
};