/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.shape.InnoRectangle;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

import javafx.fxml.FXML;


import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.NumberStringConverter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

public class SceneController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private TextField scene_width_input;
  @FXML
  private TextField scene_height_input;
  @FXML
  private TextField scene_rotation_input;

  private SimpleDoubleProperty widthInput = new SimpleDoubleProperty();
  private SimpleDoubleProperty heightInput = new SimpleDoubleProperty();
  private SimpleDoubleProperty rotationInput = new SimpleDoubleProperty();
  

  @FXML
  private void initialize() {
  }

  public void init() {
    InnoRectangle rectangle = (InnoRectangle) getIntent();

    scene_width_input.textProperty().bindBidirectional(widthInput, new NumberStringConverter());
    scene_height_input.textProperty().bindBidirectional(heightInput, new NumberStringConverter());
    scene_rotation_input.textProperty().bindBidirectional(rotationInput, new NumberStringConverter());
    widthInput.set(rectangle.getWidth());
    heightInput.set(rectangle.getHeight());
    rotationInput.set(rectangle.getRotation().getAngle());

    rectangle.getWidthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (widthInput.get() != rectangle.getWidth())
        widthInput.set(rectangle.getWidth());
    });
    rectangle.getHeightProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (heightInput.get() != rectangle.getHeight())
        heightInput.set(rectangle.getHeight());
    });
    widthInput.addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (checkInputs()) {
        if (rectangle.getWidth() != newX.intValue())
          rectangle.setWidth(newX.intValue());
      }

    });
    heightInput.addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (checkInputs()) {
        if (rectangle.getHeight() != newY.intValue())
          rectangle.setHeight(newY.intValue());
      }
    });

  }

  @FXML
  private void onKeyReleased() {
    if (checkInputs()) {
      InnoRectangle rectangle = (InnoRectangle) getIntent();

      if (rectangle == null) {
        System.out.println("Rectangle is null");
        return;
      }
      try {
        if (scene_width_input.isFocused())
          rectangle.setWidth(Integer.parseInt(scene_width_input.getText()));
        if (scene_height_input.isFocused())
          rectangle.setHeight(Integer.parseInt(scene_height_input.getText()));
        if (scene_rotation_input.isFocused())
          rectangle.setRotationAngle(Double.parseDouble(scene_rotation_input.getText()));
        // }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private boolean checkInputs() {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(scene_width_input, "required|numeric|min:1");
    fields.put(scene_height_input, "required|numeric|min:1");
    fields.put(scene_rotation_input, "required|numeric|min:0|max:360");

    for (Map.Entry<TextField, String> entry : fields.entrySet()) {
      TextField field = entry.getKey();
      String validator = entry.getValue();
      if ((field.isFocused()) && !Validator.validate(field.getText(), validator)) {
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
}