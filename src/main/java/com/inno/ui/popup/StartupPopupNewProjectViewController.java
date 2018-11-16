/*
 * File Created: Friday, 12th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Friday, 16th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.ui.popup;

import javafx.fxml.FXML;

import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.util.Vector;

import com.inno.ui.ViewController;
import com.inno.ui.View.AnimationDirection;

import javafx.geometry.Point2D;


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
  private TextField vital_space_input;

  public void init() {
  }

  @FXML
  private void initialize() {
  }

  @FXML
  private void doneButtonAction() {
    System.out.println(project_name_input.getText());

    Double roomWidth = Double.parseDouble(room_width_input.getText());
    Double roomHeight = Double.parseDouble(room_height_input.getText());
    Double sceneWidth = Double.parseDouble(scene_width_input.getText());
    Double sceneHeight = Double.parseDouble(scene_height_input.getText());

    System.out.println(roomWidth);
    double[] scenePos = { roomWidth / 2 - sceneWidth / 2,
                          roomHeight / 2 - sceneHeight / 2,
                          roomWidth / 2 + sceneWidth / 2,
                          roomHeight / 2 - sceneHeight / 2,
                          roomWidth / 2 + sceneWidth / 2,
                          roomHeight / 2 + roomHeight / 2,
                          roomWidth / 2 - sceneWidth / 2,
                          roomHeight / 2 + roomHeight / 2 };

    Core().createRoom(project_name_input.getText(), roomWidth, roomHeight);
    
    Core().createScene(sceneWidth, sceneHeight, scenePos);
    
      // Core().setVitalS(Integer.parseInt(room_height_input.getText()));

    View().showMainView();
  }

  @FXML
  private void cancelButtonAction() {
    View().openViewWithAnimation("popup.fxml", AnimationDirection.RIGHT, anchor_root);
  }
}