/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 11th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.service.pricing.ImmutableOffer;
import com.inno.service.pricing.ImmutableOfferCondition;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.View.AnimationDirection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

    ObservableList<String> _offerList = FXCollections.observableArrayList();
    ObservableList<String> _offerConditionList = FXCollections.observableArrayList();
    ImmutableOffer _selectedOffer = null;

    public OfferManagerController() {
        // Core().createOffer("toto1", "Je sais pas encore", 50, "PERCENTAGE");
        // Core().createOffer("toto2", "Je sais pas encore", 50, "PERCENTAGE");
        // Core().createOffer("toto4", "Je sais pas encore", 50, "PERCENTAGE");

        // Core().createOfferCondition("toto1", "totocondition1", "Je ne sais pas non plus mdr", "AND");
        // Core().createOfferCondition("toto1", "totocondition2", "Je ne sais pas non plus mdr", "AND");
        // Core().createOfferCondition("toto1", "totocondition3", "Je ne sais pas non plus mdr", "AND");

        // Core().createOfferConditionOperation("toto1", "totocondition1", "20", "EQUALS", "AND");
        // Core().createOfferConditionOperation("toto1", "totocondition1", "10", "INFERIOR_OR_EQUALS", "AND");
        // Core().createOfferConditionOperation("toto1", "totocondition1", "50", "SUPERIOR", "AND");
    }

    private void setSelectedOffer(ImmutableOffer offer) {

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
        refreshOfferConditionList(offer.getName());
    }

    @FXML
    private void initialize() {
        offerList.setItems(_offerList);
        offerConditionList.setItems(_offerConditionList);
        refreshOfferList();
    }

    @FXML
    private void offerListOnMouseClicked() {
        if (offerList.getSelectionModel().getSelectedItem() != null) {
            ImmutableOffer offer = Core().getOffer(offerList.getSelectionModel().getSelectedItem().toString());
            if (offer == null) {
                return;
            }
            setSelectedOffer(offer);
        }
    }

    @FXML
    private void offerConditionListOnMouseClicked(MouseEvent click) {
        if (click.getClickCount() == 2 && _selectedOffer != null && offerConditionList.getSelectionModel().getSelectedItem() != null) {
            ImmutableOfferCondition offerCondition = Core().getOfferCondition(_selectedOffer.getName(),
                    offerConditionList.getSelectionModel().getSelectedItem().toString());
            if (offerCondition != null) {
                View().openViewWithAnimation("popup/offer_condition_manager.fxml", AnimationDirection.LEFT, anchor_root,
                        offerCondition);
            }
        }
    }

    private void refreshOfferConditionList(String offerName) {
        _offerConditionList.clear();
        HashMap<String, ? extends ImmutableOfferCondition> offersCondition = Core().getOfferConditions(offerName);
        for (Map.Entry<String, ? extends ImmutableOfferCondition> entry : offersCondition.entrySet()) {
            String offerConditionName = entry.getKey();
            _offerConditionList.add(offerConditionName);
        }
    }

    private void refreshOfferList() {
        _offerList.clear();
        HashMap<String, ? extends ImmutableOffer> offers = Core().getOffers();
        for (Map.Entry<String, ? extends ImmutableOffer> entry : offers.entrySet()) {
            String offerName = entry.getKey();
            _offerList.add(offerName);
        }
    }

    @FXML
    private void createNewOfferAction() {
        ImmutableOffer offer = Core().createOffer(null, "", 0, "PERCENTAGE");
        refreshOfferList();
        setSelectedOffer(offer);
        _offerConditionList.clear();
    }

    @FXML
    private void createNewConditionAction() {
        ImmutableOfferCondition offerCondition = Core().createOfferCondition(_selectedOffer.getName(), null, "", "AND");
        refreshOfferConditionList(_selectedOffer.getName());
        View().openViewWithAnimation("popup/offer_condition_manager.fxml", AnimationDirection.LEFT, anchor_root,
                offerCondition);
    }

    @FXML
    private void onTextChanged() {
        if (checkInputs()) {
            double reduction = Double.parseDouble(offerReductionInput.getText());
            if (!_selectedOffer.getName().equals(offerNameInput.getText())) {
                ImmutableOffer offer = Core().setOfferName(_selectedOffer.getName(), offerNameInput.getText());
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
        ImmutableOffer offer = (ImmutableOffer) this.getIntent();
        if (offer == null) {
            return;
        }
        setSelectedOffer(offer);
    }
}