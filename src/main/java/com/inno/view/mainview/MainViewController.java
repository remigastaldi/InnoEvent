/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 13th October 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.view.mainview;

import com.inno.InnoEngine;
import com.inno.InnoViewController;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;




import javafx.fxml.FXMLLoader;


public class MainViewController extends InnoViewController {
  @FXML
  private Canvas canvas;

  @FXML
  private AnchorPane sidebarAnchor;

  public MainViewController() {
  }

  @FXML
	private void initialize() {
    InnoCore().setEngine(new InnoEngine(canvas));
    InnoCore().View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);
  }

  @FXML
  private void quitButtonAction() {
    InnoCore().View().showStartupPopup();
  }

  @FXML 
  private void testBtn() {
    System.out.println("ok");
    InnoCore().View().setSidebarFromFxmlFileName("sidebar_section.fxml", sidebarAnchor);
  }
}