/*
 * File Created: Wednesday, 26th September 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Tuesday, 18th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 GASTALDI Rémi
 * <<licensetext>>
 */

package com.inno.ui.popup;

import java.io.File;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;
import com.inno.ui.View.AnimationDirection;
import com.inno.ui.ViewController;
import com.inno.ui.components.ProjectListViewCell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class StartupPopupController extends ViewController {

  @FXML
  private AnchorPane anchor_root;
  @FXML
  private StackPane parentContainer;
  @FXML
  private Pane project_pane;
  @FXML
  private AnchorPane recent_project_pane;

  private LinkedTreeMap<String, String> _recentPaths = new LinkedTreeMap<>();
  @FXML
  private ListView<Project> recent_projects_list;

  ObservableList<Project> _items = FXCollections.observableArrayList();

  public class Project {
    private String _path;
    private String _name;

    Project(String name, String path) {
      _name = name;
      _path = path;
    }

    public String getPath() {
      return _path;
    }

    public String getName() {
      return _name;
    }
  }

  public StartupPopupController() {
  }

  @FXML
  private void initialize() {
    _recentPaths = Core().getRecentPaths();
    if (_recentPaths.size() != 0) {
      recent_project_pane.setVisible(true);
      recent_projects_list.setItems(_items);
      project_pane.setPrefWidth(450);
      for (Map.Entry<String, String> entry : _recentPaths.entrySet()) {
        String name = entry.getKey();
        String path = entry.getValue();
        _items.add(new Project(name, path));
      }
    }
    recent_projects_list.setCellFactory(studentListView -> new ProjectListViewCell());
  }

  @FXML
  private void onMouseClicked() {
    if (recent_projects_list.getSelectionModel().getSelectedIndex() != -1) {
      Core().loadProject(recent_projects_list.getSelectionModel().getSelectedItem().getPath());
      View().showMainView();
    }
  }

  public void init() {
    System.out.println(getIntent());

    if (getIntent() != null) {
      View().openViewWithAnimation("popup_new_project.fxml", AnimationDirection.LEFT, anchor_root, "new");
    }
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