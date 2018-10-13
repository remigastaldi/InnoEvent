/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Friday, 12th October 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.view.mainview;

import com.inno.InnoViewController;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

public class MainViewController extends InnoViewController{
  @FXML
  private Button testButton;

  public MainViewController() {
  }

	@FXML
	private void initialize() {
  }
  
  @FXML
  private void testButtonMethod() {
    System.out.println("===============");
  }
}