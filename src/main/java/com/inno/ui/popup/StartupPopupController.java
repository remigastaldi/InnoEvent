/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 15th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.view.startuppopup.main;

import java.io.File;

import com.inno.InnoViewController;
import com.inno.service.View.AnimationDirection;

import javafx.fxml.FXML;

import javafx.stage.FileChooser;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;StartupPopupController
import javafx.scene.layout.StackPane;

public class StartupPopupMainViewController extends InnoViewController {

  @FXML
  private AnchorPane anchorRoot;
  @FXML
  private StackPane parentContainer;

  public StartupPopupMainViewController() {
  }

  @FXML
  private void initialize() {
  }

  @FXML
  private void openProjectButtonAction() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("InnoEvent", "*.inevt"));
    File file = fileChooser.showOpenDialog(InnoCore().View().getMainView());
    if (file != null) {
      System.out.println("OK");
      System.out.println(file);
    }
  }

  @FXML
  private void createNewProjectButtonAction() {
    InnoCore().View().openViewWithAnimation("popup_new_project.fxml", AnimationDirection.LEFT, anchorRoot);
  }
}