/*
 * File Created: Sunday, 14th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 16th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.service.engine.shape;

import  javafx.scene.input.MouseEvent;
import  com.inno.service.engine.Engine;
import  javafx.scene.layout.Pane;
import  javafx.scene.shape.Circle;

public abstract class InteractiveShape {
  private Engine  _engine = null;
  private Pane  _pane = null;
  private boolean _collision = false;
  private Circle _cursor = null;
  
  public abstract void start();
  
  public void enableCollision(boolean collision) { _collision = collision; }

  public boolean onMouseEntered(MouseEvent event) { return true; }
  public boolean onMouseClicked(MouseEvent event) { return true; }
  public boolean onMouseExited(MouseEvent event)  { return true; }
  public boolean onMouseMoved(MouseEvent event)   { return true; }
  public boolean onMousePressed(MouseEvent event)  { return true; }
  public boolean onMouseReleased(MouseEvent event) { return true; }
  public boolean onMouseOnDragDetected(MouseEvent event) { return true; }
  public boolean onMouseOnDragDropped(MouseEvent event) { return true; }
}