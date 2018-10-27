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

package com.inno.ui.mainview.sidebar;

import com.inno.ui.ViewController;

import javafx.fxml.FXML;


import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class RoomController extends ViewController {

  @FXML
  private Button createNewProjectButton;
  @FXML
  private AnchorPane anchorRoot;
  @FXML
  private StackPane parentContainer;

  public RoomController() {
  }

  @FXML
  private void initialize() {
  }
}