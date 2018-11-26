/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import java.io.File;

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
    View().setSidebar(sidebar_anchor);
    View().setSidebarFromFxmlFileName("sidebar_room.fxml");

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
      case "r":
        Engine().createRectangularSection();
    }
    
    if (evt.getCode() == KeyCode.DELETE)
      Engine().deleteSelectedShape();
  }

  @FXML
  private void menuQuitButtonAction() {
    Core().closeProject();
    View().showStartupPopup();
  }

  @FXML
  private void menuOpenProjectAction() {
    File file = View().getProjectFilePath();
    if (file != null) {
      Core().loadProject(file.getAbsolutePath());
      View().showMainView();
    }
  }

  @FXML
  private void menuSaveAction() {
    if (Core().save() == false) {
      menuSaveAsAction();
    }
  }

  @FXML
  private void menuSaveAsAction() {
    File file = View().getSaveProjectFilePath();
    if (file != null) {
      Core().saveTo(file.getAbsolutePath());
    }
  }

  @FXML
  private void openRoom() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml");
  }

  @FXML
  private void openRegularSittingSection() {
    View().setSidebarFromFxmlFileName("sidebar_regular_sitting_section.fxml");
  }

  @FXML
  private void openIrregularSittingSection() {
    View().setSidebarFromFxmlFileName("sidebar_irregular_sitting_section.fxml");
  }

  @FXML
  private void openStandingSection() {
    View().setSidebarFromFxmlFileName("sidebar_standing_section.fxml");
  }

  @FXML
  private void openScene() {
    View().setSidebarFromFxmlFileName("sidebar_scene.fxml");
  }
}