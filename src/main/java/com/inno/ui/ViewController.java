/*
 * File Created: Saturday, 27th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 18th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui;

import  com.inno.app.Core;

import  com.inno.ui.innoengine.InnoEngine;

import javafx.stage.Stage;

public abstract class ViewController {
  private View _view = null;
  private Object _intent = null;
  private Stage _stage = null;

  public void setView(final View view) {
    _view = view;
  }

  public void setStage(final Stage stage) {
    _stage = stage;
  }

  public Core Core() {
    return Core.get();
  }

  public View View() {
    if (_view == null) {
      System.out.println("View is not set");
    }
    return _view;
  }

  public InnoEngine Engine() {
    if (_view.getEngine() == null) {
      System.out.println("Engine is not set");
    }
    return _view.getEngine();
  }

  public void addIntent(Object intent) {
    _intent = intent;
  }

  public Object getIntent() {
    return _intent;
  }

  public Stage getStage() {
    return _stage;
  }

  public abstract void init();
};
