/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI R??mi
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI R??mi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.InnoEngine;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.converter.NumberStringConverter;
import javafx.beans.value.ChangeListener;

public class NewSceneController extends ViewController {
  @FXML
  private TextField width_input;
  @FXML
  private TextField height_input;

  private SimpleDoubleProperty widthInput = new SimpleDoubleProperty();
  private SimpleDoubleProperty heightInput = new SimpleDoubleProperty();

  public NewSceneController() {
  }

  @FXML
  private void initialize() {
  }

  private boolean checkInputs() {
    boolean valid = true;
    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(width_input, "required|numeric|min:1");
    fields.put(height_input, "required|numeric|min:1");

    for (Map.Entry<TextField, String> entry : fields.entrySet()) {
      TextField field = entry.getKey();
      String validator = entry.getValue();
      if (!Validator.validate(field.getText(), validator)) {
        if (!field.getStyleClass().contains("error"))
          field.getStyleClass().add("error");
        valid = false;
      } else if (field.isFocused()) {
        if (field.getStyleClass().contains("error"))
          field.getStyleClass().remove("error");
      }
    }
    return valid;
  }

  @FXML
  public void onKeyReleasedAction() {
    if (checkInputs()) {
      InnoRectangle rectangle = (InnoRectangle) getIntent();

      if (rectangle == null) {
        System.out.println("Rectangle is null");
        return;
      }
      try {
        if (width_input.isFocused())
          rectangle.setWidth(Integer.parseInt(width_input.getText()));
        if (height_input.isFocused())
          rectangle.setRowNumber(Integer.parseInt(height_input.getText()));
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  @FXML
  public void doneButtonAction() {
    if (checkInputs() == false) {
      return;
    }
    double roomHeight = Core().getImmutableRoom().getHeight();
    double roomWidth = Core().getImmutableRoom().getWidth();      

    double sceneHeight = Integer.parseInt(height_input.getText());
    double sceneWidth = Integer.parseInt(width_input.getText());
    double[] scenePos = { roomWidth / 2 - sceneWidth / 2, roomHeight / 2 - sceneHeight / 2,
      roomWidth / 2 + sceneWidth / 2, roomHeight / 2 - sceneHeight / 2, roomWidth / 2 + sceneWidth / 2,
      roomHeight / 2 + roomHeight / 2, roomWidth / 2 - sceneWidth / 2, roomHeight / 2 + roomHeight / 2 };

      Core().createScene(sceneWidth, sceneHeight, scenePos);
      InnoEngine innoEngine = View().getEngine();
    innoEngine.loadScene();
    Stage stage = (Stage) width_input.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void cancelButtonAction() {
    Stage stage = (Stage) width_input.getScene().getWindow();
    Engine().deleteSelectedShape();
    stage.close();
  }

  @Override
  public void init() {
    InnoRectangle rectangle = (InnoRectangle) getIntent();

    if (rectangle == null) {
      System.out.println("Rectangle is null");
      return;

    }
  }
}