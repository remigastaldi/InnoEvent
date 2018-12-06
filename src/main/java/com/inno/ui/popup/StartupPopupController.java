/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Monday, 3rd December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.io.File;
import java.util.ArrayList;

import com.inno.ui.ViewController;
import com.inno.ui.View.AnimationDirection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;

public class StartupPopupController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private StackPane parentContainer;
  @FXML
  private Pane project_pane;
  @FXML
  private AnchorPane recent_project_pane;

  private ArrayList<String> _recentPaths;
  @FXML
  private ListView<String> recent_projects_list;
  ObservableList<String> _items = FXCollections.observableArrayList();

  public StartupPopupController() {
    _recentPaths = Core().getRecentPaths();
  }

  @FXML
  private void initialize() {
    if (_recentPaths.size() != 0) {
      recent_project_pane.setVisible(true);
      recent_projects_list.setItems(_items);
      project_pane.setPrefWidth(450);
      _recentPaths = Core().getRecentPaths();
      _recentPaths.forEach((path) -> {
        _items.add(path);
      });
    }
   
  }

  @FXML
  private void onMouseClicked() {
    if (recent_projects_list.getSelectionModel().getSelectedIndex() != -1) {
      Core().loadProject(recent_projects_list.getSelectionModel().getSelectedItem().toString());
      View().showMainView();
    }
  }

  private String ellipsise(String input, int maxLen) {
    if (input == null)
      return null;
    if ((input.length() < maxLen) || (maxLen < 3))
      return input;
    return  input.substring(0, (maxLen / 2)  - 2) + "..." + input.substring(input.length() - ((maxLen / 2)  - 2), input.length() );
  }

  public void init() {
    // View().openPopup("offer_manager.fxml");
  }

  @FXML
  private void openProjectButtonAction() {
    File file = View().getProjectFilePath();
    if (file != null) {
      Core().loadProject(file.getAbsolutePath());
      View().showMainView();
    }
  }

  @FXML
  private void createNewProjectButtonAction() {
    View().openViewWithAnimation("popup_new_project.fxml", AnimationDirection.LEFT, anchor_root);
  }
}