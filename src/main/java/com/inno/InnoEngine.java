/*
 * File Created: Friday, 12th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 14th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno;

import java.util.ArrayList;

import com.inno.room.Section;
import com.inno.room.StandingSection;
import com.inno.service.engine.Engine;
// import com.inno.service.engine.GraphicShape;
// import com.inno.service.engine.Polyline;

import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.geometry.Point2D;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;


public class InnoEngine extends Engine {
  private enum State {
    NOTHING,
    ADD_IRREGULAR_SECTION,
    ADD_REGULAR_SECTION,
    WAITING
  }
  private State _state = State.NOTHING;

  // Test
  private StandingSection _section = new StandingSection();

  public InnoEngine(Pane pane) {
    super(pane);

    setBackground(Color.valueOf("#282C34"));
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

  private void drawLine(MouseEvent event) {
    // System.out.println("Draw Line");
    // update();
    // GraphicsContext gc = getGraphicsContext();
  }
}