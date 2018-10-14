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

public class View extends Application {

  Stage _mainView;

  public View() {
  }

  @Override
  public void start(Stage mainView) throws Exception {
    _mainView = mainView;
    Core.get().setViewService(this);
    showStartupPopup();
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
   * @return
   */
  public Stage getMainView() {
    return _mainView;
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