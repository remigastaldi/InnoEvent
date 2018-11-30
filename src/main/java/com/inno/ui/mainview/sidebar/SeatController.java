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

public class SeatController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private Label seat_number_info;
  @FXML
  private Label row_number_info;
  @FXML
  private Label section_number_info;

  @FXML
  private Label seat_price_info;
  @FXML
  private Rectangle seat_price_color_info;

  @FXML
  private TextField seat_price_input;
  @FXML
  private ColorPicker seat_price_color_picker;

  @FXML
  private void initialize() {
  }

  public void init() {
    InnoRow row = (InnoRow) getIntent();

    if (row == null) {
      System.out.println("Row is null");
      return;
    }

    seat_number_info.setText(seat_number_info.getText() + "  " + row.getSelectedSeat().getId());
    row_number_info.setText(row_number_info.getText() + "  " + row.getImmutableRow().getIdRow());
    section_number_info.setText(section_number_info.getText() + "  " + row.getImmutableSection().getIdSection());
    ImmutablePlaceRate place = Core().getSeatPrice(row.getImmutableSection().getIdSection(),
        row.getImmutableRow().getIdRow(), Integer.toString(row.getSelectedSeat().getId()));
    if (place != null) {
      seat_price_info.setText(seat_price_info.getText() + "  " + (place.getPrice() != -1 ? place.getPrice() : "NA"));
      seat_price_color_info.setFill(Color.valueOf(place.getColor()));

      if (place.getPrice() != -1) {
        seat_price_input.setText(Double.toString(place.getPrice()));
        seat_price_color_picker.setDisable(false);
      }
      seat_price_color_picker.setValue(Color.valueOf(place.getColor()));
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
        if (seat_price_input.getText().trim().length() != 0) {
          Core().setSeatPrice(row.getImmutableSection().getIdSection(), row.getImmutableRow().getIdRow(),
              Integer.toString(row.getSelectedSeat().getId()), Double.parseDouble(seat_price_input.getText()),
              "#" + Integer.toHexString(seat_price_color_picker.getValue().hashCode()));
          // seat_price_info.setText(seat_price_input.getText().trim().length() != 0 ?
          // seat_price_input.getText() : "NA"); // TODO: Fix that !
          row.resetRowColor();
          seat_price_color_info.setFill(seat_price_color_picker.getValue());
          row.setSeatColor(row.getSelectedSeat().getId(), seat_price_color_picker.getValue());
          seat_price_color_picker.setDisable(false);
        } else {
          seat_price_color_picker.setDisable(true);
          Core().setSeatPrice(row.getImmutableSection().getIdSection(), row.getImmutableRow().getIdRow(),
              Integer.toString(row.getSelectedSeat().getId()), Double.parseDouble("-1"), "#FFA500");
          seat_price_color_info.setFill(Color.valueOf("#FFA500"));
          row.resetRowColor();
          row.resetSeatColor(row.getSelectedSeat().getId());
        }
      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private boolean checkInputs() {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(seat_price_input, "numeric|min:0");

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