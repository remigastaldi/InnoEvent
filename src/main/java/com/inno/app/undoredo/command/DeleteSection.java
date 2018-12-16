/*
 * File Created: Saturday, 15th December 2018
 * Author: GASTALDI Rémi
 * -----
 * Last Modified: Sunday, 16th December 2018
 * Modified By: GASTALDI Rémi
 * -----
 * Copyright - 2018 GASTALDI Remi
 * <<licensetext>>
 */


package com.inno.app.undoredo.command;

import java.util.HashMap;

import com.inno.app.room.ImmutableSittingSection;
import com.inno.app.room.Room;
import com.inno.app.room.Section;
import com.inno.service.pricing.ImmutablePlaceRate;
import com.inno.service.pricing.Pricing;
import com.inno.service.undoredo.Command;
import com.inno.ui.innoengine.InnoEngine;

public class DeleteSection implements Command {
  private InnoEngine _engine = null;
  private Room _room = null;
  private String _idSection = null;
  private Pricing _pricing = null;

  private ImmutableSittingSection _section = null;

  public DeleteSection(InnoEngine engine, Room room, Pricing pricing, String idSection) {
    _engine = engine;
    _room = room;
    _pricing = pricing;
    _idSection = idSection;
  }

  @Override
  public void execute() {
    deleteSection();
    _engine.deleteSection(_idSection);
  }

  @Override
  public void unExecute() {
    if (_section != null) {
      // TODO; Standing section
      // new CreateSittingSection(_engine, _room, _pricing, _section.getPositions(), _section.getRotation(), _section.isRectangle()).execute();
      CreateSittingSection command =  new CreateSittingSection(_engine, _room, _pricing,
        _section.getPositions(), _section.getRotation(), _section.isRectangle());
      _section = command.createSectionInDomain();

      _idSection = _section.getId();
      command.createSectionInEngine(_section);
    }
  }

  public void deleteSection() {
    try {
      Section section = _room.getSectionById(_idSection);
      if (section == null)
        return;

      _section =  (ImmutableSittingSection) section.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }


    HashMap<String, ? extends ImmutablePlaceRate> places = _pricing.getPlaces(_idSection);

    for (String key : places.keySet()) {
      _pricing.deletePlaceRate(key);
    }
    _room.deleteSection(_idSection);
  }
}