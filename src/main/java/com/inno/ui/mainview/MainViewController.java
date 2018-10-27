/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 27th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import com.inno.ui.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainViewController extends ViewController {
  @FXML
  private Pane graphicsPane;

  @FXML
  private AnchorPane sidebarAnchor;

  @FXML
  private ScrollPane scrollPane;

  @FXML
  private void initialize() {
  }

  public void init() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);
  
    scrollPane.setVvalue(0.5);
    scrollPane.setHvalue(0.5);
  
    View().createEngine(graphicsPane);
  }

  @FXML
  private void  keyAction() {
    System.out.println("Add section");
    Engine().test();
    // System.out.println(event.getKeyChar());
  }

  @FXML
  private void quitButtonAction() {
    View().showStartupPopup();
  }

  @FXML
  private void openRoom() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);
  }

  @FXML
  private void openSection() {
    View().setSidebarFromFxmlFileName("sidebar_section.fxml", sidebarAnchor);
  }
}