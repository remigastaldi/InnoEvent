/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 4th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.mainview;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

public class MainViewController {
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