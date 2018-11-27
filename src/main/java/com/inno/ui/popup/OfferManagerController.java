/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 26th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.service.pricing.OfferData;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class OfferManagerController extends ViewController {

    @FXML
    private ListView<String> offerList;
    @FXML
    private ListView<String> offerConditionList;
    @FXML
    private AnchorPane offerProperties;
    @FXML
    private TextField offerReductionInput;
    @FXML
    private TextField offerNameInput;
    @FXML
    private AnchorPane anchor_root;
    @FXML
    private StackPane parentContainer;

    ObservableList<String> _items = FXCollections.observableArrayList();
    OfferData _selectedOffer = null;

    public OfferManagerController() {
        Core().createOffer("toto1", "Je sais pas encore", 50, "PERCENTAGE");
        Core().createOffer("toto2", "Je sais pas encore", 50, "PERCENTAGE");
        Core().createOffer("toto4", "Je sais pas encore", 50, "PERCENTAGE");
    }

    private void setSelectedOffer(OfferData offer) {

        if (_selectedOffer == null && offer != null) {
            offerProperties.setVisible(true);
        }

        for (int i = 0; i < offerList.getItems().size(); ++i) {
            String offerName = offerList.getItems().get(i).toString();
            if (offer.getName().equals(offerName)) {
                offerList.getSelectionModel().select(i);
                offerList.getFocusModel().focus(i);
                offerList.scrollTo(i);
            }
        }
        
        if (!offerNameInput.getText().equals(offer.getName())) {
            offerNameInput.setText(offer.getName());
        }
        if (offerReductionInput.getText() != Double.toString(offer.getReduction())) {
            offerReductionInput.setText(Double.toString(offer.getReduction()));
        }
        _selectedOffer = offer;
    }

    @FXML
    private void initialize() {
        offerList.setItems(_items);
        refreshOfferList();
    }

    @FXML
    private void onMouseClicked() {
        OfferData offer = Core().getOffer(offerList.getSelectionModel().getSelectedItem().toString());
        setSelectedOffer(offer);
    }

    private void refreshOfferList() {
        _items.clear();
        HashMap<String, ? extends OfferData> offers = Core().getOffers();
        for (Map.Entry<String, ? extends OfferData> entry : offers.entrySet()) {
            String offerName = entry.getKey();
            _items.add(offerName);
        }
    }

    @FXML
    private void createNewOfferAction() {
        OfferData offer = Core().createOffer(null, "", 0, "PERCENTAGE");
        refreshOfferList();
        setSelectedOffer(offer);
    }

    @FXML
    private void createNewConditionAction() {

    }

    @FXML
    private void onTextChanged() {
        if (checkInputs()) {
            double reduction = Double.parseDouble(offerReductionInput.getText());
            if (!_selectedOffer.getName().equals(offerNameInput.getText())) {
                OfferData offer = Core().setOfferName(_selectedOffer.getName(), offerNameInput.getText());
                refreshOfferList();
                setSelectedOffer(offer);
            }
            if (_selectedOffer.getReduction() != reduction) {
                Core().setOfferReduction(_selectedOffer.getName(), reduction);
            }
        }
    }

    private boolean checkInputs() {
        boolean valid = true;
        HashMap<TextField, String> fields = new LinkedHashMap<>();
        fields.put(offerNameInput, "required|max:30");
        fields.put(offerReductionInput, "required|numeric|min:0|max:100");

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

    @Override
    public void init() {
    }
}