/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import com.inno.ui.ViewController;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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

    // double[] pos = new double[]{10, 10, 50, 10, 100, 100, 10, 50};
    // Core().createSittingSection(0, pos, 0.0);

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
      case "o":
        Core().save();
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
  private void openRegularSittingSection() {
    View().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml", sidebar_anchor);
  }

  @FXML
  private void openIrregularSittingSection() {
    View().setSidebarFromFxmlFileName("sidebar_irregular_sitting_section.fxml", sidebar_anchor);
  }

  @FXML
  private void openStandingSection() {
    View().setSidebarFromFxmlFileName("sidebar_standing_section.fxml", sidebar_anchor);
  }

  @FXML
  private void openScene() {
    View().setSidebarFromFxmlFileName("sidebar_scene.fxml", sidebar_anchor);
  }
}