/*
 * File Created: Monday, 17th December 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Tuesday, 18th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.app.Core;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AutomaticPriceController extends ViewController {

    @FXML
    private TextField minPlacePriceInput;
    @FXML
    private TextField maxPlacePriceInput;
    @FXML
    private TextField totalRevenueInput;
    @FXML
    private ChoiceBox<String> attributionTypeDropdown;

    public AutomaticPriceController() {
    }

    @FXML
    private void initialize() {
    }

    @Override
    public void init() {

        attributionTypeDropdown.getItems().setAll(Core().getAttributionTypesPossibilities());

        if (Core.get().isAutoEnabled()) {
            minPlacePriceInput.setText(Double.toString(Core.get().getAutoMinPrice()));
            maxPlacePriceInput.setText(Double.toString(Core.get().getAutoMaxPrice()));
            totalRevenueInput.setText(Double.toString(Core.get().getAutoTotal()));
            attributionTypeDropdown.getSelectionModel().select(Core.get().getAutoAttributionType());

        } else {
            attributionTypeDropdown.getSelectionModel().select("SEAT");

        }


    }

    @FXML
    private void cancelButtonAction() {
        Stage stage = (Stage) minPlacePriceInput.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void doneButtonAction() {
        if (checkInputs(true)) {

            double minPrice = Double.parseDouble(minPlacePriceInput.getText());
            double maxPrice = Double.parseDouble(maxPlacePriceInput.getText());
            double totalRevenue = 0;
            if (totalRevenueInput.getText().length() != 0) {
                totalRevenue = Double.parseDouble(totalRevenueInput.getText());
            }

            if (Core().setAutomaticPrices(minPrice, maxPrice, totalRevenue,
                    attributionTypeDropdown.getSelectionModel().getSelectedItem())) {
                Stage stage = (Stage) minPlacePriceInput.getScene().getWindow();
                stage.close();
            } else {
                if (!totalRevenueInput.getStyleClass().contains("error"))
                    totalRevenueInput.getStyleClass().add("error");
            }
        }
    }

    @FXML
    private void onKeyReleased() {
        if (checkInputs(false)) {

        }
    }

    private boolean checkInputs(boolean required) {
        boolean valid = true;

        HashMap<TextField, String> fields = new LinkedHashMap<>();
        fields.put(minPlacePriceInput,
                (required == true ? "required|" : "") + "numeric|min:0|max:" + maxPlacePriceInput.getText());
        fields.put(maxPlacePriceInput,
                (required == true ? "required|" : "") + "numeric|min:0|min:" + minPlacePriceInput.getText());
        fields.put(totalRevenueInput, "numeric|min:0");

        for (Map.Entry<TextField, String> entry : fields.entrySet()) {
            TextField field = entry.getKey();
            String validator = entry.getValue();

            if (!Validator.validate(field.getText(), validator)) {
                if (!field.getStyleClass().contains("error"))
                    field.getStyleClass().add("error");
                valid = false;
            } else if (Validator.validate(field.getText(), validator) && field.getText().length() == 0
                    && field.getStyleClass().contains("error")) {
                valid = false;
            } else {
                if (field.getStyleClass().contains("error"))
                    field.getStyleClass().remove("error");
            }
        }
        return valid;
    }

}