/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 15th December 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import java.io.File;

import com.inno.app.Core;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.InnoEngine;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.input.KeyCodeCombination;


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
  @FXML
  private ImageView magnet_icon;
  @FXML
  private Button magnet_button;

  Node componentsPane = null;

  private boolean _sidebarToggled = false;
  private boolean _sidebarAnimation = false;

  @FXML
  private void initialize() {
  }

  public void init() {
    View().setSidebar(sidebar_anchor);

    View().createEngine(stack_pane);
    View().setSidebarFromFxmlFileName("sidebar_room.fxml");

    componentsPane = mainSplitPane.getItems().get(1);

  }

  final KeyCombination controlC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
  final KeyCombination controlV = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
  final KeyCombination controlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
  final KeyCombination controlL = new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);

  @FXML
  private void keyAction(KeyEvent evt) {
    switch (evt.getText()) {
    case "a":
      Engine().createIrregularSection();
      break;
    case "r":
      Engine().createRectangularSection();
      break;
    }

    if (evt.getCode() == KeyCode.DELETE)
      Engine().deleteSelectedShape();
    if (controlC.match(evt))
      Engine().copySelectedSectionsToDomainBuffer();
    if (controlV.match(evt))
      Engine().pastBufferToEngine();
    if (controlZ.match(evt))
      Core.get().undo();
    if (controlL.match(evt))
      Core.get().redo();
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
    File file = View().getSaveProjectFilePath("*.inevt");
    if (file != null) {
      Core().saveTo(file.getAbsolutePath());
    }
  }

  @FXML
  private void menuExportAsJsonAction() {
    File file = View().getSaveProjectFilePath("*.json");
    if (file != null) {
      Core().exportAsJson(file.getAbsolutePath());
    }
  }

  @FXML
  private void menuZoomInAction() {
    Engine().zoom(1.2);
  }

  @FXML
  private void menuZoomOutAction() {
    Engine().zoom(0.83);
  }

  @FXML
  private void menuDrawRectangularSectionAction() {
    Engine().createRectangularSection();
  }

  @FXML
  private void menuDrawIrregularSectionAction() {
    Engine().createIrregularSection();
  }

  @FXML
  private void menuCreateSceneAction() {
    if (Core().getImmutableRoom().getImmutableScene() == null) {
      InnoEngine innoEngine = View().getEngine();
      innoEngine.getView().openPopup("new_scene.fxml");
    }
  }

  @FXML
  private void toggleMagnetismAction() {
    magnet_icon.setImage(new Image("icon/magnet_selected.png"));
    magnet_icon.setImage(new Image("icon/magnet.png"));
    Engine().toggleMagnetism();
  }

  @FXML
  private void menuUndoAction() {
    Core.get().undo();
  }

  @FXML
  private void menuRedoAction() {
    Core.get().redo();
  }

}