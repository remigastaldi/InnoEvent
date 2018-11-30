/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Friday, 30th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import com.inno.service.pricing.ImmutablePlaceRate;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.InnoRow;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RowController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private Label row_number_info;
  @FXML
  private Label section_number_info;

  @FXML
  private Label row_price_info;
  @FXML
  private Rectangle row_price_color_info;

  @FXML
  private TextField row_price_input;
  @FXML
  private ColorPicker row_price_color_picker;

  @FXML
  private void initialize() {
  }

  public void init() {
    InnoRow row = (InnoRow) getIntent();

    if (row == null) {
      System.out.println("Row is null");
      return;
    }

    row_number_info.setText(row_number_info.getText() + "  " + row.getImmutableRow().getIdRow());
    section_number_info.setText(section_number_info.getText() + "  " + row.getImmutableSection().getIdSection());
    ImmutablePlaceRate place = Core().getRowPrice(row.getImmutableSection().getIdSection(),
        row.getImmutableRow().getIdRow());
    if (place != null) {
      row_price_info.setText(row_price_info.getText() + "  " + (place.getPrice() != -1 ? place.getPrice() : "NA"));
      row_price_color_info.setFill(Color.valueOf(place.getColor()));
      if (place.getPrice() != -1) {
        row_price_input.setText(Double.toString(place.getPrice()));
        row_price_color_picker.setDisable(false);
      }
      row_price_color_picker.setValue(Color.valueOf(place.getColor()));
    }

  }

  @FXML
  private void onKeyReleased() {
    if (checkInputs()) {
      InnoRow row = (InnoRow) getIntent();

      if (row == null) {
        System.out.println("Row is null");
        return;
      }
      try {

        if (row_price_input.getText().trim().length() != 0) {
          row_price_color_picker.setDisable(false);
          Core().setRowPrice(row.getImmutableSection().getIdSection(), row.getImmutableRow().getIdRow(),
              Double.parseDouble(row_price_input.getText().trim().length() != 0 ? row_price_input.getText() : "-1"),
              "#" + Integer.toHexString(row_price_color_picker.getValue().hashCode()));
          // row_price_info.setText(row_price_input.getText().trim().length() != 0 ?
          // row_price_input.getText() : "NA"); // TODO: Fix that !
          if (row_price_color_picker.isFocused()) {
            row_price_color_info.setFill(row_price_color_picker.getValue());
            row.setRowColor(row_price_color_picker.getValue());
          }
        } else {
          Core().setRowPrice(row.getImmutableSection().getIdSection(), row.getImmutableRow().getIdRow(),
              Double.parseDouble(row_price_input.getText().trim().length() != 0 ? row_price_input.getText() : "-1"),
              "#7289DA");
          row_price_color_info.setFill(Color.valueOf("#7289DA"));
          row.resetRowColor();
          row.resetSeatsColor();
          row_price_color_picker.setDisable(true);
          row_price_color_picker.setValue(Color.valueOf("#7289DA"));

        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private boolean checkInputs() {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(row_price_input, "numeric|min:0");

    for (Map.Entry<TextField, String> entry : fields.entrySet()) {
      TextField field = entry.getKey();
      String validator = entry.getValue();
      if (!Validator.validate(field.getText(), validator)) {
        if (!field.getStyleClass().contains("error"))
          field.getStyleClass().add("error");
        valid = false;
      } else {
        if (field.getStyleClass().contains("error"))
          field.getStyleClass().remove("error");
      }
    }
    return valid;
  }
}