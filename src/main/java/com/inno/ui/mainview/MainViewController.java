/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 17th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import com.inno.app.room.ImmutableRoom;
import com.inno.app.room.ImmutableScene;
import com.inno.ui.ViewController;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

public class MainViewController extends ViewController {
  @FXML
  private AnchorPane top_bar;
  @FXML
  private AnchorPane sidebar_anchor;
  @FXML
  private AnchorPane anchor_canvas;
  @FXML
  private StackPane stack_pane;
  
  private ScrollPane scrollPane;

  @FXML
  private void initialize() {
  }
  
  public void init() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebar_anchor);

    Pane _pane = new Pane();
    ImmutableRoom roomData = Core().getImmutableRoom();
    ImmutableScene sceneData = Core().getImmutableScene();

    Rectangle scene = new Rectangle(sceneData.getPositions()[0], sceneData.getPositions()[1],
                                    sceneData.getWidth(), sceneData.getHeight());
    scene.setFill(Color.CHARTREUSE);
    scene.setOpacity(0.8);
    _pane.getChildren().add(scene);
    _pane.setPrefSize(roomData.getWidth(), roomData.getHeight());

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

          Point2D scrollOffset = figureScrollOffset(group, scrollPane);

          // do the resizing
          _pane.setScaleX(zoomFactor * _pane.getScaleX());
          _pane.setScaleY(zoomFactor * _pane.getScaleY());

          // refresh ScrollPane scroll positions & content bounds
          scrollPane.layout();

          repositionScroller(group, scrollPane, zoomFactor, scrollOffset);
      }
    });
    stack_pane.getChildren().add(scrollPane);
    View().createEngine(_pane);
  }

  @FXML
  private void  keyAction(KeyEvent evt) {
    if (evt.getText().compareTo("a") == 0)
      Engine().createIrregularSection();
    else if (evt.getText().compareTo("s") == 0)
      Engine().createRectangularSection();
    else if (evt.getCode() == KeyCode.DELETE)
      Engine().deleteSelectedShape();
  }

  @FXML
  private void quitButtonAction() {
    View().showStartupPopup();
  }

  @FXML
  private void openRoom() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebar_anchor);
  }

  @FXML
  private void openSection() {
    View().openPopup("new_sitting_rectangulary_section.fxml");    
    View().setSidebarFromFxmlFileName("sidebar_section.fxml", sidebar_anchor);
  }

  private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
    double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
    double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
    return new Point2D(scrollXOffset, scrollYOffset);
  }

  private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
    double scrollXOffset = scrollOffset.getX();
    double scrollYOffset = scrollOffset.getY();
    double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();

    if (extraWidth > 0) {
      double halfWidth = scroller.getViewportBounds().getWidth() / 2;
      double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
      scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }

    double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    if (extraHeight > 0) {
      double halfHeight = scroller.getViewportBounds().getHeight() / 2;
      double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
      scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
  }
}