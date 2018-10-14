/*
 * File Created: Wednesday, 10th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 14th October 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.service;

import com.inno.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class View extends Application {

  private Stage _mainView;

  public View() {
  }

  @Override
  public void start(Stage mainView) throws Exception {
    _mainView = mainView;
    Core.get().setViewService(this);
    showMainView();
    // showStartupPopup();
  }

  /**
   * Open new view from fxmlFileName
   * 
   * @param view
   * @param fxmlFileName
   */
  public Stage openView(Stage view, String fxmlFileName) {
    if (view == null) {
      view = new Stage();
    }
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("/fxml/" + fxmlFileName));
      Scene scene = new Scene(fxmlLoader.load());
      view.setTitle("InnoEvent");
      view.setScene(scene);
      view.show();
    } catch (Exception e) {
      System.out.println("Error when load newView => " + e.getMessage());
    }
    return view;
  }

  /**
   * Close view passed in parameter
   * 
   * @param view
   */
  public void closeView(Stage view) {
    if (view != null) {
      view.close();
    }
  }

  /**
   * Set mainView passed in parameter
   * 
   * @param mainView
   */
  public void setMainView(Stage mainView) {
    _mainView = mainView;
  }

  /**
   * Get current mainView
   * 
   * @return
   */
  public Stage getMainView() {
    return _mainView;
  }

  public enum AnimationDirection {
    LEFT, RIGHT, TOP, BOTTOM
  }

  public void openViewWithAnimation(String fxmlFileName, AnimationDirection animationTo, AnchorPane anchorRoot) {
    StackPane parentContainer = (StackPane) anchorRoot.getScene().getRoot();
    Scene scene = parentContainer.getScene();
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();

      fxmlLoader.setLocation(getClass().getResource("/fxml/" + fxmlFileName));
      Parent newAnchor = (Parent) fxmlLoader.load();

      KeyValue kv;
      KeyValue kv2;

      switch (animationTo) {
        case LEFT:
          newAnchor.translateXProperty().set(scene.getWidth());
          kv = new KeyValue(newAnchor.translateXProperty(), 0, Interpolator.EASE_IN);
          kv2 = new KeyValue(anchorRoot.translateXProperty(), -scene.getWidth(), Interpolator.EASE_IN);
          break;
        case RIGHT:
          newAnchor.translateXProperty().set(-scene.getWidth());
          kv = new KeyValue(newAnchor.translateXProperty(), 0, Interpolator.EASE_IN);
          kv2 = new KeyValue(anchorRoot.translateXProperty(), scene.getWidth(), Interpolator.EASE_IN);
          break;
        case TOP:
          newAnchor.translateYProperty().set(scene.getHeight());
          kv = new KeyValue(newAnchor.translateYProperty(), 0, Interpolator.EASE_IN);
          kv2 = new KeyValue(anchorRoot.translateYProperty(), -scene.getHeight(), Interpolator.EASE_IN);
          break;
        case BOTTOM:
          newAnchor.translateYProperty().set(-scene.getHeight());
          kv = new KeyValue(newAnchor.translateYProperty(), 0, Interpolator.EASE_IN);
          kv2 = new KeyValue(anchorRoot.translateYProperty(), scene.getHeight(), Interpolator.EASE_IN);
          break;
        default:
          kv = new KeyValue(newAnchor.translateXProperty(), 0, Interpolator.EASE_IN);
          kv2 = new KeyValue(anchorRoot.translateYProperty(), 0, Interpolator.EASE_IN);
          break;
      }

      parentContainer.getChildren().add(newAnchor);

      Timeline timeline = new Timeline();
      Timeline timeline2 = new Timeline();
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
      System.out.println("Error with annimation " + e.getMessage());
    }
  }

  public void setSidebarFromFxmlFileName(String fxmlFileName, AnchorPane anchorPane) {
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/sidebar/" + fxmlFileName));

    try {
      Pane pane = (Pane) fxmlLoader.load();
      anchorPane.getChildren().setAll(pane.getChildren());
    } catch (Exception e) {
      System.out.println("Error when load sidebar file " + e.getMessage());
    }
  }

  public void showMainView() {
    Stage view = openView(null, "main_view.fxml");
    closeView(getMainView());
    setMainView(view);
  }

  public void showStartupPopup() {
    Stage view = openView(null, "popup.fxml");
    view.setResizable(false);
    closeView(getMainView());
    setMainView(view);
  }

  public void run(String[] args) {
    Application.launch(View.class, args);
  }
};