/*
 * File Created: Saturday, 27th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 27th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui;

import  com.inno.app.Core;

import  com.inno.ui.innoengine.InnoEngine;

public abstract class ViewController {
  private View _view = null;

  public void setView(final View view) {
    _view = view;
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

  public abstract void init();
};
