/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.engine.shape.InteractiveRectangle;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class SceneController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private TextField scene_width_input;
  @FXML
  private TextField scene_height_input;
  @FXML
  private TextField scene_rotation_input;
  @FXML
  private TextField scene_elevation_input;

  @FXML
  private Group scene_rotation_group;
  @FXML
  private Circle scene_rotation_circle;
  @FXML
  private AnchorPane sidebar_content;

  EventHandler<MouseEvent> _mouseDragged;

  @FXML
  private void initialize() {
  }

  public void init() {
    InnoRectangle rectangle = (InnoRectangle) getIntent();

    if (rectangle == null) {
      System.out.println("Rectangle is null");
      return;
    }
    rectangle.widthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (!scene_width_input.getText().equals(Double.toString(rectangle.getWidthToMetter()))) {
        scene_width_input.setText(Double.toString(rectangle.getWidthToMetter()));
        checkInputs();
      }
    });
    rectangle.heightProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (!scene_height_input.getText().equals(Double.toString(rectangle.getHeightToMetter()))) {
        scene_height_input.setText(Double.toString(rectangle.getHeightToMetter()));
        checkInputs();
      }
    });

    scene_height_input.setText(Double.toString(rectangle.getHeightToMetter()));
    scene_width_input.setText(Double.toString(rectangle.getWidthToMetter()));
    scene_rotation_input.setText(Double.toString(Core().getImmutableRoom().getImmutableScene().getRotation()));
    scene_elevation_input.setText(Double.toString(Core().getImmutableRoom().getImmutableScene().getElevation()));
  }

  private void setRotation(double angle, boolean input) {
    scene_rotation_group.setRotate(angle);
    if (!input) {
      scene_rotation_input.setText("" + (angle));
    }
    InteractiveRectangle rectangle = (InteractiveRectangle) getIntent();

    Core().setSceneRotation(angle);
    rectangle.setRotationAngle(Core().getImmutableRoom().getImmutableScene().getRotation());
  }

  @FXML
  private void onMousePressed() {
    _mouseDragged = mouseEvent -> {
      double mouseX = mouseEvent.getX();
      double mouseY = mouseEvent.getY();
      Point2D pos = new Point2D(mouseX, mouseY);
      Point2D pos2 = new Point2D(scene_rotation_circle.getCenterX(), scene_rotation_circle.getCenterY());
      //pos2 = scene_rotation_group.localToScene(pos2);
      //pos2 = sidebar_content.sceneToLocal(pos2);

      //double angle = Math.atan2(pos.getX() - pos2.getX(), -(pos.getY() - pos2.getY())) * (180 / Math.PI);

      //System.out.println(angle);
      // angle = convertTo360(angle);
      //setRotation(angle, false);
    };
    sidebar_content.addEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDragged);
  }

  @FXML
  private void onMouseReleased() {
    sidebar_content.removeEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDragged);
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
        if (scene_width_input.isFocused()) {
          rectangle.setWidth(Double.parseDouble(scene_width_input.getText()));
          Core().setSceneWidth(Double.parseDouble(scene_width_input.getText()));
        }
        if (scene_height_input.isFocused()) {
          rectangle.setHeight(Double.parseDouble(scene_height_input.getText()));
          Core().setSceneHeight(Double.parseDouble(scene_height_input.getText()));
        }
        if (scene_elevation_input.isFocused())
          Core().setSceneElevation(Double.parseDouble(scene_elevation_input.getText()));
        if (scene_rotation_input.isFocused()) {
          setRotation(Double.parseDouble(scene_rotation_input.getText()), false);
          Core().setSceneRotation(Double.parseDouble(scene_rotation_input.getText()));
        }
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
    fields.put(scene_elevation_input, "required|numeric");
    fields.put(scene_rotation_input, "required|numeric|min:0|max:360");

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