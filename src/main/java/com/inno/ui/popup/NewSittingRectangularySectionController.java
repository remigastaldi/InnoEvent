/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 17th November 2018
 * Modified By: GASTALDI Rémi
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

public class NewSittingRectangularySectionController extends ViewController {
  @FXML
  private TextField columnsInput;
  @FXML
  private TextField rangeInput;

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
      rectangle.setWidth(Double.parseDouble(columnsInput.getText()));
      rectangle.setHeight(Double.parseDouble(rangeInput.getText()));
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
    columnsInput.setText(Double.toString(rectangle.getWidth()));
    rangeInput.setText(Double.toString(rectangle.getHeight()));
    columnsInput.textProperty().bindBidirectional(rectangle.getWidthProperty(), new NumberStringConverter());
    rangeInput.textProperty().bindBidirectional(rectangle.getHeightProperty(), new NumberStringConverter());
    // StringConverter<Number> converter = new NumberStringConverter();
    // Bindings.bindBidirectional(_width, rectangle.getMaxXProperty(), converter);
    // Bindings.bindBidirectional(_height, rectangle.getMaxXProperty(), converter);

  }
}