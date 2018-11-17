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


public class NewSittingRectangularySectionController extends ViewController {
  @FXML
  private TextField columnsInput;
  @FXML
  private TextField rangeInput;

  
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

  @Override
  public void init() {
    InnoRectangle rectangle = (InnoRectangle) getIntent();

    if (rectangle == null) {
      System.out.println("Rectangle is null");
      return;
    }
    columnsInput.setText(Double.toString(rectangle.getWidth()));
    rangeInput.setText(Double.toString(rectangle.getHeight()));
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
}