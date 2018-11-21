/*
 * File Created: Friday, 12th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Wednesday, 21st November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.ui.popup;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.View.AnimationDirection;

public class StartupPopupNewProjectViewController extends ViewController {
  @FXML
  private Button cancel_button;
  @FXML
  private Button done_button;
  @FXML
  private AnchorPane anchor_root;
  @FXML
  private TextField project_name_input;
  @FXML
  private TextField room_width_input;
  @FXML
  private TextField room_height_input;
  @FXML
  private TextField scene_width_input;
  @FXML
  private TextField scene_height_input;
  @FXML
  private TextField vital_space_width_input;
  @FXML
  private TextField vital_space_height_input;

  public void init() {
  }

  @FXML
  private void initialize() {
  }

  @FXML
  private void doneButtonAction() {

    if (checkInputs(true) == false) {
      System.out.println("EEEE");
      return;
    }
    System.out.println(project_name_input.getText());

    Double roomWidth = Double.parseDouble(room_width_input.getText());
    Double roomHeight = Double.parseDouble(room_height_input.getText());
    Double sceneWidth = Double.parseDouble(scene_width_input.getText());
    Double sceneHeight = Double.parseDouble(scene_height_input.getText());
    Double vitalSpaceWidth = Double.parseDouble(vital_space_width_input.getText());
    Double vitalSpaceHeight = Double.parseDouble(vital_space_height_input.getText());

    System.out.println(roomWidth);
    double[] scenePos = { roomWidth / 2 - sceneWidth / 2, roomHeight / 2 - sceneHeight / 2,
        roomWidth / 2 + sceneWidth / 2, roomHeight / 2 - sceneHeight / 2, roomWidth / 2 + sceneWidth / 2,
        roomHeight / 2 + roomHeight / 2, roomWidth / 2 - sceneWidth / 2, roomHeight / 2 + roomHeight / 2 };

    Core().createRoom(project_name_input.getText(), roomWidth, roomHeight, vitalSpaceWidth, vitalSpaceHeight);

    Core().createScene(sceneWidth, sceneHeight, scenePos);

    // Core().setVitalS(Integer.parseInt(room_height_input.getText()));

    View().showMainView();
  }

  @FXML
  private void onKeyReleased() {
    checkInputs(false);
  }

  private boolean checkInputs(boolean required) {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(project_name_input, required ? "required|" : "" + "max:30");
    fields.put(room_width_input, required ? "required|" : "" + "numeric");
    fields.put(room_height_input, required ? "required|" : "" + "numeric");
    fields.put(scene_width_input, required ? "required|" : "" + "numeric");
    fields.put(scene_height_input, required ? "required|" : "" + "numeric");
    fields.put(vital_space_width_input, required ? "required|" : "" + "numeric");
    fields.put(vital_space_height_input, required ? "required|" : "" + "numeric");

    for (Map.Entry<TextField, String> entry : fields.entrySet()) {
      TextField field = entry.getKey();
      String validator = entry.getValue();
      if ((required || field.isFocused()) && !Validator.validate(field.getText(), validator)) {
        valid = false;
        if (!field.getStyleClass().contains("error"))
          field.getStyleClass().add("error");
      } else if (field.isFocused()) {
        if (field.getStyleClass().contains("error"))
          field.getStyleClass().remove("error");
      }
    }
    return valid;
  }

  @FXML
  private void cancelButtonAction() {
    View().openViewWithAnimation("popup.fxml", AnimationDirection.RIGHT, anchor_root);
  }
}