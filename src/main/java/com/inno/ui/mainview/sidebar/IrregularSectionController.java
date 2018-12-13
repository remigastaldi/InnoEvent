/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

import javafx.fxml.FXML;


import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class IrregularSectionController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private TextField section_name_input;
  @FXML
  private TextField section_vital_space_width_input;
  @FXML
  private TextField section_vital_space_height_input;
  @FXML
  private TextField section_auto_distrib_input;

  @FXML
  private void initialize() {
  }

  public void init() {
  }

  boolean check = true;
  @FXML
  private void onKeyReleased() {
    
    // InnoPolygon poly = (InnoPolygon) getIntent();
    
    // if (check) {
    //   System.out.println("Change sitting to standing");
    //   poly.sittingToStanding();
    //   check = false;
    // } else {
    //   System.out.println("Change standing to sitting");
    //   poly.standingToSitting();
    //   check = true;
    // }

    checkInputs(false);
  }

  private boolean checkInputs(boolean required) {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(section_name_input, (required == true ? "required|" : "") + "max:30");
    fields.put(section_vital_space_width_input, (required == true ? "required|" : "") + "numeric");
    fields.put(section_vital_space_height_input, (required == true ? "required|" : "") + "numeric");

    for (Map.Entry<TextField, String> entry : fields.entrySet()) {
      TextField field = entry.getKey();
      String validator = entry.getValue();
      if ((required || field.isFocused()) && !Validator.validate(field.getText(), validator)) {
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
}