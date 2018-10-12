/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.view.startuppopup.main;

import java.io.File;

import javafx.fxml.FXML;

import javafx.stage.FileChooser;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

public class StartupPopupMainViewController {

  @FXML
  private Button createNewProjectButton;
  @FXML
  private AnchorPane anchorRoot;
  @FXML
  private StackPane parentContainer;

  public StartupPopupMainViewController() {
  }

  @FXML
  private void initialize() {
  }

  @FXML
  private void openProjectButtonAction() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("InnoEvent", "*.inevt"));
    File file = fileChooser.showOpenDialog(null); //ToDO add current stage 
    if (file != null) {
      System.out.println("OK");
      System.out.println(file);
    }

  }

  @FXML
  private void createNewProjectButtonAction() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/fxml/popup_new_project.fxml"));
      Scene scene = createNewProjectButton.getScene();

      root.translateXProperty().set(scene.getWidth());
      parentContainer.getChildren().add(root);

      Timeline timeline = new Timeline();
      Timeline timeline2 = new Timeline();
      KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
      KeyValue kv2 = new KeyValue(anchorRoot.translateXProperty(), -600 , Interpolator.EASE_IN);
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
      System.out.println("ERROR"+ e.getMessage());
    }
  }
}