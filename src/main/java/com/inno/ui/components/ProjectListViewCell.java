/*
 * File Created: Thursday, 6th December 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Thursday, 6th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 HUBERT Léo
 * <<licensetext>>
 */


package com.inno.ui.components;
import java.io.IOException;

import com.inno.ui.popup.StartupPopupController.Project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public class ProjectListViewCell extends ListCell<Project>  {
    
    @FXML
    private GridPane grid_pane;
    @FXML
    private Label project_name_label;
    @FXML
    private Label project_path_label;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Project project, boolean empty) {
        super.updateItem(project, empty);

        if(empty || project == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/components/recent_project.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            project_name_label.setText(project.getName());
            project_path_label.setText(project.getPath());

            setText(null);
            setGraphic(grid_pane);
        }

    }
}