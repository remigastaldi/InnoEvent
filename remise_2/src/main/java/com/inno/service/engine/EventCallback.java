/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 14th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.service.engine;

import  javafx.scene.input.MouseEvent;

public class EventCallback {
  public boolean onMouseClicked(MouseEvent event) { return true; }
  public boolean onMouseEntered(MouseEvent event) { return true; }
  public boolean onMouseExited(MouseEvent event)  { return true; }
  public boolean onMouseMoved(MouseEvent event)   { return true; }
  public boolean onMousePressed(MouseEvent event)  { return true; }
  public boolean onMouseReleased(MouseEvent event) { return true; }
  public boolean onMouseOnDragDetected(MouseEvent event) { return true; }
}