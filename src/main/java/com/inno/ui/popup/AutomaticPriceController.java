/*
 * File Created: Monday, 17th December 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Monday, 17th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.ui.Validator;
import com.inno.ui.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AutomaticPriceController extends ViewController {

    @FXML
    private TextField minPriceInput;
    @FXML
    private TextField maxPriceInput;
    @FXML
    private TextField totalRevenuePriceInput;
    @FXML
    private ChoiceBox<String> attributionTypeDropdown;

    public AutomaticPriceController() {
    }

    @FXML
    private void initialize() {
    }

    @Override
    public void init() {
    }

    @FXML
    private void cancelButtonAction() {

    }

    @FXML
    private void doneButtonAction() {
        if (checkInputs(true)) {
            
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
        fields.put(minPriceInput, (required == true ? "required|" : "") + "numeric");
        fields.put(maxPriceInput, (required == true ? "required|" : "") + "numeric");

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