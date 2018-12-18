/*
 * File Created: Tuesday, 13th November 2018
 * Author: MAREL Maud
 * -----
 * Last Modified: Tuesday, 18th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 MAREL Maud
 * <<licensetext>>
 */

package com.inno.ui.mainview.sidebar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inno.app.room.ImmutableSittingSection;
import com.inno.service.pricing.ImmutableOffer;
import com.inno.service.pricing.ImmutablePlaceRate;
import com.inno.ui.Validator;
import com.inno.ui.ViewController;
import com.inno.ui.innoengine.shape.InnoPolygon;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class IrregularSectionController extends ViewController {

  @FXML
  private Accordion accordion;
  @FXML
  private AnchorPane anchor_root;
  @FXML
  private TextField section_name_input;
  @FXML
  private TextField section_vital_space_width_input;
  @FXML
  private TextField section_vital_space_height_input;

  @FXML
  private TextField section_rotation_input;
  @FXML
  private TextField section_elevation_input;

  @FXML
  private CheckBox section_auto_distrib_input;

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
    InnoPolygon polygon = (InnoPolygon) getIntent();

    if (polygon == null) {
      System.out.println("Polygon is null");
      return;
    }

    if (Core().getSectionPrice(polygon.getID()) != null && Core().getSectionPrice(polygon.getID()).getPrice() != -1) {
      section_price_input.setText(Double.toString(Core().getSectionPrice(polygon.getID()).getPrice()));
    }

    section_name_input.setText(Core().getImmutableRoom().getSectionById(polygon.getID()).getNameSection());
    section_rotation_input.setText(Double.toString(polygon.getRotation().getAngle()));
    section_elevation_input
        .setText(Double.toString(Core().getImmutableRoom().getSectionById(polygon.getID()).getElevation()));
    section_auto_distrib_input.setSelected(
        ((ImmutableSittingSection) Core().getImmutableRoom().getSectionById(polygon.getID())).getAutoDistribution());
    section_auto_distrib_input.setIndeterminate(false);

    section_rotation_input
        .setText(Double.toString(Core().getImmutableRoom().getSectionById(polygon.getID()).getUserRotation()));

    section_vital_space_width_input
        .setText(Double.toString(polygon.getSittingData().getImmutableVitalSpace().getWidth()));
    section_vital_space_height_input
        .setText(Double.toString(polygon.getSittingData().getImmutableVitalSpace().getHeight()));

    // Price color
    ImmutablePlaceRate place = Core().getSectionPrice(polygon.getID());
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
        Core().addSectionOffer(polygon.getID(), available_offers_list.getFocusModel().getFocusedItem());
        available_offers_list.getItems().remove(available_offers_list.getFocusModel().getFocusedItem());
        refreshAttributedOffer();
      }
    });

    attributed_offers_list.setOnMouseClicked((e) -> {
      if (e.getClickCount() == 2 && attributed_offers_list.getFocusModel().getFocusedItem() != null) {
        Core().removeSectionOffer(polygon.getID(), attributed_offers_list.getFocusModel().getFocusedItem());
        available_offers_list.getItems().add(attributed_offers_list.getFocusModel().getFocusedItem());
        refreshAttributedOffer();
      }
    });

  }

  private void refreshAttributedOffer() {
    InnoPolygon polygon = (InnoPolygon) getIntent();

    if (polygon == null) {
      System.out.println("polygon is null");
      return;
    }

    Core().refreshOfferList();
    available_offers_list.setItems(Core().getObservableOffersList());

    ArrayList<? extends ImmutableOffer> offers = Core().getSectionPrice(polygon.getID()).getImmutableOffers();
    attributed_offers_list.getItems().clear();
    offers.forEach((offer) -> {
      available_offers_list.getItems().remove(offer.getName());
      attributed_offers_list.getItems().add(offer.getName());
    });
  }

  private void setRotation(Double angle, boolean input) {
    section_rotation_group.setRotate(angle - 90);
    if (input) {
      section_rotation_input.setText("" + (angle));
    }
    InnoPolygon polygon = (InnoPolygon) getIntent();

    Core().setSectionUserRotation(polygon.getID(), angle);
    polygon.updateFromData(false);
    // polygon.setRotationAngle(Core().getImmutableRoom().getSectionById(polygon.getID()).getRotation());
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

      // angle = convertTo360(angle);
      setRotation(angle, true);
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
      InnoPolygon polygon = (InnoPolygon) getIntent();

      if (polygon == null) {
        System.out.println("polygon is null");
        return;
      }
      try {
        if (section_rotation_input.isFocused())
          setRotation(Double.parseDouble(section_rotation_input.getText()), false);
        if (section_vital_space_width_input.isFocused() || section_vital_space_height_input.isFocused()) {
          double width = Double.parseDouble(section_vital_space_width_input.getText());
          double height = Double.parseDouble(section_vital_space_height_input.getText());
          polygon.setVitalSpace(width, height);
          ;
          polygon.updateFromData(false);
        }
        if (section_name_input.isFocused())
          Core().setSectionName(polygon.getID(), section_name_input.getText());
        if (section_elevation_input.isFocused())
          Core().setSectionElevation(polygon.getID(), Double.parseDouble(section_elevation_input.getText()));

        if ((section_price_input.isFocused() || section_price_color_picker.isFocused())
            && section_price_input.getText().trim().length() != 0) {
          if (section_price_input.isFocused()) {
            String color = Core().getColorPrice(Double.parseDouble(section_price_input.getText()));
            if (color != null) {
              section_price_color_picker.setValue(Color.valueOf(color));
              Core().setSectionPrice(polygon.getID(),
                Double.parseDouble(
                    section_price_input.getText().trim().length() != 0 ? section_price_input.getText() : "-1"),
                color);
            }
          }
          section_price_color_picker.setDisable(false);
          if (section_price_color_picker.isFocused()) {
            Core().setSectionPrice(polygon.getID(),
                Double.parseDouble(
                    section_price_input.getText().trim().length() != 0 ? section_price_input.getText() : "-1"),
                section_price_color_picker.getValue().toString());
            polygon.updateFromData(false);
          } else {
            Core().setSectionPrice(polygon.getID(), Double.parseDouble(
                section_price_input.getText().trim().length() != 0 ? section_price_input.getText() : "-1"));
          }
        } else if (section_price_input.isFocused() || section_price_color_picker.isFocused()) {
          Core().setSectionPrice(polygon.getID(), Double.parseDouble("-1"), "#6378bf");
          section_price_color_picker.setDisable(true);
          section_price_color_picker.setValue(Color.valueOf("#6378bf"));
          polygon.updateFromData(false);
        }

      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  @FXML
  private void sectionSittingToStanding() {
    InnoPolygon polygon = (InnoPolygon) getIntent();

    if (polygon == null) {
      System.out.println("polygon is null");
      return;
    }
    polygon.sittingToStanding();
  }

  @FXML
  private void sectionSittingIrregularAutoDistrib() {
    InnoPolygon polygon = (InnoPolygon) getIntent();
    ImmutableSittingSection section = (ImmutableSittingSection) Core().getImmutableRoom().getSectionById(polygon.getID());
    if (section.getAutoDistribution()) {
      section_auto_distrib_input.setSelected(false);
      Core().setSittingSectionAutoDistribution(polygon.getID(), false);
    }
    else {
      section_auto_distrib_input.setSelected(true);
      Core().setSittingSectionAutoDistribution(polygon.getID(), true);
    }
  }

  private boolean checkInputs() {
    boolean valid = true;

    HashMap<TextField, String> fields = new LinkedHashMap<>();
    fields.put(section_name_input, "required|max:30");
    fields.put(section_vital_space_width_input, "required|numeric");
    fields.put(section_vital_space_height_input, "required|numeric");
    fields.put(section_rotation_input, "required|numeric|min:-180|max:180");
    fields.put(section_elevation_input, "required|numeric");
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