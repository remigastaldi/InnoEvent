/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 14th October 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.view.mainview;

import com.inno.InnoEngine;
import com.inno.InnoViewController;

import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javafx.scene.shape.Polygon;
import javafx.event.EventHandler;

import javafx.fxml.FXMLLoader;

public class MainViewController extends InnoViewController {
  @FXML
  private Pane graphicsPane;

  @FXML
  private AnchorPane sidebarAnchor;

  public MainViewController() {
  }

  @FXML
  private void initialize() {
    // Polygon polygon = new Polygon();
    // polygon.getPoints().addAll(new Double[] { 0.0, 0.0, 1000.0, 100.0, 10.0, 20.0 });
    // Polygon polygon2 = new Polygon();
    // polygon2.getPoints().addAll(new Double[] { 10.0, 50.0, 200.0, 300.0, 500.0, 400.0 });
    // polygon.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<javafx.scene.input.MouseEvent>() {
    //   public void handle(javafx.scene.input.MouseEvent event) {
    //     // code used for retrieving x,y values
    //     // canvas.removeEventHandler(MouseEvent.MOUSE_MOVED, this);
    //     System.out.println("Poly one");
    //   }
    // });
    // polygon2.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<javafx.scene.input.MouseEvent>() {
    //   public void handle(javafx.scene.input.MouseEvent event) {
    //     // code used for retrieving x,y values
    //     // canvas.removeEventHandler(MouseEvent.MOUSE_MOVED, this);
    //     System.out.println("Poly two");
    //   }
    // });

    // graphicsPane.getChildren().add(polygon);
    // graphicsPane.getChildren().add(polygon2);

    // graphicsPane.setPrefHeight(graphicsPane.getPrefHeight() * 2);
    // graphicsPane.setPrefWidth(graphicsPane.getPrefWidth() * 2);

    // graphicsPane.getChildren().remove(polygon2);
    // graphicsPane.setScaleX(2);
    // graphicsPane.setScaleY(2);
    InnoCore().setEngine(new InnoEngine(graphicsPane));
    InnoCore().View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);
  }

  @FXML
  private void quitButtonAction() {
    InnoCore().View().showStartupPopup();
  }

  @FXML
  private void testBtn() {
    System.out.println("ok");
    InnoCore().View().setSidebarFromFxmlFileName("sidebar_section.fxml", sidebarAnchor);
  }

  @FXML
  private void dragStart() {
    System.out.println("start");
  }

  @FXML
  private void dragDone() {
    System.out.println("done");
  }
}