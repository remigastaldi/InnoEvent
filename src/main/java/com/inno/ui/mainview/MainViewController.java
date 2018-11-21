/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 21st November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import com.inno.ui.ViewController;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainViewController extends ViewController {
  @FXML
  private AnchorPane top_bar;
  @FXML
  private AnchorPane sidebar_anchor;
  @FXML
  private AnchorPane anchor_canvas;
  @FXML
  private StackPane stack_pane;
  
  @FXML
  private void initialize() {
  }
  
  public void init() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebar_anchor);

    View().createEngine(stack_pane);
  }

  @FXML
  private void  keyAction(KeyEvent evt) {
    switch (evt.getText()) {
      case "a":
        Engine().createIrregularSection();
        break;
      case "s":
        Engine().createRectangularSection();
      // case "p":
        // Engine.save
    }
    
    if (evt.getCode() == KeyCode.DELETE)
      Engine().deleteSelectedShape();
  }

  @FXML
  private void quitButtonAction() {
    View().showStartupPopup();
  }

  @FXML
  private void openRoom() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebar_anchor);
  }

  @FXML
  private void openSection() {
    View().setSidebarFromFxmlFileName("sidebar_section.fxml", sidebar_anchor);
  }
}