/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 3rd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import java.util.ArrayList;

import com.inno.ui.ViewController;
import com.inno.ui.engine.Engine;
import com.inno.ui.engine.shape.InteractivePolygon;
import com.inno.ui.engine.shape.InteractiveShape;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

public class MainViewController extends ViewController {
  // @FXML
  // private Pane graphicsPane;

  @FXML
  private AnchorPane top_bar;

  @FXML
  private AnchorPane sidebarAnchor;

  // @FXML
  private ScrollPane scrollPane;

  // @FXML
  // private AnchorPane anchorCanvas;

  @FXML
  private StackPane stackPane;

  // @FXML
  // private StackPane paneParent;

  
  @FXML
  private void initialize() {
  }
  
  // Pane _pane =  new Pane();
  Pane _pane = null;

  public void init() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);

    _pane = new Pane();

    _pane.setPrefSize(1000, 1000);
    // _pane.setOnDragDetected(evt -> {
    //     Node target = (Node) evt.getTarget();
    //     while (target != _pane && target != null) {
    //         target = target.getParent();
    //     }
    //     if (target != null) {
    //       target.startFullDrag();
    //     }
    //   });
    // _pane.setStyle("-fx-background-color: #777A81");

    Group group = new Group(_pane);
    StackPane content = new StackPane(group);
    content.setStyle("-fx-background-color: #1E1E1E");

    group.layoutBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
      // keep it at least as large as the content
      content.setMinWidth(newBounds.getWidth());
      content.setMinHeight(newBounds.getHeight());
    });

    scrollPane = new ScrollPane(content);
    scrollPane.setStyle("-fx-background-color: #1E1E1E");


    // scrollPane.setPannable(true);
    scrollPane.viewportBoundsProperty().addListener((observable, oldBounds, newBounds) -> {
        // use vieport size, if not too small for zoomTarget
        content.setPrefSize(newBounds.getWidth(), newBounds.getHeight());
    });

    content.setOnScroll(evt -> {
      if (evt.isControlDown()) {
          evt.consume();

          final double zoomFactor = evt.getDeltaY() > 0 ? 1.2 : 1 / 1.2;

          Bounds groupBounds = group.getLayoutBounds();
          final Bounds viewportBounds = scrollPane.getViewportBounds();

          // calculate pixel offsets from [0, 1] range
          double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
          double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

          // convert content coordinates to zoomTarget coordinates
          Point2D posInZoomTarget = _pane.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));

          // calculate adjustment of scroll position (pixels)
          Point2D adjustment = _pane.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

          // do the resizing
          _pane.setScaleX(zoomFactor * _pane.getScaleX());
          _pane.setScaleY(zoomFactor * _pane.getScaleY());

          // refresh ScrollPane scroll positions & content bounds
          scrollPane.layout();

          // convert back to [0, 1] range
          // (too large/small values are automatically corrected by ScrollPane)
          groupBounds = group.getLayoutBounds();
          scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
          scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
      }
    });
    StackPane sp = new StackPane();
    System.out.println(stackPane.getPrefWidth() + " : " + stackPane.getPrefHeight());
    sp.setPrefSize(928, 600);
    // Pane test = new Pane();
    // test.setPrefSize(8000, 600);
    // test.setStyle("-fx-background-color: #777A81");
    sp.getChildren().add(scrollPane);

    stackPane.getChildren().add(sp);

    View().createEngine(_pane);
    Engine().scrlPane = scrollPane;
  }

  @FXML
  private void  keyAction(KeyEvent evt) {
    System.out.println("Add section");
    if (evt.getText().compareTo("a") == 0)
      Engine().test();
    // System.out.println("++++ " + scrollPane.viewportBoundsProperty());
      // System.out.println(event.getKeyChar());
  }

  @FXML
  private void quitButtonAction() {
    View().showStartupPopup();
  }

  @FXML
  private void openRoom() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);
  }

  @FXML
  private void openSection() {
    View().setSidebarFromFxmlFileName("sidebar_section.fxml", sidebarAnchor);
  }
}