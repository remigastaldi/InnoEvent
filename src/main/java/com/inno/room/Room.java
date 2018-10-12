/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.room;

public class Room {
  private SectionHandler _sectionHandler = null;
  private Scene _scene = null;

  public Room(){
    _sectionHandler = new SectionHandler();
  }
}