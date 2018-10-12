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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.google.common.collect.MapMaker;

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

  public class Point {
    public double x = 0;
    public double y = 0;

    // constructor
    public Point(int a, int b) {
      x = a;
      y = b;
    }
  }

  @FXML
  private void doneButtonAction() {

    Vector<Point> points = new Vector<Point>();

    points.add(new Point(-1, 2));
    points.add(new Point(7, 5));
    points.add(new Point(4, 3));
    points.add(new Point(6, -1));
    points.add(new Point(3, 1));

    Point test = getCenterOfPoints(points);

    System.out.println("X => " + test.x + " Y => " + test.y);

    System.out.println(projectNameInput.getText());
  }

  private Point getCenterOfPoints(Vector<Point> points) {
    Point center = new Point(0, 0);

    System.out.println("Size =>  " + points.size());

    double sum1 = 0;
    double sum2 = 0;
    double sum3 = 0;

    for (int i = 0; i < points.size(); i++) {
      System.out.println("I =>  " + i);

      Point point1 = points.get(i);
      Point point2;
      if (i + 1 == points.size()) {
        point2 = points.get(0);
      } else {
        point2 = points.get(i + 1);
      }

      double val1 = ((point1.x * point2.y) - (point2.x * point1.y));
      double val2 = (val1 * (point1.x + point2.x));
      double val3 = (val1 * (point1.y + point2.y));

      sum1 += val1;
      sum2 += val2;
      sum3 += val3;

      System.out.println("Val1 " + val1);
      System.out.println("Val2 " + val2);
      System.out.println("Val3 " + val3);

    }

    double air = (sum1 / 2);
    center.x = (sum2 / (6 * air));
    center.y = (sum3 / (6 * air));
    return center;
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