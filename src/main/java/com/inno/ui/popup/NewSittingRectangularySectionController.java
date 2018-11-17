/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 17th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import com.inno.ui.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


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
  
    public void init() {

    }

    @FXML
    public void onKeyPressedAction() {
        System.out.println(Double.parseDouble(rangeInput.getText()));
        System.out.println(Double.parseDouble(columnsInput.getText()));
    }
   
  }