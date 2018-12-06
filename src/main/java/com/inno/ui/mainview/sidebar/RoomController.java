/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 6th December 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import com.inno.app.Core;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.InnoEngine;

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
    InnoEngine engine = (InnoEngine) getIntent();

       

    project_name_input.setText(Core.get().getImmutableRoom().getName());
    room_height_input.setText(Integer.toString((int) Core.get().getImmutableRoom().getHeight()));
    room_width_input.setText(Integer.toString((int) Core.get().getImmutableRoom().getWidth()));
    vital_space_height_input.setText(Integer.toString((int) Core.get().getImmutableRoom().getImmutableVitalSpace().getHeight()));
    vital_space_width_input.setText(Integer.toString((int) Core.get().getImmutableRoom().getImmutableVitalSpace().getWidth()));
  }

  @FXML
  private void onKeyReleased() {
    if (checkInputs()) {
      InnoEngine engine = (InnoEngine) getIntent();
      
      if (engine == null) {
        System.out.println("Engine is null");
        return;
      }
      try {
        if (project_name_input.isFocused())
          Core.get().setRoomName(project_name_input.getText());
        if (room_height_input.isFocused()) {
          engine.setBoardHeight(engine.meterToPixel(Double.parseDouble(room_height_input.getText())));
          Core.get().setRoomHeight(Double.parseDouble(room_height_input.getText()));
        }
        if (room_width_input.isFocused()) {
          engine.setBoardWidth(engine.meterToPixel(Double.parseDouble(room_width_input.getText())));
          Core.get().setRoomWidth(Double.parseDouble(room_width_input.getText()));
        }
        if (vital_space_height_input.isFocused())
          Core.get().setRoomVitalSpaceHeight(Double.parseDouble(vital_space_height_input.getText()));
        if (vital_space_width_input.isFocused())
          Core.get().setRoomVitalSpaceWidth(Double.parseDouble(vital_space_width_input.getText()));
      } catch (Exception e) {
        System.out.println(e);
      }
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