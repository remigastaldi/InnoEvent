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



public class NewSittingRectangularySectionController extends ViewController {

  
    public NewSittingRectangularySectionController() {
    }
  
    @FXML
    private void initialize() {
        View().openPopup("new_sitting_rectangulary_section.fxml");
    }
  
    public void init() {

    }
  
   
  }