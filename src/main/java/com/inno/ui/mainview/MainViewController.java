/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Wednesday, 28th November 2018
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
import com.inno.service.pricing.ImmutablePlaceRate;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.InnoEngine;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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
  private SplitPane mainSplitPane;

  Node componentsPane = null;

  private boolean _sidebarToggled = false;
  private boolean _sidebarAnimation = false;

  @FXML
  private void initialize() {
  }

  public void init() {
    View().setSidebar(sidebar_anchor);
    View().setSidebarFromFxmlFileName("sidebar_room.fxml");

    // double[] pos = new double[] { 10, 10, 50, 10, 100, 100, 10, 50 };
    // ImmutableSittingSection sittingSection = Core().createSittingSection(pos,
    // 0.0);
    // ImmutableSittingRow sittingRow =
    // Core().createSittingRow(sittingSection.getIdSection(), pos, pos);
    // ImmutableSeat sittingSeat = Core().createSeat(sittingSection.getIdSection(),
    // sittingRow.getIdRow(), pos);
    // ImmutableSeat sittingSeat2 = Core().createSeat(sittingSection.getIdSection(),
    // sittingRow.getIdRow(), pos);
    // ImmutableSeat sittingSeat3 = Core().createSeat(sittingSection.getIdSection(),
    // sittingRow.getIdRow(), pos);
    // ImmutableSeat sittingSeat4 = Core().createSeat(sittingSection.getIdSection(),
    // sittingRow.getIdRow(), pos);
    // ImmutableSeat sittingSeat5 = Core().createSeat(sittingSection.getIdSection(),
    // sittingRow.getIdRow(), pos);

    // Core().setSectionPrice(sittingSection.getIdSection(), 50);

    // HashMap<String, ? extends ImmutablePlaceRate> places = Core().getPrices();

    // for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
    // String key = entry.getKey();
    // ImmutablePlaceRate value = entry.getValue();
    // System.out.println("price => " + key + " set to " + value.getPrice());
    // }

    // Core().setRowPrice(sittingSection.getIdSection(), sittingRow.getIdRow(), 20);

    // for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
    // String key = entry.getKey();
    // ImmutablePlaceRate value = entry.getValue();
    // System.out.println("price => " + key + " set to " + value.getPrice());
    // }

    // Core().setSeatPrice(sittingSection.getIdSection(), sittingRow.getIdRow(),
    // Integer.toString(sittingSeat4.getId()), 10);

    // for (Map.Entry<String, ? extends ImmutablePlaceRate> entry : places.entrySet()) {
    // String key = entry.getKey();
    // ImmutablePlaceRate value = entry.getValue();
    // System.out.println("price => " + key + " set to " + value.getPrice());
    // }

    View().createEngine(stack_pane);

    componentsPane = mainSplitPane.getItems().get(1);

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
  private void offerManagerAction() {
    View().openPopup("offer_manager.fxml");
  }

  @FXML
  private void toggleSidebarAction() {
    if (_sidebarAnimation) {
      return;
    }
    _sidebarAnimation = true;
    double pos = 0;
    Timeline timeline = new Timeline();
    if (_sidebarToggled == false) {
      sidebar_anchor.setMinWidth(0);
      pos = 2;
      timeline.setOnFinished(t -> {
        mainSplitPane.getItems().remove(componentsPane);
        _sidebarToggled = !_sidebarToggled;
        _sidebarAnimation = false;
      });
    } else {
      pos = 0.8;
      mainSplitPane.getItems().add(1, componentsPane);
      mainSplitPane.setDividerPositions(2);
      timeline.setOnFinished(t -> {
        sidebar_anchor.setMinWidth(250);
        _sidebarToggled = !_sidebarToggled;
        _sidebarAnimation = false;
      });
    }
    KeyValue kv = new KeyValue(mainSplitPane.getDividers().get(0).positionProperty(), pos, Interpolator.EASE_IN);
    KeyFrame kf = new KeyFrame(Duration.seconds(0.2), kv);
    timeline.getKeyFrames().add(kf);
    timeline.play();
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