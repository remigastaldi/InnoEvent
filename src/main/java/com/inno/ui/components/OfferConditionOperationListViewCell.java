/*
 * File Created: Thursday, 6th December 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Tuesday, 11th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.ui.components;

import java.io.IOException;

import com.inno.app.Core;
import com.inno.ui.popup.OfferConditionManagerController.OfferConditionOperationCell;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class OfferConditionOperationListViewCell extends ListCell<OfferConditionOperationCell> {

    @FXML
    private GridPane grid_pane;

    @FXML
    private ChoiceBox<String> operation_logical;

    @FXML
    private ChoiceBox<String> operation_relational;

    @FXML
    private TextField operation_value;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(OfferConditionOperationCell offerOperation, boolean empty) {
        super.updateItem(offerOperation, empty);

        if (empty || offerOperation == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/components/offer_condition_operation.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String[] relationalOperators = Core.get().getRelationalOperatorTypePossibilities();
            String[] logicalOperators = Core.get().getLogicalOperatorTypePossibilities();

            operation_logical.setItems(FXCollections.observableArrayList(logicalOperators));
            operation_relational.setItems(FXCollections.observableArrayList(relationalOperators));

            operation_logical.getSelectionModel().select(offerOperation.getLogicalOperator());
            operation_relational.getSelectionModel().select(offerOperation.getRelationalOperator());

            operation_value.setText(offerOperation.getValue());

            operation_logical.setOnAction((e) -> {
                offerOperation.setLogicalOperator(operation_logical.getSelectionModel().getSelectedItem());
            });

            operation_relational.setOnAction((e) -> {
                offerOperation.setRelationalOperator(operation_relational.getSelectionModel().getSelectedItem());
            });

            operation_value.setOnKeyReleased((e) -> {
                offerOperation.setValue(operation_value.getText());
            });

            setText(null);
            setGraphic(grid_pane);
        }
    }
}