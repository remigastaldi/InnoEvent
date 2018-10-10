/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 10th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.view.startuppopup.newproject;

import javafx.fxml.FXML;

import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;

import javafx.util.Duration;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

public class StartupPopUpNewProjectViewController {

  @FXML
  private Button cancelButton;
  @FXML
  private Button doneButton;
  @FXML
  private AnchorPane anchorRoot;

  @FXML
  private TextField projectNameInput;
  @FXML
  private TextField roomHeightInput;
  @FXML
  private TextField roomWidthInput;
  @FXML
  private TextField sceneHeightInput;
  @FXML
  private TextField sceneWidthInput;
  @FXML
  private TextField vitalSpaceInput;


  public StartupPopUpNewProjectViewController() {
  }

  public void init(Stage stage) {
  }

  @FXML
  private void initialize() {
  }

  @FXML
  private void doneButtonAction() {
    System.out.println(projectNameInput.getText());
  }

  @FXML
  private void cancelButtonAction() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/fxml/popUp.fxml"));
      StackPane parentContainer = (StackPane) cancelButton.getScene().getRoot();

      root.translateXProperty().set(-600);

      parentContainer.getChildren().add(root);

      Timeline timeline = new Timeline();
      Timeline timeline2 = new Timeline();
      KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
      KeyValue kv2 = new KeyValue(anchorRoot.translateXProperty(), 600, Interpolator.EASE_IN);
      KeyFrame kf = new KeyFrame(Duration.seconds(0.2), kv);
      KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);

      timeline2.getKeyFrames().add(kf2);
      timeline.getKeyFrames().add(kf);
      timeline.setOnFinished(t -> {
        parentContainer.getChildren().remove(anchorRoot);
      });

      timeline.play();
      timeline2.play();
    } catch (Exception e) {
      System.out.println("ERROR in annimation" + e.getMessage());
    }
  }
}