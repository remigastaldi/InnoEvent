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

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.util.Vector;

import com.inno.InnoViewController;
import com.inno.service.View.AnimationDirection;

import javafx.geometry.Point2D;


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
    InnoCore().View().openViewWithAnimation("popup.fxml", AnimationDirection.RIGHT, anchorRoot);
  }
}