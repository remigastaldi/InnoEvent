/*
 * File Created: Monday, 17th December 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Monday, 17th December 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */


package com.inno.ui.popup;

import java.awt.SystemTray;
import java.io.File;

import com.inno.ui.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SavePopupController extends ViewController {
    
    @FXML
    private Button yes_button;
    @FXML
    private Button no_button;

  @FXML
  private void initialize() {
  }

  @FXML
  private void noButtonAction() {
    String type = (String) getIntent();
    Stage stage = (Stage) yes_button.getScene().getWindow();
    stage.close();
    if (type == "new" || type == "close") {
        Core().closeProject();
        View().showStartupPopup();
    }
    else if (type == "quit") {
        Core().closeProject();
        View().closeView(View().getMainView());
    }
  }

  @FXML
  private void yesButtonAction() {
    String type = (String) getIntent();
    Stage stage = (Stage) yes_button.getScene().getWindow();
    stage.close(); 
    File file = View().getSaveProjectFilePath("*.inevt", Core().getImmutableRoom().getName());
    if (file != null) {
        Core().saveTo(file.getAbsolutePath());
    }
    if (type == "new" || type == "close") {
        Core().closeProject();
        View().showStartupPopup();
    }
    else if (type == "quit") {
        Core().closeProject();
        View().closeView(View().getMainView());
    }
  }

  @Override
  public void init() {

  }
}