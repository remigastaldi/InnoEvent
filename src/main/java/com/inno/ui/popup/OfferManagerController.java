/*
 * File Created: Friday, 26th October 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Saturday, 24th November 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import com.inno.ui.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class OfferManagerController extends ViewController {

    @FXML
    private ListView offerList;
    @FXML
    private AnchorPane anchor_root;
    @FXML
    private StackPane parentContainer;

    public OfferManagerController() {
    }

    @FXML
    private void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");
        offerList.setItems(items);
    }

    @Override
    public void init() {
    }
}