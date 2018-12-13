/*
 * File Created: Thursday, 6th December 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */

package com.inno.ui.components;

import java.io.IOException;

import com.inno.ui.popup.OfferManagerController.CellWithDelFunction;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public class CellWithDelButton extends ListCell<CellWithDelFunction> {

    @FXML
    private GridPane grid_pane;

    @FXML
    private Label label;

    private FXMLLoader mLLoader;

    private CellWithDelFunction _cell;

    @Override
    protected void updateItem(CellWithDelFunction cell, boolean empty) {
        super.updateItem(cell, empty);
        _cell = cell;

        if (empty || cell == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/components/cell_with_del_button.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            label.setText(cell.getLabel());

            setText(null);
            setGraphic(grid_pane);
        }
    }

    @FXML
    private void delButtonAction() {
        _cell.callDelFunction();
    }
}