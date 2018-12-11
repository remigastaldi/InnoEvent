/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 10th December 2018
 * Modified By: GASTALDI Rémi
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
  private ListView<ImmutableOfferOperation> offer_condition_operation_list;

  ObservableList<ImmutableOfferOperation> _offerConditionOperatorList = FXCollections.observableArrayList();
  ArrayList<OfferConditionOperationListViewCell> _test = new ArrayList<>();

  public OfferConditionManagerController() {
  }

  @FXML
  private void initialize() {
    offer_condition_operation_list.setItems(_offerConditionOperatorList);
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
    offer_condition_operation_list.setCellFactory(studentListView -> {
      OfferConditionOperationListViewCell asd = new OfferConditionOperationListViewCell();
      _test.add(asd);
      return asd;
    });
    // offer_condition_operation_list.setCellFactory(studentListView -> new OfferConditionOperationListViewCell());

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

    _offerConditionOperatorList.clear();
    ArrayList<? extends ImmutableOfferOperation> offerConditionOperations = Core()
        .getOfferCondition(offerCondition.getParentOffer().getName(), offerCondition.getName())
        .getImmutableOfferOperations();
    System.out.println("+++ " + _test.size());
    offerConditionOperations.forEach((operation) -> {
      _offerConditionOperatorList.add((ImmutableOfferOperation) operation);
    });
    for (OfferConditionOperationListViewCell test : _test) {
      test.getTest();
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

      View().openViewWithAnimation("popup/offer_manager.fxml", AnimationDirection.RIGHT, anchor_root,
          offerCondition.getParentOffer());
    }
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