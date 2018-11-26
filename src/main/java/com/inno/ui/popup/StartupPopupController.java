/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 25th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.io.File;

import com.inno.ui.ViewController;
import com.inno.ui.View.AnimationDirection;

import javafx.fxml.FXML;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class StartupPopupController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private StackPane parentContainer;

  public StartupPopupController() {
  }

  @FXML
  private void initialize() {
  }

  public void init() {
    //View().openPopup("offer_manager.fxml");

  }

  @FXML
  private void openProjectButtonAction() {
    File file = View().getProjectFilePath();
    if (file != null) {
      Core().loadProject(file.getAbsolutePath());
      View().showMainView();
    }
  }

  @FXML
  private void createNewProjectButtonAction() {
    View().openViewWithAnimation("popup_new_project.fxml", AnimationDirection.LEFT, anchor_root);
  }
}