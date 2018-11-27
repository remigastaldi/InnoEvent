/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: MAREL Maud
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.converter.NumberStringConverter;
import javafx.beans.value.ChangeListener;

public class NewSittingRectangularySectionController extends ViewController {
  @FXML
  private TextField columns_input;
  @FXML
  private TextField rows_input;
  @FXML
  private TextField width_vital_space_input;
  @FXML
  private TextField height_vital_space_input;

  private SimpleDoubleProperty widthInput = new SimpleDoubleProperty();
  private SimpleDoubleProperty heightInput = new SimpleDoubleProperty();

  public NewSittingRectangularySectionController() {
  }

  @FXML
  private void initialize() {
  }

  private boolean checkInputs() {
    boolean valid = true;
    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(columns_input, "required|numeric|min:1");
    fields.put(rows_input, "required|numeric|min:1");
    fields.put(width_vital_space_input, "required|numeric|min:1");
    fields.put(height_vital_space_input, "required|numeric|min:1");

    for (Map.Entry<TextField, String> entry : fields.entrySet()) {
      TextField field = entry.getKey();
      String validator = entry.getValue();
      if (!Validator.validate(field.getText(), validator)) {
        if (!field.getStyleClass().contains("error"))
          field.getStyleClass().add("error");
        valid = false;
      } else if (field.isFocused()) {
        if (field.getStyleClass().contains("error"))
          field.getStyleClass().remove("error");
      }
    }
    return valid;
  }

  @FXML
  public void onKeyReleasedAction() {
    if (checkInputs()) {
      InnoRectangle rectangle = (InnoRectangle) getIntent();

      if (rectangle == null) {
        System.out.println("Rectangle is null");
        return;
      }
      try {
        if (columns_input.isFocused())
          rectangle.setColumnNumber(Integer.parseInt(columns_input.getText()));
        if (rows_input.isFocused())
          rectangle.setRowNumber(Integer.parseInt(rows_input.getText()));
        if (width_vital_space_input.isFocused() || height_vital_space_input.isFocused()) {
          rectangle.setVitalSpace(Double.parseDouble(width_vital_space_input.getText()),
              Double.parseDouble(height_vital_space_input.getText()));
          widthInput.set(rectangle.getColumnNumber());
          heightInput.set(rectangle.getRowNumber());
        }
        // }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  @FXML
  public void doneButtonAction() {
    if (checkInputs() == false) {
      return;
    }
    Stage stage = (Stage) rows_input.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void cancelButtonAction() {
    Stage stage = (Stage) rows_input.getScene().getWindow();
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

    columns_input.textProperty().bindBidirectional(widthInput, new NumberStringConverter());
    rows_input.textProperty().bindBidirectional(heightInput, new NumberStringConverter());
    widthInput.set(rectangle.getColumnNumber());
    heightInput.set(rectangle.getRowNumber());
    width_vital_space_input.textProperty()
        .set(Double.toString(rectangle.getSectionData().getImmutableVitalSpace().getWidth()));
    height_vital_space_input.textProperty()
        .set(Double.toString(rectangle.getSectionData().getImmutableVitalSpace().getHeight()));

    rectangle.getWidthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (widthInput.get() != rectangle.getColumnNumber())
        widthInput.set(rectangle.getColumnNumber());
    });
    rectangle.getHeightProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (heightInput.get() != rectangle.getRowNumber())
        heightInput.set(rectangle.getRowNumber());
    });
    widthInput.addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (checkInputs()) {
        if (rectangle.getColumnNumber() != newX.intValue())
          rectangle.setColumnNumber(newX.intValue());
      }

    });
    heightInput.addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (checkInputs()) {
        if (rectangle.getRowNumber() != newY.intValue())
          rectangle.setRowNumber(newY.intValue());
      }
    });

  }
}