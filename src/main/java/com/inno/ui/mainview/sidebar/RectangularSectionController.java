/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Wednesday, 12th December 2018
 * Modified By: HUBERT Léo
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.service.pricing.ImmutableOffer;
import com.inno.service.pricing.ImmutablePlaceRate;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.engine.shape.InteractiveRectangle;
import com.inno.ui.innoengine.shape.InnoRectangle;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class RectangularSectionController extends ViewController {

  @FXML
  private Accordion accordion;
  @FXML
  private AnchorPane anchor_root;
  @FXML
  private TextField section_name_input;
  @FXML
  private TextField section_columns_input;
  @FXML
  private TextField section_rows_input;
  @FXML
  private TextField section_vital_space_width_input;
  @FXML
  private TextField section_vital_space_height_input;
  @FXML
  private TextField section_rotation_input;
  @FXML
  private TextField section_elevation_input;

  @FXML
  private Group section_rotation_group;
  @FXML
  private Circle section_rotation_circle;
  @FXML
  private AnchorPane sidebar_content;

  @FXML
  private TextField section_price_input;
  @FXML
  private ColorPicker section_price_color_picker;

  @FXML
  private ListView<String> attributed_offers_list;
  @FXML
  private ListView<String> available_offers_list;

  EventHandler<MouseEvent> _mouseDragged;

  @FXML
  private void initialize() {

    if (Core().getSettingsValue("opened" + getClass().getName()) != null) {
      accordion.getPanes().forEach((pane) -> {
        if (pane.getText().equals(Core().getSettingsValue("opened" + getClass().getName()))) {
          accordion.expandedPaneProperty().set(pane);
        }
      });
    } else {
      accordion.expandedPaneProperty().set(accordion.getPanes().get(0));
      Core().setSettingsValue("opened" + getClass().getName(), accordion.expandedPaneProperty().getValue().getText());
    }

    accordion.expandedPaneProperty().addListener((e) -> {
      if (accordion.expandedPaneProperty().getValue() != null) {
        Core().setSettingsValue("opened" + getClass().getName(), accordion.expandedPaneProperty().getValue().getText());
      }
    });
  }

  public void init() {
    InnoRectangle rectangle = (InnoRectangle) getIntent();

    if (rectangle == null) {
      System.out.println("Rectangle is null");
      return;
    }

    if (Core().getSectionPrice(rectangle.getID()) != null
        && Core().getSectionPrice(rectangle.getID()).getPrice() != -1) {
      section_price_input.setText(Double.toString(Core().getSectionPrice(rectangle.getID()).getPrice()));
    }

    section_name_input.setText(Core().getImmutableRoom().getSectionById(rectangle.getID()).getNameSection());
    section_columns_input.setText(Integer.toString(rectangle.getColumnNumber()));
    section_rows_input.setText(Integer.toString(rectangle.getRowNumber()));
    section_rotation_input.setText(Double.toString(rectangle.getRotation().getAngle()));

    // setRotation(rectangle.getRotation().getAngle(), false);
    section_rotation_input
        .setText(Double.toString(Core().getImmutableRoom().getSectionById(rectangle.getID()).getUserRotation()));

    section_vital_space_width_input
        .setText(Double.toString(rectangle.getSectionData().getImmutableVitalSpace().getWidth()));
    section_vital_space_height_input
        .setText(Double.toString(rectangle.getSectionData().getImmutableVitalSpace().getHeight()));

    rectangle.widthProperty().addListener((ChangeListener<Number>) (ov, oldX, newX) -> {
      if (!section_columns_input.getText().equals(Integer.toString(rectangle.getColumnNumber()))) {
        section_columns_input.setText(Integer.toString(rectangle.getColumnNumber()));
      }
    });
    rectangle.heightProperty().addListener((ChangeListener<Number>) (ov, oldY, newY) -> {
      if (!section_rows_input.getText().equals(Integer.toString(rectangle.getRowNumber()))) {
        section_rows_input.setText(Integer.toString(rectangle.getRowNumber()));
      }
    });

    // Price color
    ImmutablePlaceRate place = Core().getSectionPrice(rectangle.getID());
    if (place != null) {
      if (place.getPrice() != -1) {
        section_price_input.setText(Double.toString(place.getPrice()));
        section_price_color_picker.setDisable(false);
      }
      section_price_color_picker.setValue(Color.valueOf(place.getColor()));
    }

    // Offers
    refreshAttributedOffer();

    available_offers_list.setOnMouseClicked((e) -> {
      if (e.getClickCount() == 2 && available_offers_list.getFocusModel().getFocusedItem() != null) {
        Core().addSectionOffer(rectangle.getID(), available_offers_list.getFocusModel().getFocusedItem());
        available_offers_list.getItems().remove(available_offers_list.getFocusModel().getFocusedItem());
        refreshAttributedOffer();
      }
    });

    attributed_offers_list.setOnMouseClicked((e) -> {
      if (e.getClickCount() == 2 && attributed_offers_list.getFocusModel().getFocusedItem() != null) {
        Core().removeSectionOffer(rectangle.getID(), attributed_offers_list.getFocusModel().getFocusedItem());
        available_offers_list.getItems().add(attributed_offers_list.getFocusModel().getFocusedItem());
        refreshAttributedOffer();
      }
    });

  }

  private void refreshAttributedOffer() {
    InnoRectangle rectangle = (InnoRectangle) getIntent();

    if (rectangle == null) {
      System.out.println("Rectangle is null");
      return;
    }


    ArrayList<? extends ImmutableOffer> offers = Core().getSectionPrice(rectangle.getID()).getImmutableOffers();
    attributed_offers_list.getItems().clear();
    available_offers_list.getItems().clear();
    available_offers_list.getItems().addAll(Core().getObservableOffersList());
    offers.forEach((offer) -> {
      available_offers_list.getItems().remove(offer.getName());
      attributed_offers_list.getItems().add(offer.getName());
    });
  }

  private void setRotation(Double angle, boolean input) {
    section_rotation_group.setRotate(angle);
    if (!input) {
      section_rotation_input.setText("" + (angle));
    }
    InteractiveRectangle rectangle = (InteractiveRectangle) getIntent();

    Core().setSectionUserRotation(rectangle.getID(), angle);
    rectangle.setRotationAngle(Core().getImmutableRoom().getSectionById(rectangle.getID()).getRotation());
  }

  // private void setRotation(String angle, boolean input) {
  // try {
  // Double nAngle = Double.parseDouble(angle);
  // setRotation(nAngle, input);
  // } catch (Exception e) {
  // System.out.println("Given angle is not double" + e.getMessage());
  // }
  // }

  @FXML
  private void onMousePressed() {
    _mouseDragged = mouseEvent -> {
      double mouseX = mouseEvent.getX();
      double mouseY = mouseEvent.getY();
      Point2D pos = new Point2D(mouseX, mouseY);
      Point2D pos2 = new Point2D(section_rotation_circle.getCenterX(), section_rotation_circle.getCenterY());
      pos2 = section_rotation_group.localToScene(pos2);
      pos2 = sidebar_content.sceneToLocal(pos2);

      double angle = Math.atan2(pos.getX() - pos2.getX(), -(pos.getY() - pos2.getY())) * (180 / Math.PI);

      System.out.println(angle);
      // angle = convertTo360(angle);
      setRotation(angle, false);
    };
    sidebar_content.addEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDragged);
  }

  private double convertTo360(double angle) {
    if (angle < 0) {
      angle = ((3600000 + angle) % 360);
    }

    if (angle > 0 && angle < 90) {
      angle = 360 - 90 + angle;
    } else {
      angle -= 90;
    }
    return angle;
  }

  @FXML
  private void onMouseReleased() {
    sidebar_content.removeEventHandler(MouseEvent.MOUSE_DRAGGED, _mouseDragged);
  }

  @FXML
  private void onKeyReleased() {
    if (checkInputs()) {
      InnoRectangle rectangle = (InnoRectangle) getIntent();

      if (rectangle == null) {
        System.out.println("Rectangle is null");
        return;
      }
      try {
        if (section_price_input.isFocused()) {
          Core().setSectionPrice(rectangle.getID(), Double.parseDouble(section_price_input.getText()));
        }
        if (section_columns_input.isFocused())
          rectangle.setColumnNumber(Integer.parseInt(section_columns_input.getText()));
        if (section_rows_input.isFocused())
          rectangle.setRowNumber(Integer.parseInt(section_rows_input.getText()));
        if (section_rotation_input.isFocused())
          setRotation(Double.parseDouble(section_rotation_input.getText()), false);
        if (section_vital_space_width_input.isFocused() || section_vital_space_height_input.isFocused()) {
          double width = Double.parseDouble(section_vital_space_width_input.getText());
          double height = Double.parseDouble(section_vital_space_height_input.getText());
          rectangle.setVitalSpace(width, height);
          rectangle.updateFromData();
        }
        if (section_name_input.isFocused())
          Core().setSectionName(rectangle.getID(), section_name_input.getText());

        if (section_price_input.getText().trim().length() != 0) {
          section_price_color_picker.setDisable(false);
          Core().setSectionPrice(rectangle.getID(),
              Double.parseDouble(
                  section_price_input.getText().trim().length() != 0 ? section_price_input.getText() : "-1"),
              "#" + Integer.toHexString(section_price_color_picker.getValue().hashCode()));
          if (section_price_color_picker.isFocused()) {
            rectangle.updateRowsFromData(false);
          }
        } else {
          Core().setSectionPrice(rectangle.getID(),
              Double.parseDouble(
                  section_price_input.getText().trim().length() != 0 ? section_price_input.getText() : "-1"),
              "#6378bf");
          section_price_color_picker.setDisable(true);
          section_price_color_picker.setValue(Color.valueOf("#6378bf"));
        }

      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private boolean checkInputs() {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(section_name_input, "required|max:30");
    fields.put(section_columns_input, "required|numeric|min:1");
    fields.put(section_rows_input, "required|numeric|min:1");
    fields.put(section_vital_space_width_input, "required|numeric");
    fields.put(section_vital_space_height_input, "required|numeric");
    fields.put(section_rotation_input, "required|numeric|min:0|max:360");
    fields.put(section_price_input, "numeric|min:0");

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