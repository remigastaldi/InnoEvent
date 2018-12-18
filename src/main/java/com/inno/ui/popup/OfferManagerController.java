/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 17th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Function;
import com.inno.service.pricing.ImmutableOffer;
import com.inno.service.pricing.ImmutableOfferCondition;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.View.AnimationDirection;
import com.inno.ui.components.CellWithDelButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OfferManagerController extends ViewController {

    @FXML
    private ListView<CellWithDelFunction> offerList;
    @FXML
    private ListView<CellWithDelFunction> offerConditionList;
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
    @FXML
    private ChoiceBox<String> offerReductionType;

    ObservableList<CellWithDelFunction> _offerList = FXCollections.observableArrayList();
    ObservableList<CellWithDelFunction> _offerConditionList = FXCollections.observableArrayList();
    ImmutableOffer _selectedOffer = null;

    public class UIOfferCondition {
        private ImmutableOffer _offer;
        private ImmutableOfferCondition _offerCondition;

        public UIOfferCondition(ImmutableOffer offer, ImmutableOfferCondition offerCondition) {
            _offer = offer;
            _offerCondition = offerCondition;
        }

        public ImmutableOffer getOffer() {
            return _offer;
        }

        public ImmutableOfferCondition getOfferCondition() {
            return _offerCondition;
        }
    }

    public class CellWithDelFunction {
        private String _label;
        private Function<String, Boolean> _function;

        CellWithDelFunction(String label, Function<String, Boolean> function) {
            _label = label;
            _function = function;
        }

        public String getLabel() {
            return _label;
        }

        public boolean callDelFunction() {
            if (_function != null)
                return _function.apply(_label);
            return false;
        }

    }

    public OfferManagerController() {
    }

    private void setSelectedOffer(ImmutableOffer offer) {

        if (_selectedOffer == null && offer != null) {
            offerProperties.setVisible(true);
        } else if (offer == null) {
            _selectedOffer = null;
            offerProperties.setVisible(false);
            return;
        }

        offerReductionType.setOnAction(null);

        offerReductionType.getSelectionModel().select(offer.getReductionType().toString());

        offerReductionType.setOnAction((e) -> {
            if (offerReductionType.getSelectionModel().getSelectedItem() != null && _selectedOffer != null) {
                Core().setOfferReductionType(_selectedOffer.getName(),
                        offerReductionType.getSelectionModel().getSelectedItem());
            }
        });

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
        offerList.setCellFactory(studentListView -> new CellWithDelButton());
        offerConditionList.setCellFactory(studentListView -> new CellWithDelButton());

        offerList.setItems(_offerList);
        offerConditionList.setItems(_offerConditionList);
        refreshOfferList();

        offerReductionType.getItems().addAll(Core().getReductionTypePossibilities());
    }

    @FXML
    private void offerListOnMouseClicked() {
        if (offerList.getSelectionModel().getSelectedItem() != null) {
            ImmutableOffer offer = Core().getOffer(offerList.getSelectionModel().getSelectedItem().getLabel());
            if (offer == null) {
                return;
            }
            setSelectedOffer(offer);
        }
    }

    @FXML
    private void offerConditionListOnMouseClicked(MouseEvent click) {
        if (click.getClickCount() == 2 && _selectedOffer != null
                && offerConditionList.getSelectionModel().getSelectedItem() != null) {
            ImmutableOfferCondition offerCondition = Core().getOfferCondition(_selectedOffer.getName(),
                    offerConditionList.getSelectionModel().getSelectedItem().getLabel());
            if (offerCondition != null) {
                View().openViewWithAnimation("popup/offer_condition_manager.fxml", AnimationDirection.LEFT, anchor_root,
                        new UIOfferCondition(_selectedOffer, offerCondition));
            }
        }
    }

    private void refreshOfferConditionList(String offerName) {
        _offerConditionList.clear();
        HashMap<String, ? extends ImmutableOfferCondition> offersCondition = Core().getOfferConditions(offerName);
        for (Map.Entry<String, ? extends ImmutableOfferCondition> entry : offersCondition.entrySet()) {
            String offerConditionName = entry.getKey();
            _offerConditionList.add(new CellWithDelFunction(offerConditionName, (offerConditionNameSelected) -> {
                Core().deleteOfferCondition(_selectedOffer.getName(), offerConditionNameSelected);
                refreshOfferConditionList(_selectedOffer.getName());
                return true;
            }));
        }
    }

    private void refreshOfferList() {
        _offerList.clear();
        HashMap<String, ? extends ImmutableOffer> offers = Core().getOffers();
        for (Map.Entry<String, ? extends ImmutableOffer> entry : offers.entrySet()) {
            String offerName = entry.getKey();
            _offerList.add(new CellWithDelFunction(offerName, (offerNameSelected) -> {
                if (_selectedOffer != null && offerNameSelected.equals(_selectedOffer.getName())) {
                    setSelectedOffer(null);
                }
                Core().deleteOffer(offerNameSelected);
                refreshOfferList();
                return true;
            }));
        }
    }

    @FXML
    private void doneButtonAction() {
        Stage stage = (Stage) anchor_root.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    private void cancelButtonAction() {
        Stage stage = (Stage) anchor_root.getScene().getWindow();
        // do what you have to do
        stage.close();
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
                new UIOfferCondition(_selectedOffer, offerCondition));
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
        refreshOfferList();
        setSelectedOffer(offer);
    }
}