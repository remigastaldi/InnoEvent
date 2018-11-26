/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.engine.shape.InteractiveRectangle;

import java.util.LinkedHashMap;
import java.util.Map;

import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class RectangularSectionController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private TextField section_name_input;
  @FXML
  private TextField section_columns_input;
  @FXML
  private TextField section_rows_input;
  @FXML
  private TextField section_vital_space_width_input;
  @FXML
  private TextField section_vital_space_height_input;
  @FXML
  private TextField section_rotation_input;
  @FXML
  private Group section_rotation_group;
  @FXML
  private Circle section_rotation_circle;
  @FXML
  private AnchorPane sidebar_content;

  EventHandler<MouseEvent> _mouseDragged;

  @FXML
  private void initialize() {
  }

  public void init() {
    InteractiveRectangle rectangle = (InteractiveRectangle)getIntent();
    section_columns_input.setText("" + rectangle.getWidth());
    section_columns_input.setText("" + rectangle.getHeight());
    System.out.println(rectangle.getRotation().getAngle());
    setRotation(rectangle.getRotation().getAngle(), false);
  }


  private void setRotation(Double angle, boolean input) {
    section_rotation_group.setRotate(angle);
    if (!input) {
      section_rotation_input.setText("" + (angle));
    }
    InteractiveRectangle rectangle = (InteractiveRectangle)getIntent();
    rectangle.setRotationAngle(angle);
  }

  private void setRotation(String angle, boolean input) {
    try {
      Double nAngle = Double.parseDouble(angle);
      setRotation(nAngle, input);
    } catch(Exception e) {
      System.out.println("Given angle is not double" + e.getMessage());
    }
  }

  @FXML
  private void onMousePressed() {
    _mouseDragged = mouseEvent -> {
      double mouseX = mouseEvent.getX();
      double mouseY = mouseEvent.getY();
      Point2D pos = new Point2D(mouseX, mouseY);
      Point2D pos2 = new Point2D(section_rotation_circle.getCenterX(), section_rotation_circle.getCenterY());
      pos2 = section_rotation_group.localToScene(pos2);
      pos2 = sidebar_content.sceneToLocal(pos2);

      double angle = Math.atan2(pos.getX() - pos2.getX(), -(pos.getY() - pos2.getY())) * (180 / Math.PI);

      if (angle < 0) {
        angle = ((3600000 + angle) % 360);
      }
      
      if (angle > 0 && angle < 90) {
        angle = 360 - 90 + angle;
      } else {
        angle -= 90;
      }
      setRotation(angle, false);
    };
    sidebar_content.addEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDragged);
  }

  @FXML
  private void onMouseReleased() {
    sidebar_content.removeEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDragged);
  }

  @FXML
  private void onKeyReleased() {
    if (checkInputs(false)) {
      if (section_rotation_input.getText() != Double.toString(section_rotation_group.getRotate()))
      setRotation(section_rotation_input.getText(), true);
    }
  }

  private boolean checkInputs(boolean required) {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(section_name_input, (required == true ? "required|" : "") + "max:30");
    fields.put(section_columns_input, (required == true ? "required|" : "") + "numeric");
    fields.put(section_rows_input, (required == true ? "required|" : "") + "numeric");
    fields.put(section_vital_space_width_input, (required == true ? "required|" : "") + "numeric");
    fields.put(section_vital_space_height_input, (required == true ? "required|" : "") + "numeric");
    fields.put(section_rotation_input, (required == true ? "required|" : "") + "numeric|min:0|max:360");

    for (Map.Entry<TextField, String> entry : fields.entrySet()) {
      TextField field = entry.getKey();
      String validator = entry.getValue();
      if ((required || field.isFocused()) && !Validator.validate(field.getText(), validator)) {
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