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

import com.inno.ui.innoengine.InnoEngine;
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

  public MainViewController() {
  }

  @FXML
  private void initialize() {
    InnoCore().View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);

    scrollPane.setVvalue(0.5);
    scrollPane.setHvalue(0.5);

    InnoCore().setEngine(new InnoEngine(graphicsPane));
  }

  @FXML
  private void  keyAction() {
    System.out.println("Add section");
    InnoCore().Engine().test();
    // System.out.println(event.getKeyChar());
  }

  @FXML
  private void quitButtonAction() {
    InnoCore().View().showStartupPopup();
  }

  @FXML
  private void openRoom() {
    InnoCore().View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);
  }

  @FXML
  private void openSection() {
    InnoCore().View().setSidebarFromFxmlFileName("sidebar_section.fxml", sidebarAnchor);
  }
}