/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 10th October 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.view.mainview;

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