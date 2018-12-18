/*
 * File Created: Thursday, 6th December 2018
 * Author: HUBERT Léo
 * -----
 * Last Modified: Monday, 10th December 2018
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
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class ProjectListViewCell extends ListCell<Project> {

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

        if (empty || project == null) {

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

            Tooltip tp = new Tooltip(project.getPath());

            project_name_label.setText(project.getName());
            project_path_label.setText(project.getPath());

            project_path_label.setOnMouseEntered((e) -> {
                Bounds bounds = grid_pane.getBoundsInLocal();
                Bounds screenBounds = grid_pane.localToScreen(bounds);
                int x = (int) screenBounds.getMinX();
                int y = (int) screenBounds.getMinY();
                tp.show(project_path_label, x, y + grid_pane.getHeight());
            });
            project_path_label.setOnMouseExited((e) -> {
                tp.hide();
            });

            this.setOnMouseEntered((e) -> {
                if (project_name_label != null) {
                    project_name_label.setStyle("-fx-text-fill: -fx-primary");
                }
            });
            this.setOnMouseExited((e) -> {
                if (project_name_label != null) {
                    tp.hide();
                    project_name_label.setStyle("-fx-text-fill: white");
                }
            });

            setText(null);
            setGraphic(grid_pane);
        }
    }
}