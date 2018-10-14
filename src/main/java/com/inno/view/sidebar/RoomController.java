/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 13th October 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.view.sidebar;

import com.inno.InnoViewController;

import javafx.fxml.FXML;


import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class RoomController extends InnoViewController {

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