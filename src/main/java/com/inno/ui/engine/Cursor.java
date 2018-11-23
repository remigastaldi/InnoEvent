/*
 * File Created: Thursday, 22nd November 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Thursday, 22nd November 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */


package com.inno.ui.engine;


import javafx.beans.value.ChangeListener;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;

public class Cursor extends Circle {
  public Cursor(Pane pane, double startX, double startY, double radius) {
    super(startX, startY, radius, Color.TRANSPARENT);
    double cursorSize = 32;

    setStrokeWidth(1);
    setStroke(Color.GREEN);

    Circle pt = new Circle(cursorSize / 2, cursorSize / 2, 1, Color.WHITE);

    double ptOffset = cursorSize / 5;
    Line lineXL = new Line(0.0, cursorSize / 2, cursorSize / 2 - ptOffset, cursorSize / 2);
    lineXL.setStroke(Color.WHITE);
    lineXL.setStrokeWidth(1.5);
    Line lineXR = new Line(ptOffset + cursorSize / 2, cursorSize / 2, cursorSize, cursorSize / 2);
    lineXR.setStroke(Color.WHITE);
    lineXR.setStrokeWidth(1.5);
    Line lineYU = new Line(cursorSize / 2, 0.0, cursorSize / 2, cursorSize / 2 - ptOffset);
    lineYU.setStroke(Color.WHITE);
    lineYU.setStrokeWidth(1.5);
    Line lineYD = new Line(cursorSize / 2, ptOffset + cursorSize / 2, cursorSize / 2, cursorSize);
    lineYD.setStroke(Color.WHITE);
    lineYD.setStrokeWidth(1.5);

    setOnMouseMoved(event -> {
      setCenterX(event.getX());
      setCenterY(event.getY());
    });
    
    Node[] nodes = new Node[]{lineXL, lineYU, lineXR, lineYD, pt};
    Group group = new Group(nodes);

    SnapshotParameters params = new SnapshotParameters();
    params.setFill(Color.TRANSPARENT);
    Image image = group.snapshot(params, null);
    ImageCursor addCursor = new ImageCursor(image, cursorSize / 2, cursorSize / 2);
    pane.setCursor(addCursor);

    pane.getChildren().add(this);
  }
}