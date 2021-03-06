/*
 * File Created: Wednesday, 10th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 18th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui;

import java.io.File;

import com.inno.app.Core;
import com.inno.ui.innoengine.InnoEngine;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class View extends Application {

  private Stage _mainView;
  private AnchorPane _sidebarAnchor;
  private InnoEngine _engine = null;

  public View() {
  }

  @Override
  public void start(Stage mainView) throws Exception {
    _mainView = mainView;
    showStartupPopup();
  }

  /**
   * Open new view from fxmlFileName
   * 
   * @param view
   * @param fxmlFileName
   */
  public Stage openView(Stage view, String fxmlFileName, Object intent) {
    if (view == null) {
      view = new Stage();
    }
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFileName));

      Scene scene = new Scene(fxmlLoader.load());
      ViewController viewController = fxmlLoader.<ViewController>getController();
      viewController.addIntent(intent);
      viewController.setStage(view);
      viewController.setView(this);
      viewController.init();

      view.setTitle("InnoEvent");
      view.setScene(scene);
      view.show();
    } catch (Exception e) {
      System.out.println("Error when load new view => " + e.getMessage());
    }
    return view;
  }

  public Stage openView(Stage view, String fxmlFileName) {
    return openView(view, fxmlFileName, null);
  }

  public void openPopup(String fxmlFileName, Object intent) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/popup/" + fxmlFileName));
      Scene scene = new Scene(loader.load());
      Stage stage = new Stage();
      ViewController view = loader.getController();
      view.setStage(stage);
      view.setView(this);
      view.addIntent(intent);
      view.init();
      stage.setResizable(false);
      stage.setScene(scene);
      stage.setAlwaysOnTop(true);
      stage.show();
    } catch (Exception e) {
      System.out.println("Error with open popup => " + e.getMessage());
    }
  }

  public void openPopup(String fxmFileName) {
    openPopup(fxmFileName, null);
  }

  /**
   * Close view passed in parameter
   * 
   * @param view
   */
  public void closeView(Stage view) {
    if (view != null) {
      view.close();
      view = null;
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
   * Set sidebarAnchor passed in parameter
   * 
   * @param sidebarAnchor
   */
  public void setSidebar(AnchorPane sidebarAnchor) {
    _sidebarAnchor = sidebarAnchor;
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

  public void openViewWithAnimation(String fxmlFileName, AnimationDirection animationTo, AnchorPane anchor_root) {
    this.openViewWithAnimation(fxmlFileName, animationTo, anchor_root, null);
  }

  public void openViewWithAnimation(String fxmlFileName, AnimationDirection animationTo, AnchorPane anchor_root,
      Object intent) {
    StackPane parentContainer = (StackPane) anchor_root.getScene().getRoot();
    Stage stage = (Stage) anchor_root.getScene().getWindow();
    Scene scene = parentContainer.getScene();
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFileName));
      Parent newAnchor = (Parent) fxmlLoader.load();

      ViewController view = fxmlLoader.getController();
      view.setStage(stage);
      view.setView(this);
      view.addIntent(intent);
      view.init();

      KeyValue kv;
      KeyValue kv2;

      switch (animationTo) {
      case LEFT:
        newAnchor.translateXProperty().set(scene.getWidth());
        kv = new KeyValue(newAnchor.translateXProperty(), 0, Interpolator.EASE_IN);
        kv2 = new KeyValue(anchor_root.translateXProperty(), -scene.getWidth(), Interpolator.EASE_IN);
        break;
      case RIGHT:
        newAnchor.translateXProperty().set(-scene.getWidth());
        kv = new KeyValue(newAnchor.translateXProperty(), 0, Interpolator.EASE_IN);
        kv2 = new KeyValue(anchor_root.translateXProperty(), scene.getWidth(), Interpolator.EASE_IN);
        break;
      case TOP:
        newAnchor.translateYProperty().set(scene.getHeight());
        kv = new KeyValue(newAnchor.translateYProperty(), 0, Interpolator.EASE_IN);
        kv2 = new KeyValue(anchor_root.translateYProperty(), -scene.getHeight(), Interpolator.EASE_IN);
        break;
      case BOTTOM:
        newAnchor.translateYProperty().set(-scene.getHeight());
        kv = new KeyValue(newAnchor.translateYProperty(), 0, Interpolator.EASE_IN);
        kv2 = new KeyValue(anchor_root.translateYProperty(), scene.getHeight(), Interpolator.EASE_IN);
        break;
      default:
        kv = new KeyValue(newAnchor.translateXProperty(), 0, Interpolator.EASE_IN);
        kv2 = new KeyValue(anchor_root.translateYProperty(), 0, Interpolator.EASE_IN);
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
        parentContainer.getChildren().remove(anchor_root);
      });

      timeline.play();
      timeline2.play();
    } catch (Exception e) {
      System.out.println("Error with annimation " + e.getMessage());
    }
  }

  public void setSidebarFromFxmlFileName(String fxmlFileName, Object object) {
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("/fxml/sidebar/" + fxmlFileName));

    try {
      Pane pane = (Pane) fxmlLoader.load();
      ViewController viewController = fxmlLoader.<ViewController>getController();
      viewController.setStage(_mainView);
      viewController.setView(this);
      viewController.addIntent(object);
      viewController.init();
      _sidebarAnchor.getChildren().setAll(pane.getChildren());
    } catch (Exception e) {
      System.out.println("Error when load sidebar file " + e.getMessage());
    }
  }

  public void setSidebarFromFxmlFileName(String fxmlFileName) {
    setSidebarFromFxmlFileName(fxmlFileName, null);
  }

  public File getProjectFilePath() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("InnoEvent", "*.inevt"));
    File file = fileChooser.showOpenDialog(getMainView());
    return file;
  }

  public File getSaveProjectFilePath(String extension, String name) {
    FileChooser fileChooser = new FileChooser();
    if (name != null) {
      fileChooser.setInitialFileName(name);
    }
    // Set extension filter for text files
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("InnoEvent", extension);
    fileChooser.getExtensionFilters().add(extFilter);

    // Show save file dialog
    File file = fileChooser.showSaveDialog(_mainView);
    return file;
  }

  public File getSaveProjectFilePath(String extension) {
    return getSaveProjectFilePath(extension, null);
  }

  public void showMainView() {
    Stage view = openView(null, "main_view.fxml");
    closeView(getMainView());
    setMainView(view);
  }

  public void showStartupPopup() {
    Stage view = openView(null, "popup.fxml", null);
    view.setResizable(false);
    closeView(getMainView());
    setMainView(view);
  }

  public void showStartupPopupNewProject() {
    Stage view = openView(null, "popup.fxml", "new");
    view.setResizable(false);
    closeView(getMainView());
    setMainView(view);
  }

  public void run(String[] args) {
    Application.launch(View.class, args);
  }

  public void createEngine(StackPane stackPane) {
    _engine = new InnoEngine(this, stackPane);
    Core.get().setEngine(_engine);
  }

  public InnoEngine getEngine() {
    return _engine;
  }
};