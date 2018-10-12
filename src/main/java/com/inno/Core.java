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

public class Core {
  private static Core _instance = null; 
  
  private Save  _saveService;
  private View  _viewService;

  public Core() {
    _saveService = new Save();
    _viewService = new View();
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