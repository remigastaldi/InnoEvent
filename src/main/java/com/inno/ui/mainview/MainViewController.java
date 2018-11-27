/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 27th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.inno.app.room.ImmutableSeat;
import com.inno.app.room.ImmutableSittingRow;
import com.inno.app.room.ImmutableSittingSection;
import com.inno.service.pricing.PlaceRate;
import com.inno.service.pricing.PlaceRateData;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.InnoEngine;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MainViewController extends ViewController {
  @FXML
  private AnchorPane top_bar;
  @FXML
  private AnchorPane sidebar_anchor;
  @FXML
  private AnchorPane anchor_canvas;
  @FXML
  private StackPane stack_pane;

  @FXML
  private void initialize() {
  }

  public void init() {
    View().setSidebar(sidebar_anchor);
    View().setSidebarFromFxmlFileName("sidebar_room.fxml");

    // double[] pos = new double[] { 10, 10, 50, 10, 100, 100, 10, 50 };
    // ImmutableSittingSection sittingSection = Core().createSittingSection(pos, 0.0);
    // ImmutableSittingRow sittingRow = Core().createSittingRow(sittingSection.getIdSection(), pos, pos);
    // ImmutableSeat sittingSeat = Core().createSeat(sittingSection.getIdSection(), sittingRow.getIdRow(), pos);
    // ImmutableSeat sittingSeat2 = Core().createSeat(sittingSection.getIdSection(), sittingRow.getIdRow(), pos);
    // ImmutableSeat sittingSeat3 = Core().createSeat(sittingSection.getIdSection(), sittingRow.getIdRow(), pos);
    // ImmutableSeat sittingSeat4 = Core().createSeat(sittingSection.getIdSection(), sittingRow.getIdRow(), pos);
    // ImmutableSeat sittingSeat5 = Core().createSeat(sittingSection.getIdSection(), sittingRow.getIdRow(), pos);

    // Core().setSectionPrice(sittingSection.getIdSection(), 50);

    // HashMap<String, ? extends PlaceRateData> places = Core().getPrices();

    // for (Map.Entry<String, ? extends PlaceRateData> entry : places.entrySet()) {
    //   String key = entry.getKey();
    //   PlaceRateData value = entry.getValue();
    //   System.out.println("price => " + key + " set to " + value.getPrice());
    // }

    // Core().setRowPrice(sittingSection.getIdSection(), sittingRow.getIdRow(), 20);

    // for (Map.Entry<String, ? extends PlaceRateData> entry : places.entrySet()) {
    //   String key = entry.getKey();
    //   PlaceRateData value = entry.getValue();
    //   System.out.println("price => " + key + " set to " + value.getPrice());
    // }

    // Core().setSeatPrice(sittingSection.getIdSection(), sittingRow.getIdRow(), Integer.toString(sittingSeat4.getId()), 10);

    // for (Map.Entry<String, ? extends PlaceRateData> entry : places.entrySet()) {
    //   String key = entry.getKey();
    //   PlaceRateData value = entry.getValue();
    //   System.out.println("price => " + key + " set to " + value.getPrice());
    // }


    View().createEngine(stack_pane);

  }

  @FXML
  private void keyAction(KeyEvent evt) {
    switch (evt.getText()) {
    case "a":
      Engine().createIrregularSection();
      break;
    case "r":
      Engine().createRectangularSection();
    }

    if (evt.getCode() == KeyCode.DELETE)
      Engine().deleteSelectedShape();
  }

  @FXML
  private void menuQuitButtonAction() {
    Core().closeProject();
    View().showStartupPopup();
  }

  @FXML
  private void menuOpenProjectAction() {
    File file = View().getProjectFilePath();
    if (file != null) {
      Core().loadProject(file.getAbsolutePath());
      View().showMainView();
    }
  }

  @FXML
  private void menuSaveAction() {
    if (Core().save() == false) {
      menuSaveAsAction();
    }
  }

  @FXML
  private void menuSaveAsAction() {
    File file = View().getSaveProjectFilePath();
    if (file != null) {
      Core().saveTo(file.getAbsolutePath());
    }
  }

  @FXML
  private void menuCreateSceneAction() {
    if (Core().getImmutableRoom().getImmutableScene() == null) {
      InnoEngine innoEngine = View().getEngine();
      innoEngine.getView().openPopup("new_scene.fxml");
    }
  }
}