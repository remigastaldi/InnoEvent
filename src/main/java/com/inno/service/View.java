/*
 * File Created: Wednesday, 10th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 13th October 2018
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
import javafx.scene.layout.StackPane;

public class View extends Application {

  Stage _mainView;

  public View() {

  }

  @Override
  public void start(Stage mainView) throws Exception {
    _mainView = mainView;
    Core.get().test2(this);
    FXMLLoader loader = new FXMLLoader();

    loader.setLocation(getClass().getResource("/fxml/popup.fxml"));
    StackPane root = (StackPane) loader.load();

    Scene scene = new Scene(root);
    _mainView.setScene(scene);
    _mainView.setTitle("InnoEvent");
    _mainView.show();

    System.out.println(_mainView);
  }

  private void gotoMain() {

  }

  private void gotoStartupPopup() {

  }

  public void test() {
    System.out.println(_mainView);
    try {
      _mainView.close();
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("/fxml/main_view.fxml"));
      Scene scene = new Scene(fxmlLoader.load());
      Stage stage = new Stage();
      stage.setTitle("New Window");
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      System.out.println("ERRROR =+-----++ " + e.getMessage());
    }

  }

  public void run(String[] args) {
    Application.launch(View.class, args);
  }

};