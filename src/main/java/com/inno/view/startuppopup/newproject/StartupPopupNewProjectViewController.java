/*
 * File Created: Friday, 12th October 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Saturday, 13th October 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
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

import java.util.Vector;

import com.inno.InnoViewController;
import javafx.geometry.Point2D;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

public class StartupPopupNewProjectViewController extends InnoViewController {

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

  public StartupPopupNewProjectViewController() {
  }

  public void init(Stage stage) {
  }

  @FXML
  private void initialize() {
  }

  @FXML
  private void doneButtonAction() {

    Vector<Point2D> points = new Vector<Point2D>();

    points.add(new Point2D(-1, 2));
    points.add(new Point2D(7, 5));
    points.add(new Point2D(4, 3));
    points.add(new Point2D(6, -1));
    points.add(new Point2D(3, 1));

    Point2D test = InnoCore().Utils().getCenterOfPoints(points);

    System.out.println("X => " + test.getX() + " Y => " + test.getY());

    System.out.println(projectNameInput.getText());

    InnoCore().View().showMainView();
  }

  @FXML
  private void cancelButtonAction() {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/fxml/popup.fxml"));
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