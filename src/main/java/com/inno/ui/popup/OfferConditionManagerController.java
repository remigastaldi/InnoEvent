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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.service.pricing.ImmutableOfferCondition;
import com.inno.service.pricing.ImmutableOfferOperation;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.View.AnimationDirection;
import com.inno.ui.components.OfferConditionOperationListViewCell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class OfferConditionManagerController extends ViewController {

  @FXML
  private AnchorPane anchor_root;

  @FXML
  private TextField offer_condition_name_input;

  @FXML
  private TextArea offer_condition_description_input;

  @FXML
  private ListView<OfferConditionOperationCell> offer_condition_operation_list;

  ObservableList<OfferConditionOperationCell> _offerConditionOperationList = FXCollections.observableArrayList();

  public OfferConditionManagerController() {
  }

  @FXML
  private void initialize() {
    offer_condition_operation_list.setItems(_offerConditionOperationList);
  }

  @Override
  public void init() {
    ImmutableOfferCondition offerCondition = (ImmutableOfferCondition) this.getIntent();
    if (offerCondition == null) {
      System.out.println("OfferConditon not found ");
      return;
    }

    offer_condition_name_input.setText(offerCondition.getName());
    offer_condition_description_input.setText(offerCondition.getDescription());

    refreshOfferConditionList();
    offer_condition_operation_list.setCellFactory(studentListView -> new OfferConditionOperationListViewCell());

  }

  public class OfferConditionOperationCell {
    protected int _index;
    protected String _value;
    protected String _logicalOperator;
    protected String _relationalOperator;

    OfferConditionOperationCell(int index, String value, String logicalOperator, String relationalOperator) {
      _index = index;
      _value = value;
      _relationalOperator = relationalOperator;
      _logicalOperator = logicalOperator;
    }

    public int getIndex() {
      return _index;
    }

    public String getValue() {
      return _value;
    }

    public void setValue(String value) {
      _value = value;
    }

    public String getRelationalOperator() {
      return _relationalOperator;
    }

    public void setRelationalOperator(String relationalOperator) {
      _relationalOperator = relationalOperator;
    }

    public String getLogicalOperator() {
      return _logicalOperator;
    }

    public void setLogicalOperator(String logicalOperator) {
      _logicalOperator = logicalOperator;
    }
  }

  @FXML
  private void cancelButtonAction() {
    ImmutableOfferCondition offerCondition = (ImmutableOfferCondition) this.getIntent();

    if (offerCondition == null) {
      System.out.println("OfferConditon not found ");
      return;
    }

    View().openViewWithAnimation("popup/offer_manager.fxml", AnimationDirection.RIGHT, anchor_root,
        offerCondition.getParentOffer());
  }

  @FXML
  private void onKeyReleased() {
    checkInputs();
  }

  private void refreshOfferConditionList() {

    ImmutableOfferCondition offerCondition = (ImmutableOfferCondition) this.getIntent();

    if (offerCondition == null) {
      System.out.println("OfferConditon not found ");
      return;
    }

    _offerConditionOperationList.clear();
    ArrayList<? extends ImmutableOfferOperation> offerConditionOperations = Core()
        .getOfferCondition(offerCondition.getParentOffer().getName(), offerCondition.getName())
        .getImmutableOfferOperations();

    for (int i = 0; i < offerConditionOperations.size(); i++) {
      _offerConditionOperationList.add(new OfferConditionOperationCell(i, offerConditionOperations.get(i).getValue(),
          offerConditionOperations.get(i).getLogicalOperator().toString(),
          offerConditionOperations.get(i).getRelationalOperator().toString()));
    }
  }

  @FXML
  private void doneButtonAction() {
    ImmutableOfferCondition offerCondition = (ImmutableOfferCondition) this.getIntent();

    if (offerCondition == null) {
      System.out.println("OfferConditon not found ");
      return;
    }
    if (checkInputs()) {
      if (offer_condition_name_input.getText() != offerCondition.getName()) {
        Core().setOfferConditionName(offerCondition.getParentOffer().getName(), offerCondition.getName(),
            offer_condition_name_input.getText());
      }
      if (offer_condition_description_input.getText() != offerCondition.getDescription()) {
        Core().setOfferConditionDescription(offerCondition.getParentOffer().getName(), offerCondition.getName(),
            offer_condition_description_input.getText());
      }

      _offerConditionOperationList.forEach((operation) -> {
        Core().setOfferConditionOperationValue(offerCondition.getParentOffer().getName(), offerCondition.getName(),
            operation.getIndex(), operation.getValue());
        Core().setOfferConditionOperationLogicalOperator(offerCondition.getParentOffer().getName(),
            offerCondition.getName(), operation.getIndex(), operation.getLogicalOperator());
        Core().setOfferConditionOperationRelationalOperator(offerCondition.getParentOffer().getName(),
            offerCondition.getName(), operation.getIndex(), operation.getRelationalOperator());
      });

      View().openViewWithAnimation("popup/offer_manager.fxml", AnimationDirection.RIGHT, anchor_root,
          offerCondition.getParentOffer());
    }
  }

  @FXML
  private void createOfferConditionAction() {
    ImmutableOfferCondition offerCondition = (ImmutableOfferCondition) this.getIntent();

    if (offerCondition == null) {
      System.out.println("OfferConditon not found ");
      return;
    }

    Core().createOfferConditionOperation(offerCondition.getParentOffer().getName(), offerCondition.getName(), "", "EQUALS", "AND");
    refreshOfferConditionList();
  }

  private boolean checkInputs() {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(offer_condition_name_input, "required|max:30");

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