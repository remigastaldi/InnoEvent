/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 15th November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.mainview;

import com.inno.ui.ViewController;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainViewController extends ViewController {

  @FXML
  private AnchorPane top_bar;

  @FXML
  private AnchorPane sidebarAnchor;
  
  @FXML
  private AnchorPane anchor_canvas;
  
  @FXML
  private StackPane stack_pane;
  
  @FXML
  private void initialize() {
  }
  
  private ScrollPane scrollPane;
  
  public void init() {
    View().setSidebarFromFxmlFileName("sidebar_room.fxml", sidebarAnchor);

    Pane _pane = new Pane();
    _pane.setPrefSize(900, 700);

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

          // final Bounds groupBounds = group.getLayoutBounds();
          // final Bounds viewportBounds = scrollPane.getViewportBounds();

          // calculate pixel offsets from [0, 1] range
          // double valX = scrollPane.getHvalue() * (groupBounds.getWidth() - viewportBounds.getWidth());
          // double valY = scrollPane.getVvalue() * (groupBounds.getHeight() - viewportBounds.getHeight());

          // convert content coordinates to zoomTarget coordinates
          // Point2D posInZoomTarget = _pane.parentToLocal(group.parentToLocal(new Point2D(evt.getX(), evt.getY())));

          // calculate adjustment of scroll position (pixels)
          // Point2D adjustment = _pane.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));
          // System.out.println("adjustement => " + adjustment);

          Point2D scrollOffset = figureScrollOffset(group, scrollPane);

          // do the resizing
          _pane.setScaleX(zoomFactor * _pane.getScaleX());
          _pane.setScaleY(zoomFactor * _pane.getScaleY());

          // refresh ScrollPane scroll positions & content bounds
          scrollPane.layout();

          repositionScroller(group, scrollPane, zoomFactor, scrollOffset);
          // convert back to [0, 1] range
          // (too large/small values are automatically corrected by ScrollPane)
          // scrollPane.setHvalue((valX + adjustment.getX()) / (groupBounds.getWidth() - viewportBounds.getWidth()));
          // scrollPane.setVvalue((valY + adjustment.getY()) / (groupBounds.getHeight() - viewportBounds.getHeight()));
      }
    });
    stack_pane.getChildren().add(scrollPane);

    View().createEngine(_pane);
    Engine().scrlPane = scrollPane;
  }

  @FXML
  private void  keyAction(KeyEvent evt) {
    if (evt.getText().compareTo("a") == 0)
      Engine().createIrregularSection();
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