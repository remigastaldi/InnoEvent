/*
 * File Created: Wednesday, 10th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.service;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Platform;

public class View extends Application {

  Stage mainView;

  public View() {

  }

  @Override
  public void start(Stage _mainView) throws IOException {
    this.mainView = _mainView;
    gotoStartupPopup();
    mainView.show();
  }

  private void gotoMain() {
    try {
      this.replaceSceneContent("main_view.fxml");
    } catch (Exception ex) {
    }
  }

  private void gotoStartupPopup() {
    try {
      this.replaceSceneContent("popup.fxml");
    } catch (Exception ex) {
    }
  }

  private Parent replaceSceneContent(String fxml) throws Exception {
    Parent page = (Parent) FXMLLoader.load(getClass().getResource("/fxml/" + fxml), null);
    Scene scene = mainView.getScene();
    if (scene == null) {
      scene = new Scene(page);
      mainView.setScene(scene);
    } else {
      mainView.getScene().setRoot(page);
    }
    mainView.sizeToScene();
    return page;
  }

  
  private void openStartupPopUp() {
    gotoMain();
  }

  public void test() {
    gotoMain();
  }

  public void run(String[] args) {
    Application.launch(args);
  }

};