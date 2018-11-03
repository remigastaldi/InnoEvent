/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 3rd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.innoengine;

// import com.inno.ui.engine.room.StandingSection;
import com.inno.ui.engine.Engine;
import com.inno.ui.innoengine.shape.InnoPolygon;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import  javafx.scene.shape.Shape;


public class InnoEngine extends Engine {
  private enum State {
    NOTHING,
    ADD_IRREGULAR_SECTION,
    ADD_REGULAR_SECTION,
    WAITING
  }
  private State _state = State.NOTHING;

  // Test
  // private StandingSection _section = new StandingSection();

  public InnoEngine(Pane pane) {
    super(pane);

    setBackgroundColor(Color.valueOf("#282C34"));
    activateGrid(true);
    // drawShapes(canvas.getGraphicsContext2D());
  }

  public void addSection(MouseEvent event) {
    // GraphicsContext gc = getGraphicsContext();
    
    System.out.println("Add Section");
    
    if (_state != State.ADD_IRREGULAR_SECTION) {
      // addPolyline(new Point2D(event.getX(), event.getY()));

    }
    // getCanvas().removeEventHandler(MouseEvent.MOUSE_MOVED, moveEvent);
    _state = State.ADD_IRREGULAR_SECTION;
  }

  public InnoPolygon test() {
    InnoPolygon innoPoly = new InnoPolygon(this, getPane());
    innoPoly.start();
    return innoPoly;
    // InnoPolygon pl = new InnoPolygon();
    // pl.start();
  }

  // public void 
}