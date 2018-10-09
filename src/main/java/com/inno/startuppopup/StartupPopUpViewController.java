/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 4th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.startuppopup;

import java.io.File;

import javafx.fxml.FXML;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StartupPopUpViewController {

  Stage stage;

  public StartupPopUpViewController() {
  }

  public void init(Stage stage) {
    this.stage = stage;
  }

  @FXML
  private void initialize() {
  }

  @FXML
  private void openProject() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("InnoEvent", "*.inevt"));
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
      System.out.println("OK");
      System.out.println(file);
    }

  }

  @FXML
  private void createNewProject() {
    System.out.println("Create");
  }
}