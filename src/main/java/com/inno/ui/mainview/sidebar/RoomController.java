/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 28th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class RoomController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private TextField project_name_input;
  @FXML
  private TextField room_width_input;
  @FXML
  private TextField room_height_input;
  @FXML
  private TextField vital_space_width_input;
  @FXML
  private TextField vital_space_height_input;

  @FXML
  private void initialize() {
  }

  public void init() {
  }

  @FXML
  private void onKeyReleased() {
    if (checkInputs()) {
      // TODO: Add function to change room settings
    }
  }

  private boolean checkInputs() {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(project_name_input, "required|max:30");
    fields.put(room_width_input, "required|numeric");
    fields.put(room_height_input, "required|numeric");
    fields.put(vital_space_width_input, "required|numeric");
    fields.put(vital_space_height_input, "required|numeric");

    for (Map.Entry<TextField, String> entry : fields.entrySet()) {
      TextField field = entry.getKey();
      String validator = entry.getValue();
      if (!Validator.validate(field.getText(), validator)) {
        if (!field.getStyleClass().contains("error"))
          field.getStyleClass().add("error");
        valid = false;
      } else {
        if (field.getStyleClass().contains("error"))
          field.getStyleClass().remove("error");
      }
    }
    return valid;
  }
}