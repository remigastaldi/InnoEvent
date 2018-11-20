/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 20th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import com.inno.ui.ViewController;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;

public class NewSittingRectangularySectionController extends ViewController {
  @FXML
  private TextField columnsInput;
  @FXML
  private TextField rangeInput;
  @FXML
  private TextField heightVitalSpaceInput;
  @FXML
  private TextField widthVitalSpaceInput;

  private StringProperty _width = new SimpleStringProperty();
  private StringProperty _height = new SimpleStringProperty();
  
  public NewSittingRectangularySectionController() {
  }
  
  @FXML
  private void initialize() {
  }
  
  @FXML
  public void onKeyReleasedAction() {
    InnoRectangle rectangle = (InnoRectangle) getIntent();

    if (rectangle == null) {
      System.out.println("Rectangle is null");
      return;
    }
    try {
      rectangle.setColumnNumber(Integer.parseInt(columnsInput.getText()));
      rectangle.setRowNumber(Integer.parseInt(rangeInput.getText()));
      // rectangle.setWidth(Double.parseDouble(columnsInput.getText()));
      // rectangle.setHeight(Double.parseDouble(rangeInput.getText()));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @FXML
  public void doneButtonAction() {
    Stage stage = (Stage) rangeInput.getScene().getWindow();
    stage.close();
  }
  
  @FXML
  public void cancelButtonAction() {
    Stage stage = (Stage) rangeInput.getScene().getWindow();
    Engine().deleteSelectedShape();
    stage.close();
  }
  
  @Override
  public void init() {
    InnoRectangle rectangle = (InnoRectangle) getIntent();

    if (rectangle == null) {
      System.out.println("Rectangle is null");
      return;
    }
    SimpleDoubleProperty widthInput = new SimpleDoubleProperty();
    SimpleDoubleProperty heightInput = new SimpleDoubleProperty();

    // columnsInput.setText(Double.toString(rectangle.getWidth()));
    // rangeInput.setText(Double.toString(rectangle.getHeight()));
    
    columnsInput.textProperty().bindBidirectional(widthInput, new NumberStringConverter());
    rangeInput.textProperty().bindBidirectional(heightInput, new NumberStringConverter());
    widthInput.set(rectangle.getColumnNumber());
    heightInput.set(rectangle.getRowNumber());
    rectangle.getWidthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (widthInput.get() != rectangle.getColumnNumber())
        widthInput.set(rectangle.getColumnNumber());
    });
    rectangle.getHeightProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (heightInput.get() != rectangle.getRowNumber())
        heightInput.set(rectangle.getRowNumber());
    });
    widthInput.addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (rectangle.getColumnNumber() != newX.intValue())
        rectangle.setColumnNumber(newX.intValue());
      // rectangle.getWidthProperty().set(Engine().meterToPixel( newX * 1.5));
    });
    heightInput.addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      // Double y = (Double) newY;
      if (rectangle.getRowNumber() != newY.intValue())
        rectangle.setRowNumber(newY.intValue());
      // rectangle.getHeightProperty().set(Engine().meterToPixel( newY * 1.5));
    });
    // StringConverter<Number> converter = new NumberStringConverter();
    // Bindings.bindBidirectional(_width, rectangle.getMaxXProperty(), converter);
    // Bindings.bindBidirectional(_height, rectangle.getMaxXProperty(), converter);

  }
}